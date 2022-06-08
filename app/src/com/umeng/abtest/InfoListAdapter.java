package com.umeng.abtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.umeng.abtest.R;
import com.umeng.utils.BitmapTarget;
import com.umeng.utils.MyDrawableWrapper;
import com.umeng.utils.NoUnderLineSpan;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import com.bumptech.glide.Glide;
import com.umeng.utils.TagSwitch;
import com.umeng.vt.common.ViewTools;

/**
 * Created by wangcui on 2018/10/31.
 */

public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.InfoViewHolder> {
    private ArrayList<Item> mData;//用于储存数据
    private Context mContext;//上下文
    InfoViewHolder holder=null; //viewholde，可以提高recycleview的性能
    private String url = null;
    private String type = null;
    private static Pattern pattern = Pattern.compile("<a((?!<a).)*<\\/a>");
    private static Pattern urlText = Pattern.compile(">((?!(>|<)).)+<");



    public  InfoListAdapter(ArrayList<Item> data, String url, Context context) {

        //构造方法，用于接收上下文和展示数据

        this.mData = data;
        this.url = url;
        this.mContext=context;
    }


    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里进行过改动
        if (headView!=null && viewType==TYPE_HEADER) {
            return new InfoViewHolder(headView);
        }
        if(url.contains("juejin")) {
            holder = new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.jj_info_item, parent, false));
        } else if(url.contains("weibo")) {
            holder = new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.weibo_info_item, parent, false));
        } else {
            holder = new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.info_item, parent, false));

        }
        return holder;


    }

    @Override
    public void onBindViewHolder(final InfoViewHolder holder, int position) {
        if (getItemViewType(position)==TYPE_HEADER) {
            return;
        }
        final int pos=getRealPosition(holder);



        if(url.contains("juejin")) {
            String title = mData.get(pos).getTitle();
            type = title.substring(0,title.indexOf("="));
            holder.title.setText(title.substring(title.indexOf("=")+1));
            holder.dateText.setText(mData.get(pos).getDate());
        } else if (url.contains("weibo")){
            holder.linearLayout.removeAllViews();

            holder.userText.setText(mData.get(pos).getUserName());
            holder.dateText.setText(mData.get(pos).getDate());
            Html.ImageGetter imageGetter = new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    if(!source.contains("http")) {
                        source = "https:" + source;
                    }
                    MyDrawableWrapper myDrawable = new MyDrawableWrapper();
                    Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
                    myDrawable.setDrawable(drawable);
                    Glide.with(mContext)
                        .load(source)
                        .asBitmap()
                        .into(new BitmapTarget(myDrawable, mContext, holder.title));
                    return myDrawable;
                }
            };
            if(VERSION.SDK_INT > VERSION_CODES.N) {
                holder.title.setText(Html.fromHtml(mData.get(pos).getTitle(),Html.FROM_HTML_MODE_LEGACY,imageGetter ,null));
            } else {
                holder.title.setText(Html.fromHtml(mData.get(pos).getTitle(), imageGetter, null));
            }

            String text = mData.get(pos).getTitle();
            SpannableString span = new SpannableString(holder.title.getText());
            Map<String, String> urlToContent = new HashMap<>();

            Matcher m = pattern.matcher(text);
            while(m.find()){
                String item = m.group(0);
                String str = item.substring(item.indexOf("href=")+6);
                String sign = item.substring(item.indexOf("href=")+5,item.indexOf("href=")+6);
                String href = str.substring(0, str.indexOf(sign));
                if(!href.contains("http")) {
                    href = "https://m.weibo.cn"+href;
                }
                Matcher m2 = urlText.matcher(item);
                String content = null;
                if(m2.find()) {
                    content = m2.group(0);
                    content = content.substring(1, content.length()-1);
                }
                urlToContent.put(content, href);
            }

            try {
                for(String key: urlToContent.keySet()) {
                    ClickableSpan noUnderLineSpan = new NoUnderLineSpan(mContext, urlToContent.get(key));
                    int start = holder.title.getText().toString().indexOf(key);
                    int end = holder.title.getText().toString().indexOf(key) + key.length();
                    span.setSpan(noUnderLineSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.title.setText(span);
                holder.title.setMovementMethod(LinkMovementMethod.getInstance());
                if (TagSwitch.USE_TAG) {
                    ViewTools.setViewId(holder.title, "" + holder.title.getText());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            List<String> imgArray = mData.get(pos).getImgArray();
            int size = imgArray.size();


            for(int i=0;i<size;i++) {
                if(i % 3 == 0) {
                    LinearLayout linearLayout = new LinearLayout(mContext);
                    linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    holder.linearLayout.addView(linearLayout);
                }
                ImageView imageView = new ImageView(mContext);
                imageView.setPadding(10, 2,0,0);
                if(size >= 3) {
                    imageView.setLayoutParams(
                        new LayoutParams(mContext.getResources().getDisplayMetrics().widthPixels / 3, LayoutParams.WRAP_CONTENT));
                }
                if(size == 2){
                    imageView.setLayoutParams(
                        new LayoutParams(mContext.getResources().getDisplayMetrics().widthPixels / 2, LayoutParams.WRAP_CONTENT));
                }
                if(size == 1) {
                    imageView.setLayoutParams(
                        new LayoutParams((int)(mContext.getResources().getDisplayMetrics().widthPixels / 1.5), LayoutParams.WRAP_CONTENT));
                }

                Glide.with(mContext).load(imgArray.get(i)).into(imageView);

                ((LinearLayout) holder.linearLayout.getChildAt(i/3)).addView(imageView);
            }
            if(mData.get(pos).getVideoUrl() != null) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 600));
                linearLayout.setPadding(20,20,20, 20);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                JzvdStd jzvdStd = new JzvdStd(mContext);

                jzvdStd.setUp(
                    mData.get(pos).getVideoUrl()
                    , "", Jzvd.SCREEN_WINDOW_NORMAL);
                Glide.with(mContext).load(mData.get(pos).getImgurl()).into(jzvdStd.thumbImageView);
                if (TagSwitch.USE_TAG) {
                    ViewTools.setViewId(jzvdStd.startButton, "play_" + mData.get(pos).getVideoUrl());
                }
                linearLayout.addView(jzvdStd);
                holder.linearLayout.addView(linearLayout);
            }

        } else{
            holder.title.setText(mData.get(pos).getTitle());
        }


        View.OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityD.class);
                if(url.contains("36")) {
                    intent.putExtra("url", url + mData.get(pos).getId()+".html");
                } else if(url.contains("juejin")) {
                    intent.putExtra("url", url + "entry/"+ mData.get(pos).getId());
                }
                else{
                    intent.putExtra("url", url + mData.get(pos).getId());
                }

                mContext.startActivity(intent);
            }
        };
        if(!url.contains("weibo")) {
            holder.title.setOnClickListener(listener);
            holder.img.setOnClickListener(listener);
            Glide.with(mContext).load(mData.get(pos).getImgurl()).into(holder.img);
            //设置viewId
            /*ViewTools.setViewId(holder.title, "title_"+holder.title.getText());
            ViewTools.setViewId(holder.img, "img_"+holder.title.getText());*/
            if (TagSwitch.USE_TAG) {
                String titleContent = holder.title.getText().toString();
                ViewTools.setViewId(holder.title, "title_test", pos);
                ViewTools.setViewId(holder.img, "img_test");
            }
        } else{
            //holder.title.setOnTouchListener(MyLinkMovementMethod.getInstance());
        }






    }


    @Override
    public int getItemCount() {

        //获取数据长度
        if(mData != null) {
            return headView == null ? mData.size() : mData.size() + 1;
        } else {
            return 0;
        }
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {

        TextView title;//标题
        ImageView img;//显示的图片
        TextView headTitle;//头部标题
        TextView dateText;//日期
        TextView userText;
        LinearLayout linearLayout;


        public InfoViewHolder(View itemView) {
            super(itemView);

            title= (TextView) itemView.findViewById(R.id.item_title);
            img= (ImageView) itemView.findViewById(R.id.item_image);
            headTitle= (TextView) itemView.findViewById(R.id.item_headtitle);
            if(url.contains("juejin")) {
                dateText = (TextView) itemView.findViewById(R.id.item_date);
            } else if(url.contains("weibo")) {
                dateText = (TextView) itemView.findViewById(R.id.item_date);
                userText = (TextView)itemView.findViewById(R.id.item_user);
                linearLayout = (LinearLayout)itemView.findViewById(R.id.item_image_array);
            }
        }
    }


    //下面的是banner

    public static final int TYPE_HEADER = 0;//显示headview
    public static final int TYPE_NORMAL = 1;//显示普通的item
    private View headView;//这家伙就是Banner

    public void setHeadView(View headView){
        this.headView=headView;
        notifyItemInserted(0);
    }

    public View getHeadView(){
        return headView;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView==null) {
            return TYPE_NORMAL;
        }
        if (position==0) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position=holder.getLayoutPosition();
        return headView==null? position:position-1;
    }



}
