package com.example.csanchez.ift2905_wordonthestreet;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

// Instances of this class are fragments representing a single
// object in our collection.
public class PagerCarlosFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    private String[] srcArr;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_news, container, false);
        list = (ListView) rootView.findViewById(R.id.listView_news);

        NewsFetcher news = new NewsFetcher();
        news.execute();

        return rootView;
    }
    public class NewsFetcher extends AsyncTask<Object, Object, News[]> {

        @Override
        protected News[] doInBackground(Object... params) {

            News[] news = new News[0];

            try {
                Bundle args = getArguments();

                SharedPreferences prefs = getActivity().getSharedPreferences("SavedData", MODE_PRIVATE);
                String sourcesStr = prefs.getString(args.getString("Category")+"Sources", "Nothing");//"No name defined" is the default value.
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
            } catch(ParseException e){
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

                    if(convertView == null)
                        convertView = getLayoutInflater(null).inflate(R.layout.single_news, parent, false);

                    TextView title = (TextView) convertView.findViewById(R.id.title);
                    TextView date = (TextView) convertView.findViewById(R.id.date);
                    ImageView image = (ImageView) convertView.findViewById(R.id.image);

                    title.setText(news[position].title);
                    date.setText(news[position].date.toString());

                    Picasso.with(getActivity().getApplicationContext())
                            .load(news[position].image)
                            .into(image);

                    return convertView;
                }
            });

        }
    }
}