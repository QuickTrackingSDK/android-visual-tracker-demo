package com.umeng.abtest;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.abtest.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by wangcui on 2018/10/31.
 */

public class ActivityD  extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zh_webview);

        WebView webView = (WebView)findViewById(R.id.zh_webview);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        String deatilurl = getIntent().getStringExtra("url");

        if (deatilurl!=null){
            webView.loadUrl(deatilurl);
            // WebViewClient会在一些影响内容喧嚷的动作发生时被调用，比如表单的错误提交需要重新提交、页面开始加载及加载完成、资源加载中、接收到http认证需要处理、页面键盘响应、页面中的url打开处理等等
            webView.setWebViewClient(new WebViewClient());
            //去掉中间 跳转的 bug 跳转中途没有空白页
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            //启动缓存
            settings.setAppCacheEnabled(true);
            //设置缓存模式
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //设置加载进来的页面自适应手机屏幕
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            //设置UA
            settings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            //settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            // 加快HTML网页加载完成速度
            if (Build.VERSION.SDK_INT >= 19) {
                settings.setLoadsImagesAutomatically(true);
            } else {
                settings.setLoadsImagesAutomatically(false);
            }

        }
        //禁止水平滚动，在样式外添加ScrollView
        webView.setScrollContainer(false);
        /*webView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }
}
