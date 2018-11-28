package ca.bcit.project.safewalk;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class ContactDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Contact_List.sqlite";
    private static final int DB_VERSION = 2;
    private Context context;

    public ContactDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        //Log.d("SQL Message: ", "SQL onCreate()");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateMyDatabase(sqLiteDatabase, i, i1);
    }

    private String getCreateContactTableSql() {
        String sql = "";
        sql += "CREATE TABLE CONTACT (";
        sql += "ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += "NAME TEXT, ";
        sql += "TYPE TEXT, ";
        sql += "PHONENUMBER TEXT);";
        return sql;
    }

    public void insertContact(SQLiteDatabase db, Contact contact) {
        ContentValues values = new ContentValues();
        values.put("NAME", contact.getName());
        values.put("TYPE", contact.getType());
        values.put("PHONENUMBER", contact.getPhoneNumber());
        db.insert("CONTACT", null, values);
    }

    public void updateContact(SQLiteDatabase db, Contact contact, int id){
        ContentValues values = new ContentValues();
        values.put("NAME", contact.getName());
        values.put("TYPE", contact.getType());
        values.put("PHONENUMBER", contact.getPhoneNumber());
        db.update("CONTACT", values, "ID = ?", new String[]{String.valueOf(id)});
    }

    public void deleteContact(SQLiteDatabase db, int id){
        db.delete("CONTACT", "ID = ?", new String[]{String.valueOf(id)});
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 1) {
                Log.d("MyMessage", "InsertData");
                db.execSQL(getCreateContactTableSql());
                for (Contact c : Contact.phoneCall) {
                    Log.d("MyMessage", c.toString());
                    insertContact(db, c);
                }
            }
        } catch (SQLException sqle) {
            String msg = "[ContactDbHelper/updateContact/insertContact] DB unavailable";
            msg += "\n\n" + sqle.toString();
            Toast t = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            t.show();
        }
    }
}
