package com.example.csanchez.ift2905_wordonthestreet;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SourceActivity extends AppCompatActivity implements View.OnClickListener{
    ListView list;

    List<Source> allSources = new ArrayList<Source>();
    List<Source> favoriteSources = new ArrayList<Source>();

    Map<String, Source> sourcesByName = new HashMap<String, Source>();
    Map<View, Source> viewsToSources = new HashMap<View, Source>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

        //categories = getIntent().getExtras().getStringArray("categories");

        list = (ListView) findViewById(R.id.listView_news);

        SourceActivity.SourceFetcher sourcesFetcher = new SourceActivity.SourceFetcher();
        sourcesFetcher.execute();
        try {
            Source[] sourceObjects = sourcesFetcher.get();
            int g = 0;

        }
        catch (Throwable t) {
            int h = 9;
        }
    }

    protected void loadFavoriteSources() {

        favoriteSources = new ArrayList<Source>();

        String sourcesStr = getSharedPreferences("SavedData", MODE_PRIVATE).getString("CustomSources", "Nothing");//"No name defined" is the default value.
        Log.v("TAG", "RETRIEVED: "+sourcesStr);

        if(sourcesStr == null || sourcesStr.equals("Nothing")) return;

        String[] sourceNames = sourcesStr.split(",");

        for (String sourceName: sourceNames) {
            sourceName = sourceName.trim();
            Log.v("TAG", "Parsed: " + sourceName);
            if (sourceName.length() > 0 && sourcesByName.containsKey(sourceName) && !favoriteSources.contains(sourceName))
                favoriteSources.add(sourcesByName.get(sourceName));
        }

    }

    protected void saveFavoriteSources() {

        StringBuffer sourcesBuffer = new StringBuffer();
        if (favoriteSources.size() == 0)
            sourcesBuffer.append("Nothing");
        else
            for (Source source: favoriteSources) { sourcesBuffer.append(source.name.trim()).append(","); }

        SharedPreferences.Editor editor = getSharedPreferences("SavedData", MODE_PRIVATE).edit();
        editor.remove("CustomSources");
        editor.putString("CustomSources", sourcesBuffer.toString());
        editor.commit();
        Log.v("TAG", "SAVING: "+ sourcesBuffer.toString());
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

    @Override
    public void onClick(View v) {

        Source source = viewsToSources.get(v);
        if (source == null) return;

        if (favoriteSources.contains(source)) {
            favoriteSources.remove(source);
            Log.v("TAG", "Favorite removed: " + new Gson().toJson(source));
        }
        else {
            favoriteSources.add(source);
            Log.v("TAG", "Favorite added: " + new Gson().toJson(source));
        }

        CheckBox cb = (v instanceof CheckBox) ? null : (CheckBox)v.findViewById(R.id.checkbox);
        if (cb != null) cb.setChecked(!cb.isChecked());

    }

    public class SourceFetcher extends AsyncTask<Object, Object, Source[]> {

        @Override
        protected Source[] doInBackground(Object... params) {

            Source[] sources = new Source[0];

            try {
                sources = NewsAPI.getSources();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            for (Source source: sources) {
                allSources.add(source);
                sourcesByName.put(source.name, source);
            }

            loadFavoriteSources();


            return sources;
        }

        @Override
        protected void onPostExecute(final Source[] sources) {



            Button saveBtn = (Button) findViewById(R.id.save_button);
            Button clearBtn = (Button) findViewById(R.id.clear_custom);

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearCheckboxes(list);
                    favoriteSources.clear();
                    saveFavoriteSources();
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveFavoriteSources();
                }
            });

            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return allSources.size();
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
                    CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

                    Source source = allSources.get(position);
                    title.setText(source.name);
                    category.setText(source.category);
                    checkBox.setChecked(favoriteSources.contains(source));

                    convertView.setOnClickListener(SourceActivity.this);
                    checkBox.setOnClickListener(SourceActivity.this);

                    viewsToSources.put(convertView, source);
                    viewsToSources.put(checkBox, source);

                    Log.e("TAG", "cvid: " + convertView.hashCode());
                    Log.e("TAG", "cbid: " + checkBox.hashCode());

                    return convertView;
                }
            });
        }
    }
}
