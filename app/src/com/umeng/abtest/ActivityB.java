package com.umeng.abtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.umeng.abtest.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.spm.SpmAgent;

public class ActivityB extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
    }

    public void onClick(View v) {
        
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PAGE_B");
        SpmAgent.updateCurSpm(this, "PAGE_B");
        //MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PAGE_B");
        //MobclickAgent.onPause(this);
    }
}
