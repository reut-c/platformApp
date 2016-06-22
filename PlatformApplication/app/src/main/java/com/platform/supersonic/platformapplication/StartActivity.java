package com.platform.supersonic.platformapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StartActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Thread(new Runnable() {
            public void run() {
                this.isTokenValid();
            }
        }).start();

    }

    private void redirectToLoginPage(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    private void isTokenValid(){
        SharedPreferences settings = getSharedPreferences(USERDATA, 0);
        String token = settings.getString(TOKEN, null);
        if (token == null){ // if the user doesn't have token, move to the login page
            redirectToLoginPage();
        } else { // user has token
            String expirationDate = settings.getString(EXPIRATION_DATE,null);
            if (expirationDate != null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date expireDate = dateFormat.parse(expirationDate);
                    if(expireDate.after(new Date())){
                        Intent intent = new Intent(this,StatisticsActivity.class);
                        startActivity(intent);
                    } else{
                        redirectToLoginPage();
                    }
                }
                catch (Exception e){
                    redirectToLoginPage();
                }
            }
            else {
                redirectToLoginPage();
            }
        }
    }
}
