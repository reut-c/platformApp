package com.platform.supersonic.platformapplication;

public class KPI {

    public enum TREND {UP,DOWN,SAME};

    public TREND trend;
    public String title;
    public String value;

    public KPI(){
        super();
    }

    public KPI(TREND tr, String title,String value) {
        super();
        this.trend = tr;
        this.title = title;
        this.value = value;
    }
}