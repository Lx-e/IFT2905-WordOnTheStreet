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
import android.view.LayoutInflater;
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


public class BookmarkActivity extends AppCompatActivity{
    ListView list;
    MyAdapter adapter;
    int n = 30;
    static int i= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);

        list = (ListView)findViewById(R.id.listviewb);
        adapter = new MyAdapter();
        list.setAdapter(adapter);
    }

    public class MyAdapter extends BaseAdapter{
        LayoutInflater inflater;
        public MyAdapter(){
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return n;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            String url="nothing";
            SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
            /*for(i = 0;i<30;i++){
                String restoreURL = "url"+i;
                if(prefs.getString(restoreURL,null)==null){
                    //ya pu de bookmarks
                }
                else
                    url = prefs.getString(restoreURL, null);
            }*/
            String restoreURL = "url"+((Integer)(prefs.getInt("bookmark_size",0)-1)).toString();
            String restoredText = prefs.getString(restoreURL, null);

       //     if (restoredText != null) {
       //         url = prefs.getString("url", "No url defined");

         //   }
            if(v==null)
                v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            TextView tv = (TextView)v.findViewById(android.R.id.text1);
            tv.setText(restoredText);

            return v;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
