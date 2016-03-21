package com.brunocasado.nubankcase.webservice;

import android.util.Log;

import com.brunocasado.nubankcase.model.Notice;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


/**
 * Created by Bruno Casado on 07/03/2016.
 */
public class NoticeREST {
    private static final String URL_WS = "https://nu-mobile-hiring.herokuapp.com/notice";

    public static Notice getNotice() throws Exception{
        HttpURLConnection resposta = new WebServiceGet().doInBackground(URL_WS);
        Log.e("NoticeREST", "Code: "+resposta.getResponseCode());
        if (resposta.getResponseCode() == 200){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(resposta.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            Log.e("NoticeREST", response.toString());

            Gson gson = new Gson();
            Notice notice = gson.fromJson(response.toString(), Notice.class);
            return notice;
        } else {
            return null;
        }
    }
}
