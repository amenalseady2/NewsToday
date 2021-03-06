package com.booboomx.todaynews.ui.fragment;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.booboomx.todaynews.R;
import com.booboomx.todaynews.base.BaseFragment;
import com.booboomx.todaynews.model.NewsBean;
import com.booboomx.todaynews.presenter.NewListPresenter;
import com.booboomx.todaynews.ui.adapter.NewListAdapter;
import com.booboomx.todaynews.ui.view.NewListView;
import com.booboomx.todaynews.utils.ConstanceValue;
import com.booboomx.todaynews.utils.RxUtils;
import com.booboomx.todaynews.widget.ParallaxRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 *
 */
public class NewsListFragment extends BaseFragment<NewListPresenter> implements SwipeRefreshLayout.OnRefreshListener, NewListView {


    @BindView(R.id.recyclerView)
    ParallaxRecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    private String titleCode;
    private NewListAdapter mAdapter;
    protected List<NewsBean> mDatas = new ArrayList<>();


    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_news_list;
    }

    @Override
    public NewListPresenter createPresenter() {
        return new NewListPresenter(this);
    }


    @Override
    public void lazyFetchData() {
        super.lazyFetchData();


        mSwipeRefresh.setOnRefreshListener(this);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //手动调用,通知系统去测量
        mSwipeRefresh.measure(0, 0);
        mSwipeRefresh.setRefreshing(true);


        mAdapter = new NewListAdapter(mDatas);
        mRecyclerView.setAdapter(mAdapter);


        if (TextUtils.isEmpty(titleCode))
            titleCode = getArguments().getString(ConstanceValue.DATA);

        mvpPresenter.getNewsList(titleCode);




        //视频监听
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayer videoPlayer = JCVideoPlayerManager.getCurrentJcvd();
                    if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                        //当滑动的时，正在播放的视频移除屏幕，取消播放这个视频
                        JCVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });


    }


    @Override
    public void onRefresh() {

        Subscription subscription = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .compose(RxUtils.<Long>rxSchedulerHelper())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mvpPresenter.getNewsList(titleCode);
                        mSwipeRefresh.setRefreshing(false);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });


    }




    @Override
    public void onGetNewsListSuccess(List<NewsBean> dataBeanList) {
        mDatas.clear();
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        //由于最后一条重复 ，删除掉
        if (dataBeanList.size() > 0)
            dataBeanList.remove(dataBeanList.size() - 1);
        mDatas.addAll(0, dataBeanList);
        mAdapter.notifyItemRangeChanged(0, dataBeanList.size());


    }
}
