package com.example.myapplication2;

import java.io.Serializable;

public class Pokemon implements Serializable {
    private String name;
    private String url;


    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
