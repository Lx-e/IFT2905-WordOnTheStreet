package com.example.csanchez.ift2905_wordonthestreet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;


public class SourceActivity extends AppCompatActivity {
    private String categories[] = {"gaming"};
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        categories = getIntent().getExtras().getStringArray("categories");

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

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return sources;
        }

        @Override
        protected void onPostExecute(final Source[] sources) {

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
                    //TextView description = (TextView) convertView.findViewById(R.id.description);
                    ImageView image = (ImageView) convertView.findViewById(R.id.image);

                    title.setText(sources[position].name);
                    //description.setText(sources[position].description);

                    Picasso.with(getApplicationContext())
                            .load(sources[position].mediumLogo)
                            .into(image);

                    return convertView;
                }
            });

        }
    }
}
