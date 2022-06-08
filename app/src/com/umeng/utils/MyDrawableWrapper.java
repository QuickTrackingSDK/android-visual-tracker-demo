package com.umeng.utils;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by wangcui on 2018/11/5.
 */

public class MyDrawableWrapper extends BitmapDrawable {
    private Drawable drawable;
    public MyDrawableWrapper() {
    }
    @Override
    public void draw(Canvas canvas) {
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }
    public Drawable getDrawable() {
        return drawable;
    }
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
