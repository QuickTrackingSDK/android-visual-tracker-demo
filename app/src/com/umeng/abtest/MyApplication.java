package com.umeng.abtest;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.utils.TagSwitch;
import com.umeng.visual.VisualAgent;

import android.app.Application;

/**
 * Created by cnzz on 18/7/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TagSwitch.USE_TAG = true; // 默认优先使用tag
        /**
         * umeng初始化
         */
        UMConfigure.preInit(this, "您的appkey", "aliyun");
        UMConfigure.setLogEnabled(true);
        // 私有化版本，需要提前设置收数域名
        UMConfigure.setCustomDomain("https://您的收数域名", null);
        VisualAgent.setWssDomain("wss://您的平台域名");

        UMConfigure.init(this, "您的appkey", "aliyun", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.disableActivityPageCollection();
    }

}
