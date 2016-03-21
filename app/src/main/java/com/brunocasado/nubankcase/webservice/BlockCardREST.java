package com.brunocasado.nubankcase.webservice;

/**
 * Created by Bruno Casado on 21/03/2016.
 */
public class BlockCardREST {

    public static void postBlockCard(String http) throws Exception{
        new WebServicePOST().doInBackground(http, "");
    }
}
