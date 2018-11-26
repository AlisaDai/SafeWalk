package bcit.ca.safewalkfragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContextFragment extends ListFragment {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<>(
                inflater.getContext(), android.R.layout.simple_list_item_1, Contact.phoneCall
        );
        setListAdapter(arrayAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String phone = Contact.phoneCall[(int) id].getPhoneNumber();
        call(phone);
    }

    private void call(String phone) {
        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
        /*if (FragmentActivity.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            //Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }*/
        startActivity(intent);
    }
}
