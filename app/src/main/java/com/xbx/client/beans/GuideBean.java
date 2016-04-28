package com.xbx.client.beans;

import java.io.Serializable;

/**
 * Created by EricYuan on 2016/4/7.
 */
public class GuideBean implements Serializable{
    private String guideId;
    private Double longitude;
    private Double latitude;
    private String guideHead;
    private String guideName;
    private String guideHourPrice;
    private String guideTimes;
    private String guideStars;

    public String getGuideStars() {
        return guideStars;
    }

    public void setGuideStars(String guideStars) {
        this.guideStars = guideStars;
    }

    public String getGuideHead() {
        return guideHead;
    }

    public void setGuideHead(String guideHead) {
        this.guideHead = guideHead;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getGuideHourPrice() {
        return guideHourPrice;
    }

    public void setGuideHourPrice(String guideHourPrice) {
        this.guideHourPrice = guideHourPrice;
    }

    public String getGuideTimes() {
        return guideTimes;
    }

    public void setGuideTimes(String guideTimes) {
        this.guideTimes = guideTimes;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
