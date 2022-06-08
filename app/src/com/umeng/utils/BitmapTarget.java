package com.umeng.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by wangcui on 2018/11/5.
 */

public class BitmapTarget extends SimpleTarget<Bitmap> {
    private final MyDrawableWrapper myDrawable;
    private final Context mContext;
    private final TextView textView;

    public BitmapTarget(MyDrawableWrapper myDrawable, Context mContext, TextView textView) {
        this.myDrawable = myDrawable;
        this.mContext = mContext;
        this.textView = textView;
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        Drawable drawable = new BitmapDrawable(resource);
        //获取原图大小
        int width=drawable.getIntrinsicWidth() ;
        int height=drawable.getIntrinsicHeight();
        //自定义drawable的高宽, 缩放图片大小最好用matrix变化，可以保证图片不失真
        drawable.setBounds(0, 0, (int)(textView.getTextSize()*1.3), (int)(textView.getTextSize()*1.3));
        myDrawable.setBounds(0, 0,  (int)(textView.getTextSize()*1.3), (int)(textView.getTextSize()*1.3));
        myDrawable.setDrawable(drawable);
        textView.setText(textView.getText());
        textView.invalidate();

    }
}
