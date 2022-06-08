package com.umeng.abtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.umeng.abtest.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.spm.SpmAgent;


import java.util.ArrayList;

public class UMAdapterViewActivity extends Activity implements AdapterView.OnItemClickListener {

    private String[] names = new String[]{"猪八戒","孙悟空","唐僧"};
    private String[] says = new String[]{"饿了","吃俺老孙一棒","紧箍咒"};
    private  int[] images = new int[]{R.drawable.aa,R.drawable.aa,R.drawable.aa};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_view);
        ListView listView = (ListView) findViewById(R.id.listview);
        ArrayList<UMAdapterItem> aData = new ArrayList<>();
        for (int i=0;i<names.length;i++){
            aData.add(new UMAdapterItem(images[i], names[i],says[i], null));
        }

        listView.setAdapter(new UMAdapterView(UMAdapterViewActivity.this, aData));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(UMAdapterViewActivity.this,"你点击了第" + position + "项",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PAGE_E");
        SpmAgent.updateCurSpm(this, "PAGE_E");
        //MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PAGE_E");
        //MobclickAgent.onPause(this);
    }
}
