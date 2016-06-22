package com.platform.supersonic.platformapplication;

import android.os.Bundle;
import android.widget.AdapterView;

public class MonetizeStats extends StatsBase implements AdapterView.OnItemSelectedListener{

    public MonetizeStats() {
        // Required empty public constructor
        this.type = "Monetize";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}