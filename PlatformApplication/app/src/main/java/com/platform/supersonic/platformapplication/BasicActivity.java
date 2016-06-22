package com.platform.supersonic.platformapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicActivity extends AppCompatActivity {

    public static final String USERDATA = "UserDataFile";
    public static final String TOKEN = "token";
    public static final String EXPIRATION_DATE = "expirationDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }

    protected void saveToken(String token,Date expirationDate){
        SharedPreferences settings = getSharedPreferences(USERDATA, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TOKEN, token);
        editor.putString(EXPIRATION_DATE,dateFormat.format(expirationDate));
        editor.apply();
    }

    protected String getToken(){
        SharedPreferences settings = getSharedPreferences(USERDATA, 0);
        String token = settings.getString(TOKEN, null);
        return token;
    }
    protected void redirectToLoginPage(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    protected void isTokenValid(){
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
