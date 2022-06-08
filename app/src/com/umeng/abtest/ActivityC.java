package com.umeng.abtest;

import java.lang.reflect.Field;

import com.umeng.analytics.MobclickAgent;
import com.umeng.spm.SpmAgent;
import com.umeng.ui.fragment.JueJinFragment;
import com.umeng.ui.fragment.WBFragment;
import com.umeng.ui.fragment.ZHFragment;
import com.umeng.utils.TagSwitch;
import com.umeng.vt.common.ViewTools;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.view.View;

import cn.jzvd.Jzvd;

/**
 * Created by wangcui on 2018/10/31.
 */

public class ActivityC extends Activity {



    private Fragment zhFragment;
    private Fragment wbFragment;
    //private Fragment jjFragment;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);

        zhFragment = ZHFragment.newInstance();
        wbFragment = WBFragment.newInstance();
        //jjFragment = JueJinFragment.newInstance();
        initTabLayout();

    }
    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                onTabItemSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
        Tab tab1 = tabLayout.newTab().setText("知乎");

        Tab tab2 = tabLayout.newTab().setText("微博");

        //Tab tab3 =tabLayout.newTab().setText("掘金");


        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        //tabLayout.addTab(tab3);
        if (TagSwitch.USE_TAG) {
            ViewTools.setViewId(getTabView(0), "知乎");
            ViewTools.setViewId(getTabView(1), "微博");
            //ViewTools.setViewId(getTabView(2), "掘金");
        }


    }
    private void onTabItemSelected(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = zhFragment;
                break;
            case 1:
                fragment = wbFragment;
                break;
            case 2:
                //fragment = jjFragment;

        }
        if(fragment!=null) {
            getFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }

    public View getTabView(int index){
        View tabView = null;
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        Field view = null;
        try {
            view = TabLayout.Tab.class.getDeclaredField("mView");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        view.setAccessible(true);
        try {
            tabView = (View) view.get(tab);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tabView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PAGE_C");
        SpmAgent.updateCurSpm(this, "PAGE_C");
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("PAGE_C");
        //MobclickAgent.onPause(this);

        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

}

