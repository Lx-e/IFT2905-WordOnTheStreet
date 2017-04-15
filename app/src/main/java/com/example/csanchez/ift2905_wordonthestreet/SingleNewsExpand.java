package com.example.csanchez.ift2905_wordonthestreet;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class SingleNewsExpand extends AppCompatActivity implements View.OnClickListener{

    private TextView textviewDate;
    private TextView textviewDesc;
    Button inapp;
    Button brow;
    WebView webview;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlenewsexpand);

        textviewDate = (TextView)findViewById(R.id.textDate);
        textviewDesc = (TextView)findViewById(R.id.textDesc);

        inapp = (Button)findViewById(R.id.button3);
        brow = (Button)findViewById(R.id.button2);

        inapp.setOnClickListener(this);
        brow.setOnClickListener(this);
        webview = (WebView)findViewById(R.id.webv);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            String k = (String) b.get("date");
            textviewDate.setText(k);
            String l = (String) b.get("desc");
            textviewDesc.setText(l);
            link = (String) b.get("link");
        }
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button3:
                webview.loadUrl(link);
                break;
            case R.id.button2:
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
