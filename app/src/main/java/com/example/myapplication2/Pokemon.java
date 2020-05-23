package com.example.myapplication2;

import java.io.Serializable;

public class Pokemon implements Serializable {

    private String name;
    private String url;
    private String img;

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
