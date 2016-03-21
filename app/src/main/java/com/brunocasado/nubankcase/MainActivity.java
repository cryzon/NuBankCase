package com.brunocasado.nubankcase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.brunocasado.nubankcase.model.Notice;
import com.brunocasado.nubankcase.webservice.NoticeREST;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Notice notice = null;

        TextView textTitle = (TextView) findViewById(R.id.textTitle);
        TextView textDesc = (TextView) findViewById(R.id.textDescription);
        Button btnContinuar = (Button) findViewById(R.id.btnContinuar);
        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        String strPrimaryAction = null;
        String strSecondaryAction = null;
        String URL = null;

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Recebendo notificação...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        try {
            notice = NoticeREST.getNotice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pDialog.dismiss();

        if (notice != null){

            try{
                textTitle.setText(notice.getTitle());
                textDesc.setText(Html.fromHtml(notice.getDescription()));
                strPrimaryAction = notice.getPrimary_action().get("title");
                strSecondaryAction = notice.getSecondary_action().get("title");
                URL = notice.getLinks().get("chargeback").get("href");
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            textTitle.setTextSize(26);

            final String finalURL = URL;

            btnContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    continuaChargeback(finalURL);
                }
            });
            btnContinuar.setText(strPrimaryAction);

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    System.exit(0);
                }
            });
            btnCancelar.setText(strSecondaryAction);
        } else{
            Toast toast = new Toast(this);
            toast.setText("Não foi possível conectar");
            toast.setDuration(Toast.LENGTH_LONG);
            finish();
        }

    }

    public void continuaChargeback(String url){
        Intent intent = new Intent(this, ChargebackActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
