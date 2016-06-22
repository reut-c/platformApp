package com.platform.supersonic.platformapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUsername,etPassword;

    public static final String USERDATA = "UserDataFile";
    public static final String TOKEN = "token";
    public static final String EXPIRATION_DATE = "expirationDate";

    public MainActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLogin:
                // todo login
                break;
        }
    }

    private boolean isTokenValid(){
        SharedPreferences settings = getSharedPreferences(USERDATA, 0);
        String token = settings.getString(TOKEN, null);
        if (token == null){
            return false;
        } else {
            String expirationDate = settings.getString(EXPIRATION_DATE,null);
            if (expirationDate != null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
                try {
                    Date expireDate = dateFormat.parse(expirationDate);
                    return expireDate.after(new Date());
                }
                catch (Exception e){
                    return false;
                }
            }
            else {
                return false;
            }
        }
    }

    private void saveToken(String token,Date expirationDate){
        SharedPreferences settings = getSharedPreferences(USERDATA, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TOKEN, token);
        editor.putString(EXPIRATION_DATE,dateFormat.format(expirationDate));
        editor.apply();
    }
}
