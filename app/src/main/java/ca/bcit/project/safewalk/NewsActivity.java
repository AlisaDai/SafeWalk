package ca.bcit.project.safewalk;

import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    List<News> newsArray = new ArrayList<>();
    List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        readFile();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, names
        );
        ListView lv = findViewById(R.id.newsList);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NewsActivity.this, NewsDetailsActivity.class);
                Log.d("Running Message: ", "int " + i + ", long " + l);
                News currentNews = newsArray.get(i);
                Log.d("Running Message: ", "current News " + currentNews);
                intent.putExtra("news", currentNews);
                startActivity(intent);
            }
        });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                //Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(this, MainActivity.class);
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_contact:
                //Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show();
                //Intent j = new Intent(this, ContactActivity.class);
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void readFile(){
        // Read the raw csv file
        InputStream is = getResources().openRawResource(R.raw.crime_csv_all_years);

        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";

        try {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity","Line: " + line);
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");
                // Read the data
                News news = new News();

                // Setters
                if(tokens[0] != "" && tokens[0] != null){
                    news.setType(tokens[0]);
                }
                if(tokens[1] != "" && tokens[1] != null){
                    news.setYear(Integer.parseInt(tokens[1]));
                }
                if(tokens[2] != "" && tokens[2] != null){
                    news.setMonth(Integer.parseInt(tokens[2]));
                }
                if(tokens[3] != "" && tokens[3] != null){
                    news.setDay(Integer.parseInt(tokens[3]));
                }
                if(tokens[4].isEmpty() && tokens[4] != "" && tokens[4] != null){
                    news.setHour(Integer.parseInt(tokens[4]));
                }
                Log.d("Tokens4", "Tokens 4: " + tokens[4]);
                if(tokens[5] != "" && tokens[5] != null){
                    news.setMinute(Integer.parseInt(tokens[5]));
                }
                if(tokens[6] != "" && tokens[6] != null){
                    news.setBlock(tokens[6]);
                }
                if(tokens[7] != "" && tokens[7] != null){
                    news.setNeighbourhood(tokens[7]);
                }
                if(tokens[8] != "" && tokens[8] != null){
                    if(tokens[8] == "0"){
                        news.setX(0);
                    }else{
                        news.setX(Float.parseFloat(tokens[8]));
                    }
                }
                if(tokens[9] != "" && tokens[9] != null){
                    if(tokens[9] == "0"){
                        news.setX(0);
                    }else {
                        news.setY(Float.parseFloat(tokens[9]));
                    }
                }
                // Adding object to a class
                newsArray.add(news);
                names.add(news.getType() + "\nAt " + news.getBlock() + " via " + news.getNeighbourhood());
                // Log the object
                Log.d("My Activity", "Just created: " + news.getBlock());
            }
        } catch (IOException e) {
            // Logs error with priority level
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);
        }
    }
}
