package ca.bcit.project.safewalk;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private Contact[] contacts;
    private ContactAdapter adapter;
    //private ArrayAdapter<Contact> adapter;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, Contact.phoneCall
        );*/

        getContact();
        ListView listContacts = findViewById(R.id.contactList);
        listContacts.setTextFilterEnabled(true);
        /*adapter = new ArrayAdapter<>(
                this, R.layout.contact_list, Contact.phoneCall
        );*/
        adapter = new ContactAdapter(this, this, contacts);
        listContacts.setAdapter(adapter);
    }

    @Override   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_submenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                finish();
                return true;
            case R.id.plus:
                this.finish();
                startActivity(new Intent(this, InsertContactActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getContact() {
        SQLiteOpenHelper helper = new ContactDbHelper(this);
        try {
            db = helper.getReadableDatabase();
            cursor = db.query("CONTACT",
                    new String[] {"ID", "NAME", "TYPE", "PHONENUMBER"},
                    null,
                    null,
                    null, null, null);

            int count = cursor.getCount();
            Log.d("MyMessage: ", "getCount = " + count);
            contacts = new Contact[count];
            if (cursor.moveToFirst()) {
                int ndx=0;
                do {
                    contacts[ndx] = new Contact(cursor.getString(1), cursor.getString(2), cursor.getString(3));
                    contacts[ndx++].set_id(Integer.parseInt(cursor.getString(0)));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException sqlex) {
            String msg = "[MainActivity / getChildren] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
        if (db != null)
            db.close();
    }
}
