package com.alertsystem.models;

public class House {

    private String name = null;
    private String mobile = null;
    private int image = 0;

    public House(String name, String mobile, int image) {
        this.name = name;
        this.mobile = mobile;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}