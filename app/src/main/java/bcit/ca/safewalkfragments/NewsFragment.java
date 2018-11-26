package bcit.ca.safewalkfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends ListFragment {
    List<News> newsArray = new ArrayList<>();
    List<String> names = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        readFile();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                inflater.getContext(), android.R.layout.simple_list_item_1, names
        );
        setListAdapter(arrayAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        Log.d("Running Message: ", "int " + position + ", long " + id);
        News currentNews = newsArray.get(position);
        Log.d("Running Message: ", "current News " + currentNews);
        intent.putExtra("news", currentNews);
        startActivity(intent);
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
                if(!tokens[4].isEmpty() && tokens[4] != "" && tokens[4] != null){
                    news.setHour(tokens[4]);
                }
                //Log.d("Tokens4", "Tokens 4: " + tokens[4]);
                if(!tokens[5].isEmpty() && tokens[5] != "" && tokens[5] != null){
                    news.setMinute(tokens[5]);
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
