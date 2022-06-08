package com.umeng.abtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.umeng.abtest.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.spm.SpmAgent;

import java.util.HashMap;
import java.util.Map;


public class UMBaseViewActivity extends Activity {
    private Context appContext;
    private Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_view);
        appContext = this.getApplicationContext();

        findViewById(R.id.textview1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(UMBaseViewActivity.this, "onClick", Toast.LENGTH_LONG).show();
            }
        });

        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> ekvs = new HashMap<String, Object>();
                ekvs.put("ekvKey1", "customEvent");
                ekvs.put("kevkey2", "customArgs");
                MobclickAgent.onEventObject(appContext, "myEkv", ekvs);
            }
        });

    }

    public void onTextViewClick(View view){

        Toast.makeText(UMBaseViewActivity.this, "onTextViewClick", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("PAGE_D");
//        SpmAgent.updateCurSpm(this, "PAGE_D");

    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("PAGE_D");

    }
}
