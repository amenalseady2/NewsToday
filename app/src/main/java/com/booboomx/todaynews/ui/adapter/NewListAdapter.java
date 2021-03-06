package com.booboomx.todaynews.ui.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.booboomx.todaynews.R;
import com.booboomx.todaynews.model.ImageUrl;
import com.booboomx.todaynews.model.NewsBean;
import com.booboomx.todaynews.model.Video;
import com.booboomx.todaynews.utils.DateUtils;
import com.booboomx.todaynews.utils.ImageLoaderUtils;
import com.booboomx.todaynews.utils.JumpUtils;
import com.booboomx.todaynews.utils.VideoPathDecoder;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by booboomx on 17/4/5.
 */

public class NewListAdapter extends BaseMultiItemQuickAdapter<NewsBean, BaseViewHolder> {

    public static final String TAG=NewListAdapter.class.getSimpleName();
    public NewListAdapter(List<NewsBean> data) {
        super(data);
        addItemType(NewsBean.ARTICLE_NO_IMAGE, R.layout.item_article_no_image);
        addItemType(NewsBean.ARTICLE_IMAGE, R.layout.item_article_image);
        addItemType(NewsBean.GALLERY_NO_IMAGE, R.layout.item_gallery_no_image);
        addItemType(NewsBean.GALLERY_IMGE, R.layout.item_gallery_image);
        addItemType(NewsBean.VIDEO, R.layout.item_video);
    }

    @Override
    protected void convert(BaseViewHolder holder, final NewsBean item) {

        final String title = item.getTitle();//标题
        List<ImageUrl> image_list =
                item.getImage_list();
        final String source = item.getSource();//来源
        String image_url = item.getImage_url();
        String video_duration_str = item.getVideo_duration_str();//视频播放时长

        int gallary_image_count = item.getGallary_image_count();//图片集合个数
        int comments_count = item.getComments_count();//评论个数
        long behot_time = item.getBehot_time();//时间

        final String source_url = item.getSource_url();

        String media_avatar_url = item.getMedia_avatar_url();//媒体图片头像


        String concat = "http://m.toutiao.com".concat(source_url);

        Log.i(TAG, "convert: concat-->"+concat);


        switch (holder.getItemViewType()) {
            case NewsBean.ARTICLE_NO_IMAGE:

                holder.setText(R.id.tv_title,title)
                        .setText(R.id.tvAuthorName,source)
                        .setText(R.id.tvCommentCount,comments_count+"评论")
                        .setText(R.id.tvTime, DateUtils.getShortTime(behot_time*1000));

                ImageView  ivRightImage = holder.getView(R.id.iv_right_image);
                if(!TextUtils.isEmpty(image_url)){
                    ImageLoaderUtils.loadImge(mContext,image_url,ivRightImage);
                }


                holder.getView(R.id.cardView)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                JumpUtils.go2NewsDetailActivity(mContext,source,"http://m.toutiao.com".concat(source_url));
                            }
                        });


                break;
            case NewsBean.ARTICLE_IMAGE:
                holder.setText(R.id.tv_title,title)
                        .setText(R.id.tvAuthorName,source)
                        .setText(R.id.tvCommentCount,comments_count+"评论")
                        .setText(R.id.tvTime, DateUtils.getShortTime(behot_time*1000));

                if(image_list!=null&&image_list.size()>0){

                    ImageUrl imageUrl = image_list.get(0);
                    ImageUrl imageUrl2 = image_list.get(1);
                    ImageUrl imageUrl3 = image_list.get(2);


                    ImageLoaderUtils.loadImge(mContext,imageUrl.getUrl(), (ImageView) holder.getView(R.id.iv_image1));
                    ImageLoaderUtils.loadImge(mContext,imageUrl2.getUrl(), (ImageView) holder.getView(R.id.iv_image2));
                    ImageLoaderUtils.loadImge(mContext,imageUrl3.getUrl(), (ImageView) holder.getView(R.id.iv_image3));


                }


                holder.getView(R.id.cardView)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                JumpUtils.go2NewsDetailActivity(mContext,source,"http://m.toutiao.com".concat(source_url));
                            }
                        });

                break;
            case NewsBean.GALLERY_NO_IMAGE:
                holder.setText(R.id.tv_title,title)
                        .setText(R.id.tvAuthorName,source)
                        .setText(R.id.tvCommentCount,comments_count+"评论")
                        .setText(R.id.tvTime, DateUtils.getShortTime(behot_time*1000));


                if(!TextUtils.isEmpty(image_url)){
                    ImageLoaderUtils.loadImge(mContext,image_url, (ImageView) holder.getView(R.id.iv_right_image));
                }else{

                    if(!TextUtils.isEmpty(media_avatar_url)){
                        ImageLoaderUtils.loadImge(mContext,media_avatar_url, (ImageView) holder.getView(R.id.iv_right_image));

                    }



                }


                holder.getView(R.id.cardView)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                JumpUtils.go2NewsDetailActivity(mContext,source,"http://m.toutiao.com".concat(source_url));
                            }
                        });

                break;
            case NewsBean.GALLERY_IMGE:
                holder.setText(R.id.tv_title,title)
                        .setText(R.id.tvAuthorName,source)
                        .setText(R.id.tvCommentCount,comments_count+"评论")
                        .setText(R.id.tvImgCount,gallary_image_count+"图")
                        .setText(R.id.tvTime, DateUtils.getShortTime(behot_time*1000));


                if(image_list!=null&&image_list.size()>0){
                    ImageLoaderUtils.loadImge(mContext,image_list.get(0).getUrl(), (ImageView) holder.getView(R.id.ivBigImg));

                }


                holder.getView(R.id.cardView)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                JumpUtils.go2NewsDetailActivity(mContext,source,"http://m.toutiao.com".concat(source_url));
                            }
                        });

                break;
            case NewsBean.VIDEO:

                if(!TextUtils.isEmpty(media_avatar_url)){
                    ImageLoaderUtils.loadeCircleImage(mContext,media_avatar_url, (ImageView) holder.getView(R.id.ivAvatar));
                }

                holder.setText(R.id.tvFrom,source)
                        .setText(R.id.tvCommentCount,comments_count+"");

                final JCVideoPlayerStandard videoPlayer = holder.getView(R.id.videoPlayer);


                videoPlayer.titleTextView.setText(title);

                if(!TextUtils.isEmpty(image_url)){
                    ImageLoaderUtils.loadImge(mContext,image_url,videoPlayer.thumbImageView);
                }

//                videoPlayer.setDurationText(video_duration_str);
                if (item.video == null) {
                    //由于地址加密，所以抽出一个类专门解析播放地址
                    VideoPathDecoder decoder = new VideoPathDecoder() {
                        @Override
                        public void onSuccess(Video s) {
                            item.video = s;
                            setPlayer(videoPlayer, item);
                        }

                        @Override
                        public void onDecodeError(Throwable e) {

                        }
                    };
                    decoder.decodePath(item.source_url);
                } else {
                    setPlayer(videoPlayer, item);
                }

                break;


        }

    }

    private void setPlayer(JCVideoPlayerStandard videoPlayer, NewsBean news) {
        Log.i(TAG, "setPlayer: "+news.video.main_url);
        videoPlayer.setUp(news.video.main_url, JCVideoPlayer.SCREEN_LAYOUT_LIST, news.title);
    }
}
