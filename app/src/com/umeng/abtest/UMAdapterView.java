package com.umeng.abtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.abtest.R;

import java.util.ArrayList;

public class UMAdapterView extends BaseAdapter {

    private Context mContext;
    private ArrayList<UMAdapterItem> list;

    @SuppressWarnings("unchecked")
    public UMAdapterView(Context mContext, ArrayList list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adaper_item, parent, false);
            holder = new ViewHolder();
            holder.icon = (ImageView)convertView.findViewById(R.id.icon);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.content = (TextView)convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.icon.setBackgroundResource(list.get(position).getiCont());
        holder.title.setText(list.get(position).getTitle());
        holder.content.setText(list.get(position).getContent());
        return convertView;
    }

    static class ViewHolder{
        ImageView icon;
        TextView title;
        TextView content;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
