package com.example.csanchez.ift2905_wordonthestreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buzzFeedCNN = (Button) findViewById(R.id.cat1);
        Button engadgetTheVerge = (Button) findViewById(R.id.cat2);
        Button timeWired = (Button) findViewById(R.id.cat3);
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

        timeWired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                String[] src = {"time","wired-de"};
                intent.putExtra("sources", src);

                startActivity(intent);
            }
        });

        allSources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                String[] src = {"polygon", "ign"};
                intent.putExtra("sources", src);

                startActivity(intent);
            }
        });

        gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                String[] src = {"mtv-news", "national-geographic"};
                intent.putExtra("sources", src);

                startActivity(intent);
            }
        });
    }


}
