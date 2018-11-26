package bcit.ca.safewalkfragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        News currentNews = (News)getIntent().getSerializableExtra("news");
        Log.d("Getting Message","========================" + currentNews);
        TextView tv = findViewById(R.id.textView);
        tv.setText(currentNews.getType() + "\nAt: "
                + currentNews.getBlock() + " via "
                + currentNews.getNeighbourhood() + "\n("
                + currentNews.getX() + ", " + currentNews.getY() + ")\n In "
                + currentNews.getMonth() + " " + currentNews.getDay() + ", " + currentNews.getYear());
        if(currentNews.getHour() != "00" || currentNews.getMinute() != "00"){
            tv.setText(tv.getText() + ", "
                    + currentNews.getHour() + " : " + currentNews.getMinute());
        }
    }
}
