package com.brunocasado.nubankcase.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Bruno Casado on 21/03/2016.
 */
public class WebServicePOST extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String charset = "UTF-8";
        HttpURLConnection conn;
        DataOutputStream wr;
        StringBuilder result = null;
        URL urlObj;

        try {
            urlObj = new URL(params[0]);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", charset);

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.connect();

            wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(params[1]);
            wr.flush();
            wr.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                Log.e("While", line);
                result.append(line);
            }

            Log.e("JSON Parser", "result: " + result.toString());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
