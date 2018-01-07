package com.android.group.synthesesapp.Tool;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by geoffrey on 09/11/17.
 */

public class BackgroundTask extends AsyncTask<String, Void, String> {

    protected void onPreExecute() {
    }

    protected String doInBackground(String... arg0) {
        JSONArray jsonObj = new JSONArray();

        try {

            URL url = new URL(arg0[0]);
            Log.d("url", arg0[0]);

            HttpURLConnection httpURLConnection;
            httpURLConnection = (HttpURLConnection) url.openConnection();
            StringBuilder builder = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                builder.append(output);
                builder.append("\n");
            }
            jsonObj = new JSONArray(builder.toString());
            Log.e("json", jsonObj.toString());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }
}