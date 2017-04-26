package com.example.csanchez.ift2905_wordonthestreet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SourceActivity extends AppCompatActivity implements View.OnClickListener {

    String categoryName = null;
    ListView list = null;

    List<Source> allSources = new ArrayList<Source>();
    List<Source> favoriteSources = new ArrayList<Source>();
    List<Source> categorySources = new ArrayList<Source>();

    Map<String, Source> namesToSources = new HashMap<String, Source>();
    Map<String, Source> idsToSources = new HashMap<String, Source>();
    Map<View, Source> viewsToSources = new HashMap<View, Source>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

        categoryName = getIntent().getStringExtra("Category");

        list = (ListView) findViewById(R.id.listView_sources);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getMenu().clear();
        toolbar.setBackground(new ColorDrawable(0x000000FF));
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setBackgroundDrawable(new ColorDrawable(0x000000FF));
        toolbar.setLogo(getDrawable(R.drawable.wots2));

//        ((Toolbar)findViewById(R.id.toolbar)).setTitle("Selects sources for " + categoryName);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SourceActivity.SourceFetcher sourcesFetcher = new SourceActivity.SourceFetcher();
        sourcesFetcher.execute();
    }

    protected void loadFavoriteSources() {

        String sourcesStr = getSharedPreferences("SavedData", MODE_PRIVATE).getString(categoryName+"Sources", "Nothing");//"No name defined" is the default value.
        Log.v("TAG", "RETRIEVED FAVORITE SOURCES: "+sourcesStr);

        if(sourcesStr == null || sourcesStr.equals("Nothing")) return;

        String[] sourceIds = sourcesStr.split(",");

        for (String sourceIdStr: sourceIds) {
            sourceIdStr = sourceIdStr.trim();
            Log.v("TAG", "Parsed from "+categoryName+": " + sourceIdStr);
            if (sourceIdStr.length() > 0 && idsToSources.containsKey(sourceIdStr)) {
                Source source =  idsToSources.get(sourceIdStr);
                if (!favoriteSources.contains(source)) favoriteSources.add(source);
            }
        }
    }

    protected void saveFavoriteSources() {

        StringBuffer sourcesBuffer = new StringBuffer();
        if (favoriteSources.size() == 0)
            sourcesBuffer.append("Nothing");
        else
            for (Source source: favoriteSources) { sourcesBuffer.append(source.id).append(","); }

        SharedPreferences.Editor editor = getSharedPreferences("SavedData", MODE_PRIVATE).edit();
        editor.remove(categoryName+"Sources");
        editor.putString(categoryName+"Sources", sourcesBuffer.toString());
        editor.commit();
        Log.v("TAG", "SAVING FAVORITE "+categoryName+" SOURCES: "+ sourcesBuffer.toString());
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
//        super.onBackPressed();
        String sourcesStr = getSharedPreferences("SavedData", MODE_PRIVATE).getString(categoryName+"Sources", "Nothing");//"No name defined" is the default value.
        int resultCount = 0;
        if(sourcesStr != null && !sourcesStr.equals("Nothing")) {
            String[] sourceNames = sourcesStr.split(",");
//            for (String sourceName: sourceNames) {
//                Source source =  idsToSources.get(sourceName.trim());
//                if (source.category.equals(categoryName)) resultCount++;
//            }
            resultCount = sourceNames.length;
        }


        Intent intent = new Intent();
        intent.putExtra("CategoryName", categoryName);
        intent.putExtra(categoryName+"Count", resultCount);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {

        Source source = viewsToSources.get(v);
        if (source == null) return;

        if (favoriteSources.contains(source)) {
            favoriteSources.remove(source);
            Log.v("TAG", "Favorite source removed: " + new Gson().toJson(source));
        }
        else {
            favoriteSources.add(source);
            Log.v("TAG", "Favorite source added: " + new Gson().toJson(source));
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
                idsToSources.put(source.id, source);
                namesToSources.put(source.name, source);
                if (source.category.equals(categoryName)) categorySources.add(source);
            }

            loadFavoriteSources();

            return sources;
        }

        @Override
        protected void onPostExecute(final Source[] sources) {

            ((Button)findViewById(R.id.clear_custom)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearCheckboxes(list);
                    favoriteSources.clear();
                    saveFavoriteSources();
                }
            });

            ((Button)findViewById(R.id.save_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveFavoriteSources();
                }
            });

            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return categorySources.size();
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

                    Source source = categorySources.get(position);

                    if(convertView == null)
                        convertView = getLayoutInflater().inflate(R.layout.single_source, parent, false);

                    TextView nameView = (TextView) convertView.findViewById(R.id.name);
                    TextView countView = (TextView) convertView.findViewById(R.id.count);
                    CheckBox checkBoxView = (CheckBox) convertView.findViewById(R.id.checkbox);

                    countView.setVisibility(View.INVISIBLE);
                    countView.setText("");
                    nameView.setText(source.name);
                    checkBoxView.setChecked(favoriteSources.contains(source));

                    convertView.setOnClickListener(SourceActivity.this);
                    checkBoxView.setOnClickListener(SourceActivity.this);

                    viewsToSources.put(convertView, source);
                    viewsToSources.put(checkBoxView, source);

                    return convertView;
                }
            });
        }
    }
}
