package com.xbx.client.beans;

/**
 * Created by EricYuan on 2016/4/14.
 */
public class MyGuideInfoBean {
    private String guidePhone;
    private String guideHeadImg;
    private String guideName;
    private double guideLon;
    private double guideLat;
    private String guideNum;
    private String guideStarts;
    private long startTime;
    private long currentTime;

    public String getGuidePhone() {
        return guidePhone;
    }

    public void setGuidePhone(String guidePhone) {
        this.guidePhone = guidePhone;
    }

    public String getGuideHeadImg() {
        return guideHeadImg;
    }

    public void setGuideHeadImg(String guideHeadImg) {
        this.guideHeadImg = guideHeadImg;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public double getGuideLon() {
        return guideLon;
    }

    public void setGuideLon(double guideLon) {
        this.guideLon = guideLon;
    }

    public double getGuideLat() {
        return guideLat;
    }

    public void setGuideLat(double guideLat) {
        this.guideLat = guideLat;
    }

    public String getGuideNum() {
        return guideNum;
    }

    public void setGuideNum(String guideNum) {
        this.guideNum = guideNum;
    }

    public String getGuideStarts() {
        return guideStarts;
    }

    public void setGuideStarts(String guideStarts) {
        this.guideStarts = guideStarts;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
