package ca.bcit.project.safewalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        News currentNews = (News)getIntent().getSerializableExtra("news");
        //Log.d("Getting Message","========================" + currentNews);
        TextView tv = findViewById(R.id.title);
        tv.setText(currentNews.getTitle());
        addLink(tv, currentNews.getTitle(), currentNews.getUrl());
        tv = findViewById(R.id.author);
        tv.setText("By: " + currentNews.getAuthor() + "\nAt: " + currentNews.getPublicshedAt());
        tv = findViewById(R.id.site);
        tv.setText("On @" + currentNews.getSourceName());
        addLink(tv, currentNews.getSourceName(),  currentNews.getSiteUrl());
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setLinksClickable(true);
        tv = findViewById(R.id.content);
        tv.setText(currentNews.getContent());
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


    public static void addLink(TextView textView, String patternToMatch,
                               final String link) {
        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            @Override public String transformUrl(Matcher match, String url) {
                return link;
            }
        };
        Linkify.addLinks(textView, Pattern.compile(patternToMatch), null, null,
                filter);
    }
}
