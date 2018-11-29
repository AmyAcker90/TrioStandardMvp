package com.trio.standard.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * Created by lixia on 2018/10/10.
 */

public class ImageUtil {

    private static Context mContext;

    private int placeholder;
    private int errorholder;

    public static ImageUtil getInstance(Context context) {
        mContext = context;
        return ImageUtilHolder.mInstance;
    }

    private static class ImageUtilHolder {
        private static final ImageUtil mInstance = new ImageUtil();
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public void setErrorholder(int errorholder) {
        this.errorholder = errorholder;
    }

    public void load(String url, ImageView view) {
        Glide.with(mContext)
                .load(url)
                .placeholder(placeholder)
                .error(errorholder)
                .priority(Priority.HIGH)
                .into(view);
    }

    public void loadCenterCrop(String url, ImageView view) {
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(placeholder)
                .error(errorholder)
                .priority(Priority.HIGH)
                .into(view);
    }

    public void loadRound(String url, ImageView view) {
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                //圆形裁剪
                .bitmapTransform(new BitmapUtil.CropCircleTransformation(mContext))
                .placeholder(placeholder)
                .error(errorholder)
                .priority(Priority.HIGH)
                .into(view);
    }

    public void loadRadius(String url, ImageView view, int dp) {
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                //居中与圆角
                .bitmapTransform(new CenterCrop(mContext), new BitmapUtil.GlideRoundTransform(mContext, dp))
                .placeholder(placeholder)
                .error(errorholder)
                .priority(Priority.HIGH)
                .into(view);
    }
}
