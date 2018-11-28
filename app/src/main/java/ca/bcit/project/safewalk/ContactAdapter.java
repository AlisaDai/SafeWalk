package ca.bcit.project.safewalk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ContactAdapter extends BaseAdapter implements ListAdapter, Filterable {
    private Context context;
    private Activity activity;
    private Contact[] list;
    private SQLiteDatabase db;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private Contact[] originalData;
    private Contact[] filteredData;

    public ContactAdapter(Context context, Activity activity, Contact[] list) {
        this.list = list;
        this.activity = activity;
        this.context = context;

        this.originalData = list;
        this.filteredData = list;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Contact getItem(int pos) {
        return list[pos];
    }

    @Override
    public long getItemId(int pos) {
        return list[pos].getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = originalData;
                    results.count = originalData.length;
                } else {
                    final String prefixString = charSequence.toString().toLowerCase();
                    final ArrayList<Contact> filterResultsData = new ArrayList<>();
                    //filterResultsData = list.;

                    for (Contact data : originalData) {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if (data.getName().equalsIgnoreCase((String)charSequence)) {
                            filterResultsData.add(data);
                        }
                    }
                    Contact[] filteredResultseData = new Contact[filterResultsData.size()];
                    for(int i = 0; i < filterResultsData.size(); i++){
                        filteredResultseData[i] = filterResultsData.get(i);
                        Log.d("MyMessage: filtered", filteredResultseData[i].getName());
                    }
                    results.values = filteredResultseData;
                    results.count = filteredResultseData.length;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (Contact[]) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_list, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = view.findViewById(R.id.list_item);
        listItemText.setText(list[position].getName());
        Log.d("MyMessage", list[position].getName());

        //Handle buttons and add onClickListeners
        Button callBtn = view.findViewById(R.id.call);
        Button deleteBtn = view.findViewById(R.id.delete);

        callBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phone = list[position].getPhoneNumber();
                Log.d("MyMessage", phone);
                notifyDataSetChanged();
                call(phone);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ContactDbHelper helper = new ContactDbHelper(context);
                db = helper.getReadableDatabase();
                helper.deleteContact(db, (int)getItemId(position));
                notifyDataSetChanged();
                activity.finish();
                context.startActivity(new Intent(context, ContactActivity.class));
            }
        });
        return view;
    }

    private void call(String phone) {
        //Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            //Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
        Log.d("MyMessage","Start to activity");
        context.startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone)));
    }
}
