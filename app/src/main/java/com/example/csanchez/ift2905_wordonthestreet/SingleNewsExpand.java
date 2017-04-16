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
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
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
    boolean book=false;
    Button inapp;
    Button brow;
    Button share;
    Button toggle;
    WebView webview;
    String link;
    String date;
    String desc;
    static int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlenewsexpand);

        textviewDate = (TextView)findViewById(R.id.textDate);
        textviewDesc = (TextView)findViewById(R.id.textDesc);

        inapp = (Button)findViewById(R.id.button3);
        brow = (Button)findViewById(R.id.button2);
        share =(Button)findViewById(R.id.button4);
        toggle = (Button)findViewById(R.id.button5);

        inapp.setOnClickListener(this);
        brow.setOnClickListener(this);
        share.setOnClickListener(this);
        toggle.setOnClickListener(this);




        webview = (WebView)findViewById(R.id.webv);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            date = (String) b.get("date");
            textviewDate.setText(date);
            desc = (String) b.get("desc");
            textviewDesc.setText(desc);
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
            case R.id.button4:
                shareIt();
                break;
            case R.id.button5:
                if(book) {
                    //mettre bookmark
                    //Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_SHORT).show();
                    toggle.setBackgroundResource(R.drawable.ic_book_black_48dp);
                    SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
                    SharedPreferences.Editor e = getSharedPreferences("bookmarks",MODE_PRIVATE).edit();
                    //Toast.makeText(getApplicationContext(), link, Toast.LENGTH_SHORT).show();
                    //if(prefs.getInt("bookmark_size", 0)!=0){}
                    i=prefs.getInt("bookmark_size", 0);
                    e.putInt("bookmark_size",i+1);

                    String listCount = "url"+i;
                    Toast.makeText(getApplicationContext(), listCount, Toast.LENGTH_SHORT).show();
                    e.putString(listCount, link);
                    e.commit();

                    book = !book;
                }
                else{
                    //retirer bookmark
                    //Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_SHORT).show();
                    toggle.setBackgroundResource(R.drawable.ic_bookmark_border_white_48dp);
                    book = !book;
                }

                break;
        }
    }
    private void shareIt() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, desc);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
