package ca.bcit.project.safewalk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, Contact.phoneCall
        );

        ListView listContacts = findViewById(R.id.contactList);
        listContacts.setAdapter(arrayAdapter);


        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                //Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
                //intent.putExtra("index", (int)id);
                //startActivity(intent);

                String phone = Contact.phoneCall[(int) id].getPhoneNumber();
                call(phone);
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
                //Intent j = new Intent(this, ContactActivity.class);
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_news:
                //Toast.makeText(this, "News", Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(this, NewsActivity.class);
                startActivity(new Intent(this, NewsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void call(String phone) {
        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            //Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
        startActivity(intent);
    }
}
