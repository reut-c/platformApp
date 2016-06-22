package com.platform.supersonic.platformapplication;

import android.os.Bundle;

public class PromoteStats extends StatsBase{

    public PromoteStats() {
        // Required empty public constructor
        this.type = "Promote";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.token = getArguments().getString(BasicActivity.TOKEN);

    }

}