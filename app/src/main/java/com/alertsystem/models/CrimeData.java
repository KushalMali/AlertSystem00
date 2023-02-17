package com.alertsystem.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrimeData {

    @SerializedName("ctype")
    @Expose
    private String ctype;
    @SerializedName("ccount")
    @Expose
    private String ccount;

    @SerializedName("des")
    @Expose
    private String des;

    public String getdes() {
        return des;
    }

    public void setdes(String des) {
        this.des = des;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getCcount() {
        return ccount;
    }

    public void setCcount(String ccount) {
        this.ccount = ccount;
    }
}