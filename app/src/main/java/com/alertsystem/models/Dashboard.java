package com.alertsystem.models;

public class Dashboard {
    private String name = null;
    private int icon = 0;

    public Dashboard() {
    }

    public Dashboard(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}