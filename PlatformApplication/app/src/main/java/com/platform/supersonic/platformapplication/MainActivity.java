package com.platform.supersonic.platformapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUsername,etPassword;

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
            Toast toast = Toast.makeText(this.getBaseContext(),st,Toast.LENGTH_SHORT);
            toast.show();
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
                String passwordText = etUsername.getText().toString();
                this.login(usernameText,passwordText);
               /* Intent i = new Intent(getApplicationContext(), SecondScreen.class);
                StartActivity(i);*/
                break;
        }
    }
}
