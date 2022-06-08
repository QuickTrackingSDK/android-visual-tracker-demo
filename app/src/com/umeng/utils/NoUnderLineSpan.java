package com.umeng.utils;

import com.umeng.abtest.ActivityD;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by wangcui on 2018/11/5.
 */

public class NoUnderLineSpan extends ClickableSpan{
    private Context mContext;
    private String url;
    public NoUnderLineSpan(Context context, String src) {
        super();
        mContext = context;
        url = src;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(Color.parseColor("#00B2EE"));
    }

    @Override
    public void onClick(View widget) {
        Intent intent = new Intent(mContext, ActivityD.class);
        intent.putExtra("url", url);
        mContext.startActivity(intent);
    }
}
