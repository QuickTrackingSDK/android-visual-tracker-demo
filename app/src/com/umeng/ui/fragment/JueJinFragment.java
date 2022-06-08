package com.umeng.ui.fragment;

import java.util.ArrayList;

import com.umeng.abtest.ActivityD;
import com.umeng.abtest.InfoListAdapter;
import com.umeng.abtest.Item;
import com.umeng.abtest.R;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bingoogolapple.bgabanner.BGABanner;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.umeng.utils.TagSwitch;
import com.umeng.vt.common.ViewTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangcui on 2018/11/1.
 */

public class JueJinFragment extends Fragment{
    private Context mContext;
    private RecyclerView mInfoList;//用于显示的列表

    private ArrayList<Item> mDatas;//用于储存数据

    private InfoListAdapter adapter;//适配器

    private int limit=20;//从第一页开始

    private RequestQueue mQueue;

    private ArrayList<Item> bannerList;//banner控件

    private ArrayList<String> titles;//存放banner中的标题

    private ArrayList<String> images;//存放banner中的图片

    private ArrayList<String> ids;//存放每一项的id

    private int mLastVisibleItemPosition;

    private String url = "https://juejin.im/";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        //获取网络数据
        mQueue = Volley.newRequestQueue(mContext);
        initDate();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.kr_fragment, container, false);

        initView(root);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDate();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }
    public static JueJinFragment newInstance() {
        return new JueJinFragment();
    }

    private void initView(View view) {
        mInfoList= (RecyclerView) view.findViewById(R.id.infolist);//绑定RecycleView
        mInfoList.setLayoutManager(new LinearLayoutManager(mContext));//设置布局管理器，你可以通过这个来决定你是要做一个Listview还是瀑布流
        adapter=new InfoListAdapter(mDatas, url, mContext);//初始化适配器

       // initBanner();
        mInfoList.setAdapter(adapter);//为ReycleView设置适配器
        mInfoList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    //发送网络请求获取更多数据
                    limit += 20;
                    getInfoFromNet();
                }
            }
        });

    }



    private void  initDate(){


        //将此处之前的for循环插入虚拟数据删除
        mDatas=new ArrayList<>();
        getInfoFromNet();
    }

    private void getInfoFromNet(){

        String targetUrl = "https://timeline-merger-ms.juejin.im/v1/get_entry_by_rank?src=web&category=all&limit="+limit;



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(targetUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray list = new JSONArray();
                    try {
                        list = response.getJSONObject("d").getJSONArray("entrylist");
                        //获取返回数据的内容

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //开始解析数据
                    for (int i = limit-20; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        String type = item.getString("type");

                        Item listItem = new Item();
                        //创建list中的每一个对象，并设置数据
                        listItem.setTitle(type+"="+item.getString("title"));
                        listItem.setImgurl(item.optString("screenshot"));
                        /*JSONArray imgArray = item.getJSONArray("pictures");
                        if(imgArray != null && imgArray.length()>0) {
                            listItem.setImgurl(item.getJSONArray("pictures").getString(0));
                        }*/
                        String dateStr = item.getString("updatedAt").replace("T"," ").replace("Z","");
                        if(dateStr.contains(".")) {
                            dateStr = dateStr.substring(0, dateStr.indexOf("."));
                        }
                        listItem.setDate(dateStr);
                        listItem.setId(item.getString("objectId"));
                        mDatas.add(listItem);
                    }
                    adapter.notifyDataSetChanged();//通知适配器 刷新数据啦
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            //如果遇到异常，在这里通知用户
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,"网络碰到了一点问题",Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);//开始任务
    }

    private void initBanner() {
        //初始化banner
        titles=new ArrayList<>();
        ids=new ArrayList<>();
        images=new ArrayList<>();

        bannerList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://news-at.zhihu.com/api/4/news/latest", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //解析banner中的数据
                    JSONArray topinfos = response.getJSONArray("top_stories");
                    Log.d("TAG", "onResponse: "+topinfos);
                    for (int i = 0; i < topinfos.length(); i++) {
                        JSONObject item = topinfos.getJSONObject(i);
                        Item item1 = new Item();
                        item1.setImgurl(item.getString("image"));
                        item1.setTitle(item.getString("title"));
                        item1.setId(item.getString("id"));
                        bannerList.add(item1);
                        titles.add(item1.getTitle());
                        images.add(item1.getImgurl());
                        ids.add(item1.getId());
                    }


                    setHeader(mInfoList, images, titles, ids);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(jsonObjectRequest);


    }

    private void setHeader(RecyclerView view, ArrayList<String> urls, ArrayList<String> titles, final ArrayList<String> ids) {
        View header = LayoutInflater.from(mContext).inflate(R.layout.headview, view, false);
        //找到banner所在的布局
        BGABanner banner = (BGABanner) header.findViewById(R.id.banner);
        //绑定banner
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {

            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(JueJinFragment.this)
                    .load(model)
                    .centerCrop()
                    .dontAnimate()
                    .into(itemView);
                //添加viewId
                if (TagSwitch.USE_TAG) {
                    ViewTools.setViewId(itemView, ids.get(position));
                }
            }
        });
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, final int position) {
                //此处可设置banner子项的点击事件
                Intent intent = new Intent(mContext, ActivityD.class);
                intent.putExtra("url","http://news-at.zhihu.com/story/"+ids.get(position));
                mContext.startActivity(intent);
            }
        });
        banner.setData(urls, titles);

        adapter.setHeadView(header);//向适配器中添加banner
    }
}
