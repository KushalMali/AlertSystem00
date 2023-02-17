package com.alertsystem.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockData {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("uid")
    @Expose
    private String uid = "";
    @SerializedName("carea")
    @Expose
    private String carea = "";
    @SerializedName("ctype")
    @Expose
    private String ctype = "";
    @SerializedName("ccount")
    @Expose
    private String ccount = "";
    @SerializedName("status")
    @Expose
    private String status = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCarea() {
        return carea;
    }

    public void setCarea(String carea) {
        this.carea = carea;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
