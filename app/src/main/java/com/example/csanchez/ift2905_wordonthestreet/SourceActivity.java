package com.example.csanchez.ift2905_wordonthestreet;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.csanchez.ift2905_wordonthestreet.R.id.parent;


public class SourceActivity extends AppCompatActivity {
    private String categories[] = {};
    ListView list;
    List<String> customSources = new ArrayList();
    boolean checked[];
    ContextWrapper c = new ContextWrapper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

        //categories = getIntent().getExtras().getStringArray("categories");

        list = (ListView) findViewById(R.id.listView_news);

        SourceActivity.SourceFetcher sources = new SourceActivity.SourceFetcher();
        sources.execute();
    }

    public class SourceFetcher extends AsyncTask<Object, Object, Source[]> {

        @Override
        protected Source[] doInBackground(Object... params) {

            Source[] sources = new Source[0];

            try {
                if(categories.length == 0){
                    sources = NewsAPI.getSources();
                }
                else{
                    sources = NewsAPI.getSources(categories);
                }

                SharedPreferences prefs = getSharedPreferences("SavedData", MODE_PRIVATE);
                String sourcesStr = prefs.getString("CustomSources", "Nothing");//"No name defined" is the default value.
                Log.v("TAG", "RETRIEVED: "+sourcesStr);
                customSources = new ArrayList<String>();
                if(!sourcesStr.equals("Nothing")) {
                    String[] sourcesArr = sourcesStr.split(",");
                    for (int i = 0; i < sourcesArr.length; i++) {
                        Log.v("TAG", "Parsed: " + sourcesArr[i]);
                        customSources.add(sourcesArr[i]);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            checked = new boolean[sources.length];
            for(boolean cb : checked){
                cb=false;
            }
            return sources;
        }

        @Override
        protected void onPostExecute(final Source[] sources) {
            Button saveBtn = (Button) findViewById(R.id.save_button);
            Button clearBtn = (Button) findViewById(R.id.clear_custom);

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = getSharedPreferences("SavedData", MODE_PRIVATE).edit();
                    editor.remove("CustomSources");
                    editor.commit();
                    clearCheckboxes(list);
                    customSources =  new ArrayList<String>();
                    Log.v("TAG", "REMOVING: "+checkBoxesToSourceName(sources));
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = getSharedPreferences("SavedData", MODE_PRIVATE).edit();
                    editor.remove("CustomSources");
                    editor.putString("CustomSources", checkBoxesToSourceName(sources));
                    editor.commit();
                    Log.v("TAG", "SAVING: "+checkBoxesToSourceName(sources));
                }
            });

            for(String source : customSources) {
                int i = getSourceIndex(sources, source);
                if(i > -1){
                    Log.v("TAG", "Found "+source+" at "+i);
                    checked[i] = true;
                }
            }
            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return sources.length;
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

                    if(convertView == null)
                        convertView = getLayoutInflater().inflate(R.layout.single_source, parent, false);

                    TextView title = (TextView) convertView.findViewById(R.id.name);
                    TextView category = (TextView) convertView.findViewById(R.id.category);
                    CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);

                    title.setText(sources[position].name);
                    category.setText(sources[position].category);
                    cb.setChecked(checked[position]);
                    return convertView;
                }


            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
                    if (cb.isChecked()) {
                        cb.setChecked(false);
                        customSources.remove(sources[position].name);
                        checked[position] = false;
                        for(String source : customSources) {
                            Log.v("TAG",source);
                        }
                    }else{
                        cb.setChecked(true);
                        customSources.add(sources[position].name);
                        checked[position] = true;
                        Log.v("TAG",(new Gson().toJson(customSources)));
                        for(String source : customSources) {
                            Log.v("TAG","In customSources: "+source);
                        }
                    }
                }
            });

        }
    }

    public String checkBoxesToSourceName(Source[] arr){
        String str = "";
        for(int i=0; i<checked.length; i++) {
            if(checked[i] == true){
                str += arr[i].id+",";
            }
        }

        return str.length() > 0 ? str.substring(0, str.length()-1): "Nothing";
    }
    public int getSourceIndex(Source[] src, String id){
        for(int i=0; i<src.length; i++){
            if(src[i].id.equals(id)){
                return i;
            }
        }
        return -1;
    }

    //Inspiré de http://stackoverflow.com/questions/14509552/uncheck-all-checbox-in-listview-in-android
    private void clearCheckboxes(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v instanceof CheckBox) {
                ((CheckBox) v).setChecked(false);
            } else if (v instanceof ViewGroup) {
                clearCheckboxes((ViewGroup) v);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
