package com.umeng.abtest;

import java.util.List;

/**
 * Created by wangcui on 2018/10/31.
 */

public class Item {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getHeadtitle() {
        return headtitle;
    }

    public void setHeadtitle(String headtitle) {
        this.headtitle = headtitle;
    }

    public List<String> getImgArray() {
        return imgArray;
    }

    public void setImgArray(List<String> imgArray) {
        this.imgArray = imgArray;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    String userName;

    String headtitle;//头部标题
    String id;//id
    String title;//文章标题
    String date;//日期
    String imgurl;//配图地址
    List<String> imgArray;
    String videoUrl;

}
