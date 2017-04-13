package com.example.csanchez.ift2905_wordonthestreet;

import android.content.ContextWrapper;
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


public class SourceActivity extends AppCompatActivity {
    private String categories[] = {"gaming"};
    ListView list;
    List<String> customSources = new ArrayList();
    boolean checked[];
    ContextWrapper c = new ContextWrapper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

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

                File f = new File(c.getFilesDir().getPath() + "/custom_sources.dat");
                //check whether file exists
                FileInputStream is = new FileInputStream(f);
                int size = is.available();
                byte[] buffer = new byte[size];
                int isFile = is.read(buffer);
                is.close();
                Log.v("TAG", "isFile: " + isFile);
                if(isFile > 0) {
                    String readStr = new String(buffer);
                    readStr = readStr.subSequence(1, buffer.length - 1).toString();
                    Log.v("TAG", "STRING READ: " + readStr);
                    customSources = new ArrayList<String>();
                    String[] toLoad = readStr.split(",");
                    for (int i = 0; i < toLoad.length; i++) {
                        toLoad[i] = toLoad[i].subSequence(1, toLoad[i].length() - 1).toString();
                        Log.v("TAG", "Parsed: " + toLoad[i]);
                        customSources.add(toLoad[i]);
                        int checkBoxIndex = findSourceIndex(sources, toLoad[i]);
                        if(checkBoxIndex > -1){
                            checked[checkBoxIndex] = true;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            checked =  new boolean[sources.length];
            return sources;
        }

        @Override
        protected void onPostExecute(final Source[] sources) {
            Button saveBtn = (Button) findViewById(R.id.save_button);
            Button clearBtn = (Button) findViewById(R.id.clear_custom);

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        FileWriter file = new FileWriter(c.getFilesDir()+"/custom_sources.dat");
                        file.write(new Gson().toJson(customSources));

                        Log.v("TAG","WRITTEN TO FILE: "+(new Gson().toJson(customSources)));
                        file.flush();
                        file.close();
                    } catch (IOException e) {
                        Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
                    }
                }
            });

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        File file = new File(c.getFilesDir()+"/custom_sources.dat");
                        file.delete();

                }
            });

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
                        customSources.remove(((TextView) view.findViewById(R.id.name)).getText().toString());
                        checked[position] = true;
                        for(String source : customSources) {
                            Log.v("TAG",source);
                        }
                    }else{
                        cb.setChecked(true);
                        customSources.add(((TextView) view.findViewById(R.id.name)).getText().toString());
                        checked[position] = false;
                        Log.v("TAG",(new Gson().toJson(customSources)));
                        for(String source : customSources) {
                            Log.v("TAG","In customSources: "+source);
                        }
                    }
                }
            });

        }
    }

    public int findSourceIndex(Source[] src, String name){
        for(int j=0; j<src.length; j++){
            if(src[j].name == name){
                return j;
            }
        }
        return -1;
    }
}
