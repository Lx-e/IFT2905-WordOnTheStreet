package com.example.csanchez.ift2905_wordonthestreet;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        Button buzzFeedCNN = (Button) findViewById(R.id.cat1);
        Button engadgetTheVerge = (Button) findViewById(R.id.cat2);
        Button polygonIGN = (Button) findViewById(R.id.cat3);
        Button allSources = (Button) findViewById(R.id.allSources);
        Button gaming = (Button) findViewById(R.id.gaming);



        buzzFeedCNN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                String[] src = {"buzzfeed","cnn"};
                intent.putExtra("sources", src);

                startActivity(intent);
            }
        });

        engadgetTheVerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                String[] src = {"engadget","the-verge"};
                intent.putExtra("sources", src);

                startActivity(intent);
            }
        });

        polygonIGN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                String[] src = {"polygon","ign"};
                intent.putExtra("sources", src);

                startActivity(intent);
            }
        });

        allSources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SourceActivity.class);
                String[] src = {};
                intent.putExtra("categories", src);

                startActivity(intent);
            }
        });

        gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SourceActivity.class);
                String[] src = {"gaming"};
                intent.putExtra("categories", src);

                startActivity(intent);
            }
        });

        changeTypeface(navigationView);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            // Handle the camera action
        } else if (id == R.id.nav_tags) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_book) {

        } else if (id == R.id.nav_settings) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
