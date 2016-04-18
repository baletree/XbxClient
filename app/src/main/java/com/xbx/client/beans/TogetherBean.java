package com.xbx.client.beans;

import java.io.Serializable;

/**
 * Created by EricYuan on 2016/4/18.
 */
public class TogetherBean implements Serializable {

    private String headImg;
    private String name;
    private float score;
    private String price;
    private String num;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
