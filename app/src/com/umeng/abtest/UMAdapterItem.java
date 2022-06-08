package com.umeng.abtest;

public class UMAdapterItem {

    private int iCont;
    private String title;
    private String content;
    private String other;

    public UMAdapterItem(int iCont, String title, String content, String other) {
        this.iCont = iCont;
        this.title = title;
        this.content = content;
        this.other = other;
    }

    public int getiCont() {
        return iCont;
    }
    public void setiCont(int iCont) {
        this.iCont = iCont;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getOther() {
        return other;
    }
    public void setOther(String other) {
        this.other = other;
    }


}
