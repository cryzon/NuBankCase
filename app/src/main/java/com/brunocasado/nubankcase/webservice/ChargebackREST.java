package com.brunocasado.nubankcase.webservice;

import android.util.Log;

import com.brunocasado.nubankcase.model.Chargeback;
import com.brunocasado.nubankcase.model.Notice;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by Bruno Casado on 15/03/2016.
 */
public class ChargebackREST {

    public static Chargeback getChargeback(String URL) throws Exception{

        HttpURLConnection resposta = new WebServiceGet().doInBackground(URL);
        Log.e("ChargebackREST: Get", "Code: " + resposta.getResponseCode());
        if (resposta.getResponseCode() == 200){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(resposta.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            Log.e("ChargebackREST", response.toString());

            Gson gson = new Gson();
            Chargeback chargeback = gson.fromJson(response.toString(), Chargeback.class);
            return chargeback;
        } else {
            return null;
        }
    }

    public static String postChargeback(String http, String json) throws Exception{
        String result = new WebServicePOST().doInBackground(http, json);

        return result;
    }
}
