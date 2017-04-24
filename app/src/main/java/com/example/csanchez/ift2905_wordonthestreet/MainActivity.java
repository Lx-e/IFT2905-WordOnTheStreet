package com.example.csanchez.ift2905_wordonthestreet;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView list;
    private String[] srcArr;
    int notId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackground(new ColorDrawable(0x000000FF));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x000000FF));
//        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.wots));
        getSupportActionBar().setLogo(getDrawable(R.drawable.wots));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list = (ListView)findViewById(R.id.listView_main);

        final NewsFetcher news = new NewsFetcher();
        news.execute();

        changeTypeface(navigationView);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){


                TextView t = (TextView) view.findViewById(R.id.hiddenurl);
                String link = t.getText().toString();
                t = (TextView)view.findViewById(R.id.date);
                String newsDate = t.getText().toString();
                t = (TextView)view.findViewById(R.id.hiddenDescription);
                String desc = t.getText().toString();


                Intent intent = new Intent(getApplicationContext(), SingleNewsExpand.class);

                intent.putExtra("date", newsDate);
                intent.putExtra("desc", desc);
                intent.putExtra("link", link);
                intent.putExtra("caller", "MainActivity");
                startActivity(intent);
            }
        });
    }

    public class NewsFetcher extends AsyncTask<Object, Object, News[]> {

        @Override
        protected News[] doInBackground(Object... params) {

            News[] news = new News[0];

            try {
                SharedPreferences prefs = getSharedPreferences("SavedData", MODE_PRIVATE);
                String sourcesStr = prefs.getString("CustomSources", "Nothing");//"No name defined" is the default value.
                Log.v("TAG", "RETRIEVED: "+sourcesStr);

                if(sourcesStr.equals("Nothing")){
                    Log.v("TAG", "Really got : "+sourcesStr);
                    try{
                        String[] cat ={"general"};
                        Source[] sources = NewsAPI.getSources(cat); //Retrieve all sources(default)

                        srcArr = new String[sources.length];
                        for(int i=0; i<sources.length; i++){
                            srcArr[i] = sources[i].id;
                        }
                        Log.v("TAG", Arrays.toString(srcArr));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    srcArr = sourcesStr.split(","); //Retrieve favorite sources
                }
                news = NewsAPI.getNews(srcArr);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return news;
        }

        @Override
        protected void onPostExecute(final News[] news) {

            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return news.length;
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
                    if (convertView == null)
                        convertView = getLayoutInflater().inflate(R.layout.single_news, parent, false);

                    TextView title = (TextView) convertView.findViewById(R.id.title);
                    TextView date = (TextView) convertView.findViewById(R.id.date);
                    ImageView image = (ImageView) convertView.findViewById(R.id.image);
                    TextView hidDescription = (TextView) convertView.findViewById(R.id.hiddenDescription);
                    TextView hidUrl = (TextView) convertView.findViewById(R.id.hiddenurl);

                    title.setText(news[position].title);
                    date.setText(news[position].date.toString());
                    hidDescription.setText(news[position].description);
                    hidUrl.setText(news[position].url);
                    Picasso.with(getApplicationContext())
                            .load(news[position].image)
                            .into(image);

                    return convertView;
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            Toast.makeText(getApplicationContext(), "favorites", Toast.LENGTH_SHORT).show();

            //Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
            Intent intent = new Intent(getApplicationContext(), SourceActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_tags) {
            Toast.makeText(getApplicationContext(), "tags", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_history) {
            Toast.makeText(getApplicationContext(), "history", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_book) {
            SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
            int size = prefs.getInt("bookmark_size", 0);

            Toast.makeText(getApplicationContext(), "bookmarks"+((Integer)size).toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            //Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
            SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
            Toast.makeText(getApplicationContext(), ((Integer)prefs.getInt("bookmark_size",0)).toString(), Toast.LENGTH_SHORT).show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


/*
    public void onSendNotificationsButtonClick(View view) {
        NotificationEventReceiver.setupAlarm(getApplicationContext());
    }*/



    private void applyFontToItem(MenuItem item, Typeface font) {
        SpannableString mNewTitle = new SpannableString(item.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, 30), 0 ,
                mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        item.setTitle(mNewTitle);
    }

    private void changeTypeface(NavigationView navigationView){
        FontTypeface fontTypeface = new FontTypeface(this);
        Typeface typeface = fontTypeface.getTypefaceAndroid();

        MenuItem item;

        item = navigationView.getMenu().findItem(R.id.nav_book);
        item.setTitle("Bookmarks");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_fav);
//        item.setTitle("Galery");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_history);
//        item.setTitle("Slideshow");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_settings);
//        item.setTitle("Manage");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_tags);
//        item.setTitle("Share");
        applyFontToItem(item, typeface);

    }



}
