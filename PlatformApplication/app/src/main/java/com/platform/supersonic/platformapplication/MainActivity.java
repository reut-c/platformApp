package com.platform.supersonic.platformapplication;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MainActivity extends BasicActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUsername,etPassword;

    public static final String USERDATA = "UserDataFile";
    public static final String TOKEN = "token";
    public static final String EXPIRATION_DATE = "expirationDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);
    }

    public void login(String username,String password){
        try {
            User user = new User(username, password);
            Request request = new Request("https://platform.supersonic.com/partners/auth/login", null, "POST");
            HttpClient http = new HttpClient(request, user);
            AsyncTask<Void, Void, String> asyncTask = http.execute();
            String st = asyncTask.get();
            JSONObject response = new JSONObject(st);
            String token = response.getString("token");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLogin:
                String usernameText = etUsername.getText().toString();
                String passwordText = etPassword.getText().toString();
                this.login(usernameText,passwordText);
               /* Intent i = new Intent(getApplicationContext(), SecondScreen.class);
                StartActivity(i);*/
                break;
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
