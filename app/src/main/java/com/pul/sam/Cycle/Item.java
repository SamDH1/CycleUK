package com.pul.sam.Cycle;

import android.media.Image;


import java.io.Serializable;


public class Item implements Serializable {

    private Image img;
    private String trail;
    private String temp;
    private String weather;
    private String conditions;

    public Item(Image img, String trail, String temp, String weather, String conditions) {
        this.img = img;
        this.trail = trail;
        this.temp = temp;
        this.weather = weather;
        this.conditions = conditions;
    }

    public Image getImg() {
        return img;
    }

    public String getTrail() {
        return trail;
    }

    public String getTemp() {
        return temp;
    }

    public String getWeather() {



        return weather;
    }





    public String getConditions() {
        return conditions;
    }
}

