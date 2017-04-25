package com.example.csanchez.ift2905_wordonthestreet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;
import android.widget.CheckBox;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * Created by nhk on 4/12/2017.
 */

public class FilterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        //buzzfeed
        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox);
        boolean checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBox1", false);
        checkBox1.setChecked(checked);
        //cnn
        final CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        boolean checked2 = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBox2", false);
        checkBox2.setChecked(checked2);
        //engadget
        final CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        boolean checked3 = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBox3", false);
        checkBox3.setChecked(checked3);
        //the verge
        final CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        boolean checked4 = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBox4", false);
        checkBox4.setChecked(checked4);
        //polygon
        final CheckBox checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        boolean checked5 = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBox5", false);
        checkBox5.setChecked(checked5);
        //ign
        final CheckBox checkBox6 = (CheckBox) findViewById(R.id.checkBox6);
        boolean checked6 = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBox6", false);
        checkBox6.setChecked(checked6);

        Button clearCheckbox = (Button) findViewById(R.id.button);

        clearCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox1.setChecked(false);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("checkBox1", false).commit();
                checkBox2.setChecked(false);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("checkBox2", false).commit();
                checkBox3.setChecked(false);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("checkBox3", false).commit();
                checkBox4.setChecked(false);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("checkBox4", false).commit();
                checkBox5.setChecked(false);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("checkBox5", false).commit();
                checkBox6.setChecked(false);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("checkBox6", false).commit();

            }
        });


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
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBox:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBox1", checked).commit();
                break;
            case R.id.checkBox2:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBox2", checked).commit();
                break;
            case R.id.checkBox3:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBox3", checked).commit();
                break;
            case R.id.checkBox4:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBox4", checked).commit();
                break;
            case R.id.checkBox5:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBox5", checked).commit();
                break;
            case R.id.checkBox6:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBox6", checked).commit();
                break;
        }
    }
}
