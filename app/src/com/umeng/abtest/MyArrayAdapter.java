package com.umeng.abtest;


import com.umeng.abtest.R;
import com.umeng.vt.common.ViewTools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by wangcui on 2018/11/30.
 */

public class MyArrayAdapter extends ArrayAdapter {
    private String[] data;
    private Context mContext;
    private LayoutInflater inflater;

    @SuppressWarnings("unchecked")
    public MyArrayAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
        mContext = context;
        data = (String[])objects;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            View newView = inflater.inflate(R.layout.arrayadapter_item, parent, false);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 150);

            view = newView.findViewById(R.id.arrayadapter_text);

            view.setLayoutParams(layoutParams);
        }
        ((TextView)view).setText(data[position]);

        //ViewTools.setViewId(view, "listview_test_id", data[position]);
        // 使用位置索引值做下标
        ViewTools.setViewId(view, "listview_test_id", position);


        return view;
    }

}
