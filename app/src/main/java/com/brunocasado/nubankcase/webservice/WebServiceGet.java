package com.brunocasado.nubankcase.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bruno Casado on 21/03/2016.
 */
public class WebServiceGet extends AsyncTask<String, String, HttpURLConnection> {

    @Override
    protected HttpURLConnection doInBackground(String... params) {
        URL reqURL = null;
        HttpURLConnection request = null;
        try {
            reqURL = new URL(params[0]);
            request = (HttpURLConnection) (reqURL.openConnection());
            request.setRequestMethod("GET");
            request.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return request;
    }

}
