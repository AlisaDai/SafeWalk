package ca.bcit.project.safewalk;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class InsertContactActivity extends AppCompatActivity {
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_contact);

        Spinner type = findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(InsertContactActivity.this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = findViewById(R.id.name);
                String name = edit.getText().toString();
                Spinner spinner = findViewById(R.id.type);
                String type = spinner.getSelectedItem().toString();
                Log.d("MyMessage", "Type is " + type);
                edit = findViewById(R.id.phone);
                String phone = edit.getText().toString();
                Contact newContact = new Contact(
                        name,
                        type,
                        phone
                );
                ContactDbHelper helper = new ContactDbHelper(InsertContactActivity.this);
                db = helper.getReadableDatabase();
                helper.insertContact(db, newContact);
                finish();
                startActivity(new Intent(InsertContactActivity.this, ContactActivity.class));
            }
        });
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(InsertContactActivity.this, ContactActivity.class));
            }
        });
    }
}
