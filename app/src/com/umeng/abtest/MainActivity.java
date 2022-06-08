package com.umeng.abtest;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.umeng.abtest.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.spm.SpmAgent;
import com.umeng.utils.TagSwitch;
import com.umeng.vt.common.ParamUtils;
import com.umeng.vt.common.ViewTools;
//import com.umeng.vt.common.ViewTools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private UmengHandler mMessageThreadHandler;

    private Context appContext;
    private static boolean backClicked = false;

    private void back() {
        Timer timer = null;
        if (!backClicked) {
            backClicked = true;
            Toast.makeText(getApplicationContext(), "再点一次退出应用", Toast.LENGTH_SHORT).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    backClicked = false;
                }
            }, 2000);//设置在2两秒内再次点击back键能够退出程序
        } else {
            MobclickAgent.onKillProcess(appContext);
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            back();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appContext = this.getApplicationContext();

        initListView();

        registerHandler();
        if (TagSwitch.USE_TAG) {
            ViewTools.setViewId(findViewById(R.id.btnSave), "SaveButton");
            ParamUtils.setParam(findViewById(R.id.btnSave), "v_key1", "v1");
            Map<String, String> viewMap = new HashMap<String, String>();
            viewMap.put("v_key2", "v2");
            viewMap.put("v_key3", "v3");
            ParamUtils.setParam(findViewById(R.id.btnSave), viewMap);

            ViewTools.setViewId(findViewById(R.id.tvDeIP), "文本测试");
            ViewTools.setViewId(findViewById(R.id.button1), "切换页面B");
            ViewTools.setViewId(findViewById(R.id.button3), "切换页面C");

            Map<String, String> argsMap = new HashMap<String, String>();
            argsMap.put("p_key1", "test1");
            argsMap.put("p_key2", "test2");
            argsMap.put("p_key3", "test3");
            argsMap.put("p_key4", "test4");

            ParamUtils.setParam("TEST", argsMap);
        }
    }

    private void initListView() {

        ListView listView = (ListView)findViewById(R.id.testListView);

        String[] data={"tiger","panda","elephant","rabbit","penguin","dog","cattle","cat",
                "horse","sheep","monkey","bear","wolf","fox","donkey","ferret"};
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,data);

        listView.setAdapter(arrayAdapter);

    }

    @Override
    protected void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("MainPage");
    }

    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btnSave:

                Toast.makeText(v.getContext().getApplicationContext(),"这是业务逻辑",Toast.LENGTH_SHORT).show();


                break;
            case R.id.button1:

                mMessageThreadHandler.start();
                mMessageThreadHandler.sendMessage(mMessageThreadHandler.obtainMessage(1));
                break;
            case R.id.button3:
                mMessageThreadHandler.start();
                mMessageThreadHandler.sendMessage(mMessageThreadHandler.obtainMessage(2));
                break;

            case R.id.button2:
                mMessageThreadHandler.start();
                mMessageThreadHandler.sendMessage(mMessageThreadHandler.obtainMessage(3));
                break;

            case R.id.button4:
                startActivity(new Intent(MainActivity.this, UMBaseViewActivity.class));
                break;

            case R.id.button5:
                startActivity(new Intent(MainActivity.this, UMAdapterViewActivity.class));

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("MainPage");
        SpmAgent.updateCurSpm(this, "MainPage");

    }

    private void registerHandler() {

        final HandlerThread thread = new HandlerThread(MainActivity.class.getCanonicalName());
        thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mMessageThreadHandler = new UmengHandler(this, thread.getLooper());
    }

    private class UmengHandler extends Handler {
        private final Lock mStartLock;

        public UmengHandler(Context context, Looper looper) {
            super(looper);
            mStartLock = new ReentrantLock();
            mStartLock.lock();
        }

        public void start() {
            try {
                mStartLock.unlock();
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            mStartLock.lock();
            try {

                final int what = msg.what;
                switch (what) {
                    case 0:
                        try {
                            //save ip:port
                            EditText et = (EditText)MainActivity.this.findViewById(R.id.edIp);
                            String ip = et.getText().toString();
                            if (TextUtils.isEmpty(ip)) {
                                Toast.makeText(MainActivity.this, "请检查IP", Toast.LENGTH_LONG).show();
                            } else {
                                String connectURL = "wss://" + ip.trim() + ":9443/";
                                SharedPreferences sp = MainActivity.this.getSharedPreferences("url",
                                    Context.MODE_PRIVATE);
                                sp.edit().putString("url", connectURL).commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        //go activity b
                        startActivity(new Intent(MainActivity.this, ActivityB.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, ActivityC.class));
                        break;

                    default:
                        break;
                }
            } finally {
                mStartLock.unlock();
            }
        }
    }

}
