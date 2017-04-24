package com.example.csanchez.ift2905_wordonthestreet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{

    ListView list;

    List<String> allCategories = new ArrayList<String>();
    List<String> favoriteCategories = new ArrayList<String>();

    List<Source> allSources = new ArrayList<Source>();
    List<Source> favoriteSources = new ArrayList<Source>();

    Map<String, Source> namesToSources = new HashMap<String, Source>();

    Map<String, Integer> categoryToSourceCount = new HashMap<String, Integer>();
    Map<String, Integer> categoryToFavoriteSourceCount = new HashMap<String, Integer>();

    Map<View, String> viewsToCategories = new HashMap<View, String>();
    Map<String, TextView> countViewsByCategories = new HashMap<String, TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ((Toolbar)findViewById(R.id.toolbar2)).setTitle("Select favorite categories");

        list = (ListView) findViewById(R.id.listView_categories);

        CategoryActivity.CategoriesFetcher categoriesFetcher = new CategoryActivity.CategoriesFetcher();
        categoriesFetcher.execute();
    }

    protected void loadFavoritesInfo() {

        favoriteCategories = new ArrayList<String>();

        String categoriesStr = getSharedPreferences("SavedData", MODE_PRIVATE).getString("FavoriteCategories", "Nothing");//"No name defined" is the default value.
        Log.v("TAG", "RETRIEVED FAVORITE CATEGORIES: "+categoriesStr);

        if(categoriesStr == null || categoriesStr.equals("Nothing")) return;

        String[] categoryNames = categoriesStr.split(",");

        for (String categoryName: categoryNames) {
            categoryName = categoryName.trim();
            Log.v("TAG", "Parsed: " + categoryName);
            if (categoryName.length() > 0 && allCategories.contains(categoryName) && !favoriteCategories.contains(categoryName))
                favoriteCategories.add(categoryName);
        }


        String sourcesStr = getSharedPreferences("SavedData", MODE_PRIVATE).getString("FavoriteSources", "Nothing");//"No name defined" is the default value.
        Log.v("TAG", "RETRIEVED FAVORITE SOURCES: "+sourcesStr);

        if(sourcesStr == null || sourcesStr.equals("Nothing")) return;

        String[] sourceNames = sourcesStr.split(",");

        for (String sourceName: sourceNames) {
            sourceName = sourceName.trim();
            Log.v("TAG", "Parsed: " + sourceName);
            if (sourceName.length() > 0 && namesToSources.containsKey(sourceName)) {
                Source source =  namesToSources.get(sourceName);
                if (!favoriteSources.contains(source)) {
                    favoriteSources.add(source);
                    int count = categoryToFavoriteSourceCount.containsKey(source.category) ?
                            categoryToFavoriteSourceCount.get(source.category) : 0;
                    categoryToFavoriteSourceCount.put(source.category, ++count);
                }
            }
        }


    }

    protected void saveFavoriteCategories() {

        StringBuffer categoriesBuffer = new StringBuffer();
        if (favoriteCategories.size() == 0)
            categoriesBuffer.append("Nothing");
        else
            for (String category: favoriteCategories) { categoriesBuffer.append(category.trim()).append(","); }

        SharedPreferences.Editor editor = getSharedPreferences("SavedData", MODE_PRIVATE).edit();
        editor.remove("FavoriteCategories");
        editor.putString("FavoriteCategories", categoriesBuffer.toString());
        editor.commit();
        Log.v("TAG", "SAVING FAVORITE CATEGORIES: "+ categoriesBuffer.toString());
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
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if(resultCode == RESULT_OK) {
                String categoryName = data.getStringExtra("CategoryName");
                int favoriteCount = data.getIntExtra("FavoriteCount", 0);
                int sourceCount = categoryToSourceCount.containsKey(categoryName) ?
                        categoryToSourceCount.get(categoryName) : 0;
                categoryToFavoriteSourceCount.put(categoryName, favoriteCount);
                countViewsByCategories.get(categoryName).setText("(" + favoriteCount + "/" + sourceCount + ")");
            }
        }
    }

    @Override
    public void onClick(View v) {

        String categoryName = viewsToCategories.get(v);

        if (v instanceof ImageButton) {
            // Click was on the source config button
            Intent intent = new Intent(getApplicationContext(), SourceActivity.class);
            intent.putExtra("Category", categoryName.toString());
            startActivityForResult(intent, 0);
            return;
        }

        // Click was on the category item or its checkbox
        if (favoriteCategories.contains(categoryName)) {
            favoriteCategories.remove(categoryName);
            Log.v("TAG", "Favorite category disabled: " + categoryName);
        }
        else {
            favoriteCategories.add(categoryName);
            Log.v("TAG", "Favorite category enabled: " + categoryName);
        }

        CheckBox cb = (v instanceof CheckBox) ? null : (CheckBox)v.findViewById(R.id.checkbox);
        if (cb != null) cb.setChecked(!cb.isChecked());

    }

    public class CategoriesFetcher extends AsyncTask<Object, Object, Source[]> {

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
                if (!allSources.contains(source)) allSources.add(source); else continue;
                namesToSources.put(source.name, source);
                if (!allCategories.contains(source.category)) allCategories.add(source.category);
                int count = categoryToSourceCount.containsKey(source.category) ?
                        categoryToSourceCount.get(source.category) : 0;
                categoryToSourceCount.put(source.category, ++count);
            }

            loadFavoritesInfo();

            return sources;
        }

        @Override
        protected void onPostExecute(final Source[] sources) {

            ((Button)findViewById(R.id.clear_cat_custom)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearCheckboxes(list);
                    favoriteCategories.clear();
                    saveFavoriteCategories();
                }
            });

            ((Button)findViewById(R.id.save_cat_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveFavoriteCategories();
                }
            });

            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return allCategories.size();
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

                    TextView nameView    = (TextView) convertView.findViewById(R.id.name);
                    TextView countView = (TextView) convertView.findViewById(R.id.count);
                    CheckBox checkBoxView = (CheckBox) convertView.findViewById(R.id.checkbox);
                    ImageButton buttonView = (ImageButton) convertView.findViewById(R.id.config);

                    String categoryName = allCategories.get(position);
                    int favoriteCount = categoryToFavoriteSourceCount.containsKey(categoryName) ?
                            categoryToFavoriteSourceCount.get(categoryName) :0;
                    int totalCount = categoryToSourceCount.containsKey(categoryName) ?
                            categoryToSourceCount.get(categoryName) :0;

                    nameView.setText(categoryName);
                    countView.setText("(" + favoriteCount + "/" + totalCount + ") ");
                    checkBoxView.setChecked(favoriteCategories.contains(categoryName));

                    convertView.setOnClickListener(CategoryActivity.this);
                    checkBoxView.setOnClickListener(CategoryActivity.this);
                    buttonView.setOnClickListener(CategoryActivity.this);

                    countViewsByCategories.put(categoryName, countView);
                    viewsToCategories.put(convertView, categoryName);
                    viewsToCategories.put(checkBoxView, categoryName);
                    viewsToCategories.put(buttonView, categoryName);

                    return convertView;
                }
            });
        }
    }
}
