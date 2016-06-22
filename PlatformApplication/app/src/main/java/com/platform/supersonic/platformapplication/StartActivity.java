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

        isTokenValid();
    }

    private void isTokenValid(){
        SharedPreferences settings = getSharedPreferences(USERDATA, 0);
        String token = settings.getString(TOKEN, null);
        if (token == null | token.length() == 0){ // if the user doesn't have token, move to the login page
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else {
            String expirationDate = settings.getString(EXPIRATION_DATE,null);
            if (expirationDate != null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date expireDate = dateFormat.parse(expirationDate);
                    //return expireDate.after(new Date());
                }
                catch (Exception e){
                    //return false;
                }
            }
            else {
                //return false;
            }
        }
    }
}
