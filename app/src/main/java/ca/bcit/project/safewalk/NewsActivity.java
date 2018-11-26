package ca.bcit.project.safewalk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    List<News> newsArray = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new BackgroundTask().execute();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_back, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getJsonData() {
        String dataUrl;
        String jsonString;
        JSONArray articals;
        HttpHandler httpHandler = new HttpHandler();
        /*Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -30);
        Log.d("MyMessage:", c.getTime().toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(c.getTime());
        Log.d("MyMessage:", dateString);*/
        dataUrl = "https://webhose.io/filterWebContent?" +
                "token=40c52b54-70df-4cdd-a907-6b5cea01efd1&" +
                "format=json&ts=1540361487140&sort=crawled&" +
                "q=thread.country%3ACA%20thread.title%3A%28New%20Westminster%29";
                /*"https://newsapi.org/v2/everything?" +
                //"country=ca&" +
                "q=New&nbsp;Westminster&" +
                "from=2018-10-23" + //dateString +
                "&apiKey=71894e74c1024cf9a765fae7380ca40e";*/
        Log.d("MyMessage", dataUrl);
        jsonString = httpHandler.makeServiceCall(dataUrl);
        Log.d("MyMessage",jsonString);
        newsArray = new ArrayList<>();

        if (jsonString != null) {
            try {
                articals = new JSONArray(new JSONObject(jsonString).getString("posts"));
                Log.d("MyMessage", String.valueOf(articals.getJSONObject(0)));
                for (int index = 0; index < articals.length(); index++) {
                    JSONObject jsonObject = articals.getJSONObject(index);
                    Log.d("MyMessage to string", String.valueOf(jsonObject));
                    addNews(jsonObject);
                }

            } catch (JSONException e) {
                Log.e("MyMessage exception", e.toString());
            }
        } else {
            Log.e("MyMessage", "Could not load JSON file");
        }
    }



    private void addNews(JSONObject jsonObject) throws JSONException {
        String siteName = new JSONObject(jsonObject.getString("thread")).getString("site");
        String siteUrl = new JSONObject(jsonObject.getString("thread")).getString("site_section");
        String url = jsonObject.getString("url");
        String author = jsonObject.getString("author");
        String dateString = jsonObject.getString("published");
        Log.d("MyMessage", "published " + dateString);
        String title = jsonObject.getString("title");
        String content = jsonObject.getString("text");
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        Date date = new Date();
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date publishedAt = date;
        Log.d("MyMessage", "publishedAt " + publishedAt.toString());

        newsArray.add(new News (
                siteName,
                siteUrl,
                author,
                title,
                url,
                publishedAt,
                content
        ));
        titles.add(title);
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {
            //Log.d("MyMessage", "doInBackground");
            getJsonData();
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Log.d("MyMessage", "onPostExecute");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<> (
                    NewsActivity.this, android.R.layout.simple_list_item_1, titles
            );

            ListView lv = findViewById(R.id.newsList);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(NewsActivity.this, NewsDetailsActivity.class);
                    //Log.d("Running Message: ", "int " + i + ", long " + l);
                    News currentNews = newsArray.get(i);
                    //Log.d("Running Message: ", "current News " + currentNews);
                    intent.putExtra("news", currentNews);
                    startActivity(intent);
                }
            });
        }
    }
}
