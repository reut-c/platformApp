package com.platform.supersonic.platformapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    public static final String USERDATA = "UserDataFile";
    public static final String TOKEN = "token";
    public static final String EXPIRATION_DATE = "expirationDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }
}
