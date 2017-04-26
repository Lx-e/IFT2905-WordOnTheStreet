package com.example.csanchez.ift2905_wordonthestreet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    ListView list;

    List<String> allCategories = new ArrayList<String>();
    List<String> favoriteCategories = new ArrayList<String>();

    List<Source> allSources = new ArrayList<Source>();
    List<Source> favoriteSources = new ArrayList<Source>();

    Map<String, Source> namesToSources = new HashMap<String, Source>();
    Map<String, Source> idsToSources = new HashMap<String, Source>();

    Map<String, Integer> categoryToSourceCount = new HashMap<String, Integer>();
    Map<String, Integer> categoryToFavoriteSourceCount = new HashMap<String, Integer>();

    Map<View, String> viewsToCategories = new HashMap<View, String>();
    Map<String, TextView> countViewsByCategories = new HashMap<String, TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.setBackground(new ColorDrawable(0x000000FF));
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setBackgroundDrawable(new ColorDrawable(0x000000FF));
        toolbar.setLogo(getDrawable(R.drawable.wots2));

//        toolbar.setTitle("Select favorite categories");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setSupportActionBar(toolbar);


        list = (ListView) findViewById(R.id.listView_categories);

        CategoryActivity.CategoriesFetcher categoriesFetcher = new CategoryActivity.CategoriesFetcher();
        categoriesFetcher.execute();
    }

    protected void loadFavoritesInfo() {

        favoriteCategories = new ArrayList<String>();

        String categoriesStr = getSharedPreferences("SavedData", MODE_PRIVATE).getString("FavoriteCategories", "Nothing");//"No name defined" is the default value.
        Log.v("TAG", "RETRIEVED FAVORITE CATEGORIES: "+categoriesStr);

        if(categoriesStr != null && !categoriesStr.equals("Nothing")) {
            String[] categoryNames = categoriesStr.split(",");

            for (String categoryName: categoryNames) {
                categoryName = categoryName.trim();
                Log.v("TAG", "Parsed: " + categoryName);
                if (categoryName.length() > 0 && allCategories.contains(categoryName) && !favoriteCategories.contains(categoryName))
                    favoriteCategories.add(categoryName);
            }
        }

        String sourcesStr = getSharedPreferences("SavedData", MODE_PRIVATE).getString("FavoriteSources", "Nothing");//"No name defined" is the default value.
        Log.v("TAG", "RETRIEVED FAVORITE SOURCES: "+sourcesStr);

        if(sourcesStr == null || sourcesStr.equals("Nothing")) return;

        String[] sourceIds = sourcesStr.split(",");

        for (String sourceId: sourceIds) {
            sourceId = sourceId.trim();
            Log.v("TAG", "Parsed: " + sourceId);
            if (sourceId.length() > 0 && idsToSources.containsKey(sourceId)) {
                Source source =  idsToSources.get(sourceId);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if(resultCode == RESULT_OK) {
                String categoryName = data.getStringExtra("CategoryName");
                int favoriteCount = data.getIntExtra("Count", 0);
                int sourceCount = categoryToSourceCount.containsKey(categoryName) ?
                        categoryToSourceCount.get(categoryName) : 0;
                categoryToFavoriteSourceCount.put(categoryName, favoriteCount);
                countViewsByCategories.get(categoryName).setText((CharSequence)("(" + favoriteCount + "/" + sourceCount + ")"));
//                countViewsByCategories.get(categoryName).invalidate();
//                countViewsByCategories.get(categoryName).postInvalidate();

//                final TextView updatedView = countViewsByCategories.get(categoryName);
//                final String newText = "(" + favoriteCount + "/" + sourceCount + ")";
//                ListView items = ((ListView)findViewById(R.id.listView_categories));
//                for (int i = 0; i < items.getChildCount(); i++) {
//                    ViewGroup child = (ViewGroup)items.getChildAt(i);
//                    TextView tv = (TextView)((ViewGroup)((ViewGroup)items.getChildAt(0)).getChildAt(0)).getChildAt(0);
//                    if (categoryName.equals(tv.getText())) {
//                        TextView tv2 = (TextView)((ViewGroup)((ViewGroup)items.getChildAt(0)).getChildAt(0)).getChildAt(1);
//                        tv2.setText(newText);
//                        break;
//                    }
//
//                }


//                Runnable updateTextView = new Runnable() { public void run() {updatedView.setText(newText);}};
//                runOnUiThread(updateTextView);

            }


        }
    }



    @Override
    public void onClick(View v) {

        String categoryName = viewsToCategories.get(v);

        Intent intent = new Intent(getApplicationContext(), SourceActivity.class);
        intent.putExtra("Category", categoryName.toString());
        startActivityForResult(intent, 0);
        return;

//        if (v instanceof ImageButton) {
//            // Click was on the source config button
//            Intent intent = new Intent(getApplicationContext(), SourceActivity.class);
//            intent.putExtra("Category", categoryName.toString());
//            startActivityForResult(intent, 0);
//            return;
//        }
//
//        // Click was on the category item or its checkbox
//        if (favoriteCategories.contains(categoryName)) {
//            favoriteCategories.remove(categoryName);
//            Log.v("TAG", "Favorite category disabled: " + categoryName);
//
//        }
//        else {
//            favoriteCategories.add(categoryName);
//            Log.v("TAG", "Favorite category enabled: " + categoryName);
//        }
//
//        CheckBox cb = (v instanceof CheckBox) ? null : (CheckBox)v.findViewById(R.id.checkbox);
//        if (cb != null) cb.setChecked(!cb.isChecked());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
//                drawer.openDrawer(GravityCompat.START);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            Toast.makeText(getApplicationContext(), "favorites", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
        }
        else if (id == R.id.nav_history) {
            Toast.makeText(getApplicationContext(), "history", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
        }
        else if (id == R.id.nav_book) {
            SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
            int size = prefs.getInt("bookmark_size", 0);

            Toast.makeText(getApplicationContext(), "bookmarks"+((Integer)size).toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings) {
            //Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
            SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
            Toast.makeText(getApplicationContext(), ((Integer)prefs.getInt("bookmark_size",0)).toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), PagerCarlos.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                idsToSources.put(source.id, source);
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

                    checkBoxView.setVisibility(View.INVISIBLE);

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

                    countViewsByCategories.put(categoryName, countView);
                    viewsToCategories.put(convertView, categoryName);
                    viewsToCategories.put(checkBoxView, categoryName);

                    return convertView;
                }
            });
        }
    }
}
