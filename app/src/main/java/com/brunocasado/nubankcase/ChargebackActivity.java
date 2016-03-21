package com.brunocasado.nubankcase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.brunocasado.nubankcase.model.Chargeback;
import com.brunocasado.nubankcase.webservice.BlockCardREST;
import com.brunocasado.nubankcase.webservice.ChargebackREST;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChargebackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargeback);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        String URL = intent.getStringExtra("URL");
        Log.e("ChargebackActivity", URL);

        Chargeback chargeback = null;

        final String[] http = new String[1];

        TextView textTitle = (TextView) findViewById(R.id.textViewChargebackTitle);
        final TextView textDesc = (TextView) findViewById(R.id.textViewChargebackDescription);
        final TextView textReconhece = (TextView) findViewById(R.id.textViewReconhece);
        final TextView textCartaoEmMaos = (TextView) findViewById(R.id.textViewCartaoEmMaos);
        TextView textCommentHint = (TextView) findViewById(R.id.textViewCommentHint);

        final ImageView imageLock = (ImageView) findViewById(R.id.imageViewChargebackLock);

        Button btnContestar = (Button) findViewById(R.id.buttonContestar);
        Button btnCancelar = (Button) findViewById(R.id.buttonCancelar);

        btnContestar.setTextColor(getResources().getColor(R.color.disabled_gray));
        btnContestar.setBackgroundColor(getResources().getColor(R.color.background));

        btnCancelar.setTextColor(getResources().getColor(R.color.close_gray));
        btnCancelar.setBackgroundColor(getResources().getColor(R.color.background));

        final Switch switchReconhece = (Switch) findViewById(R.id.switchReconehce);
        final Switch switchCartaoEmMaos = (Switch) findViewById(R.id.switchCartaoEmMaos);

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Recebendo notificação...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        try{
            chargeback = ChargebackREST.getChargeback(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pDialog.dismiss();

        if (chargeback != null){
            try{
                textTitle.setText(chargeback.getTitle());
                textTitle.setTextColor(getResources().getColor(R.color.blackTexts));
                textTitle.setAllCaps(true);
                textTitle.setTypeface(null, Typeface.BOLD);
                textCommentHint.setText(Html.fromHtml(chargeback.getComment_hint()));
                textReconhece.setText(Html.fromHtml(chargeback.getReason_details().get(0).valueAt(1).toString()));
                textCartaoEmMaos.setText(Html.fromHtml(chargeback.getReason_details().get(1).valueAt(1).toString()));
                if (chargeback.isAutoblock()){
                    Log.e("Autoblock", "Entrou Autoblock");
                    imageLock.setImageResource(R.drawable.chargeback_lock);
                    switchReconhece.setChecked(true);
                    switchCartaoEmMaos.setChecked(true);
                    textReconhece.setTextColor(getResources().getColor(R.color.green));
                    textCartaoEmMaos.setTextColor(getResources().getColor(R.color.green));
                    textDesc.setText("Bloqueamos preventivamente o seu cartão");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        switchReconhece.getThumbDrawable().setColorFilter(getResources().getColor(R.color.disabled_gray), PorterDuff.Mode.MULTIPLY);
                        switchReconhece.getTrackDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
                        switchCartaoEmMaos.getThumbDrawable().setColorFilter(getResources().getColor(R.color.disabled_gray), PorterDuff.Mode.MULTIPLY);
                        switchCartaoEmMaos.getTrackDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
                    }
                    try {
                        BlockCardREST.postBlockCard(chargeback.getLinks().get("block_card").get("href"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else{
                    imageLock.setImageResource(R.drawable.chargeback_unlock);
                    textReconhece.setTextColor(getResources().getColor(R.color.blackTexts));
                    textCartaoEmMaos.setTextColor(getResources().getColor(R.color.blackTexts));
                    switchReconhece.setChecked(false);
                    switchCartaoEmMaos.setChecked(false);
                    textDesc.setText("Cartão desbloqueado. recomendamos mantê-lo bloqueado.");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        switchReconhece.getThumbDrawable().setColorFilter(getResources().getColor(R.color.disabled_gray), PorterDuff.Mode.MULTIPLY);
                        switchReconhece.getTrackDrawable().setColorFilter(getResources().getColor(R.color.close_gray), PorterDuff.Mode.MULTIPLY);
                        switchCartaoEmMaos.getThumbDrawable().setColorFilter(getResources().getColor(R.color.disabled_gray), PorterDuff.Mode.MULTIPLY);
                        switchCartaoEmMaos.getTrackDrawable().setColorFilter(getResources().getColor(R.color.close_gray), PorterDuff.Mode.MULTIPLY);
                    }
                }
                textDesc.setTextColor(getResources().getColor(R.color.red));
                http[0] = chargeback.getLinks().get("self").get("href");
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            switchReconhece.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        textReconhece.setTextColor(getResources().getColor(R.color.green));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            switchReconhece.getThumbDrawable().setColorFilter(getResources().getColor(R.color.disabled_gray), PorterDuff.Mode.MULTIPLY);
                            switchReconhece.getTrackDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
                        }
                    } else{
                        textReconhece.setTextColor(getResources().getColor(R.color.blackTexts));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            switchReconhece.getThumbDrawable().setColorFilter(getResources().getColor(R.color.disabled_gray), PorterDuff.Mode.MULTIPLY);
                            switchReconhece.getTrackDrawable().setColorFilter(getResources().getColor(R.color.close_gray), PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }
            });

            switchCartaoEmMaos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        textCartaoEmMaos.setTextColor(getResources().getColor(R.color.green));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            switchCartaoEmMaos.getThumbDrawable().setColorFilter(getResources().getColor(R.color.disabled_gray), PorterDuff.Mode.MULTIPLY);
                            switchCartaoEmMaos.getTrackDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
                        }
                    } else {
                        textCartaoEmMaos.setTextColor(getResources().getColor(R.color.blackTexts));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            switchCartaoEmMaos.getThumbDrawable().setColorFilter(getResources().getColor(R.color.disabled_gray), PorterDuff.Mode.MULTIPLY);
                            switchCartaoEmMaos.getTrackDrawable().setColorFilter(getResources().getColor(R.color.close_gray), PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final String[] result = new String[1];
            btnContestar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ArrayMap> reason_details = new ArrayList<ArrayMap>();
                    ArrayMap<String, String> detalhes = new ArrayMap<String, String>();
                    detalhes.put("id", "merchant_regonized");
                    detalhes.put("response", String.valueOf(switchReconhece.isChecked()));
                    reason_details.add(0, detalhes);
                    detalhes = new ArrayMap<String, String>();
                    detalhes.put("id", "card_in_possession");
                    detalhes.put("response", String.valueOf(switchCartaoEmMaos.isChecked()));
                    reason_details.add(1, detalhes);
                    String comment = "Estava viajando e estava em outra cidade no dia desta compra";

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("comment", comment);
                        jsonObject.put("reason_details", reason_details);

                        result[0] = ChargebackREST.postChargeback(http[0], jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (result[0].equals("{\"status\":\"Ok\"}")){
                        Log.e("EntrouResult", result[0]);
                        finish();
                        Intent intent = new Intent(ChargebackActivity.this, NotificationActivity.class);

                        startActivity(intent);
                    } else{
                        Toast toast = new Toast(ChargebackActivity.this);
                        toast.makeText(ChargebackActivity.this, "Não foi possível conectar", Toast.LENGTH_LONG);
                        finish();
                    }
                }
            });
        } else{
            Toast toast = new Toast(this);
            toast.setText("Não foi possível conectar");
            toast.setDuration(Toast.LENGTH_LONG);
            finish();
        }


    }

}
