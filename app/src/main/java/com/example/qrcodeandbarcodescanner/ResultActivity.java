package com.example.qrcodeandbarcodescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {
    TextView content,formate;
    ImageView imageView,copy,share,home,wifi,search;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        content = (TextView) findViewById(R.id.txtcontent);
        formate = (TextView) findViewById(R.id.txtformate);
        imageView=(ImageView)findViewById(R.id.imgcode);
        copy=(ImageView)findViewById(R.id.copy);
        home=(ImageView)findViewById(R.id.imghome);
        share=(ImageView)findViewById(R.id.share);
        wifi=(ImageView)findViewById(R.id.wifi);
        search=(ImageView)findViewById(R.id.search);
        Intent i= getIntent();
        final String contentt=i.getStringExtra("content");
        String formatt=i.getStringExtra("format");
        String str_bit=i.getStringExtra("bitmap");
        Bitmap bitmap=StringToBitMap(str_bit);
        content.setText(contentt);
        formate.setText(formatt);
        imageView.setImageBitmap(bitmap);
        try {
            if (containsLink(contentt)) {

                search.setVisibility(View.VISIBLE);
                copy.setVisibility(View.GONE);
                wifi.setVisibility(View.GONE);
            }else if(containWifiPassword(contentt)!=null && contentt.contains("WIFI")){
                search.setVisibility(View.GONE);
                copy.setVisibility(View.GONE);
                wifi.setVisibility(View.VISIBLE);
            }else{
                search.setVisibility(View.GONE);
                copy.setVisibility(View.VISIBLE);
                wifi.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            search.setVisibility(View.GONE);
            copy.setVisibility(View.VISIBLE);
            wifi.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Execution Error !",Toast.LENGTH_SHORT).show();
        }
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", content.getText().toString());
                clipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!content.getText().toString().equals("")) {
                    ShareText(content.getText().toString());
                }

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ResultActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentt));
                startActivity(browserIntent);

            }
        });
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToAP(containWifiSSID(contentt),containWifiPassword(contentt),ResultActivity.this);

            }
        });
    }
    public static String containWifiSSID(String input){
        String newtext;

        newtext=input.substring(input.lastIndexOf(";S:") + 3);
        newtext=newtext.replace(";", "");
        return newtext;
    }
    public void connectToAP(String ssid, String passkey, Context context) {
        WifiConfiguration wifiConfig = new WifiConfiguration();

        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", passkey);


        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }
    public static String containWifiPassword(String input){
        String newtext;

        newtext=input.substring(input.indexOf(";P:") + 3, input.indexOf(";S:"));
        newtext=newtext.replace("\"", "");

        return newtext;
    }
    public static boolean containsLink(String input) {
        boolean result = false;

        String[] parts = input.split("\\s+");

        for (String item : parts) {
            if (android.util.Patterns.WEB_URL.matcher(item).matches()) {
                result = true;
                break;
            }
        }

        return result;
    }
    public void ShareText(String str){
        String shareBody = str;
        final String appPackageName = getPackageName();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+appPackageName+"\n"+shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
