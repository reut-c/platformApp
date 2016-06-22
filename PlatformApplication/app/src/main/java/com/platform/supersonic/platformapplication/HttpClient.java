package com.platform.supersonic.platformapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient extends AsyncTask<Void, Void, String> {

    private Request request;
    private User user;
    private ProgressDialog pd;

    public HttpClient(Request request, User user) {
        super();

        this.request = request;
        this.user = user;
    }

    public HttpClient(Request request, User user, Context context) {
        super();

        this.request = request;
        this.user = user;
        //Create a Progressdialog
        this.pd = new ProgressDialog(context);
    }

    @Override
    protected String doInBackground(Void... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String bufferAsString = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL(this.request.getUrl());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(this.request.getMethod());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            if (this.request.getMethod() == "POST"){
                JSONObject msg = new JSONObject();
                msg.put("username",user.getUser());
                msg.put("password", user.getPassword());

                OutputStream os = urlConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(msg.toString());
                osw.flush();
                osw.close();
            }
            if (this.request.getAuth() != null){
                urlConnection.setRequestProperty("Authorization", "Advanced " + this.request.getAuth());
            }
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            bufferAsString = buffer.toString();
            return bufferAsString;
        } catch (IOException e) {

            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {

                }
            }
        }
    }

    @Override
    protected void onPreExecute()
    {
        //Set the Progressdialog
        this.pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.pd.setMessage("Loading....");
    }

    @Override
    protected void onPostExecute(String result) {
        //Dismiss Progressdialog
        this.pd.dismiss();
    }
}