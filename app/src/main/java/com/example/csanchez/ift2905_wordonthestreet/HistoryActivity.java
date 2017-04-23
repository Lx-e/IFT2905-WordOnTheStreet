package com.example.csanchez.ift2905_wordonthestreet;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import android.widget.PopupMenu;


public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{
    ListView list;
    MyAdapter adapter;
    private String[] hist;

    Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        SharedPreferences prefs = getSharedPreferences("history", MODE_PRIVATE);
        int size = prefs.getInt("history_size", 0);
        String restored = "";
        String link = "";
        hist = new String[size+1];
        for(int i = 0; i<size+1; i++){
            link = "H_title"+((Integer)i).toString();
            restored = prefs.getString(link, null);
            hist[i] = restored;
        }

        reset = (Button)findViewById(R.id.button7);
        reset.setOnClickListener(this);
        list = (ListView)findViewById(R.id.listviewb2);
        adapter = new MyAdapter();
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                SharedPreferences prefs = getSharedPreferences("history", MODE_PRIVATE);


                String link = prefs.getString("H_url"+((Integer)position).toString(), null);

                String newsDate = prefs.getString("H_date"+((Integer)position).toString(), null);

                String desc = prefs.getString("H_title"+((Integer)position).toString(), null);


                Intent intent = new Intent(getApplicationContext(), SingleNewsExpand.class);

                intent.putExtra("date", newsDate);
                intent.putExtra("desc", desc);
                intent.putExtra("link", link);
                intent.putExtra("caller", "HistoryActivity");
                startActivity(intent);
            }
        });
    }

    public class MyAdapter extends BaseAdapter{
        LayoutInflater inflater;
        public MyAdapter(){
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            SharedPreferences prefs = getSharedPreferences("history", MODE_PRIVATE);
            int size = prefs.getInt("history_size", 0);
            return size;
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

            if(v==null)
                v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            TextView tv = (TextView)v.findViewById(android.R.id.text1);
            tv.setText(hist[position]);

            return v;
        }
    }
    public void onClick(View v){
        this.getSharedPreferences("history", 0).edit().clear().commit();
        startActivity(new Intent(this, HistoryActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
