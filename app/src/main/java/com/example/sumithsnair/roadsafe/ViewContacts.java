package com.example.sumithsnair.roadsafe;

import android.app.ListActivity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rst.roadsafe.R;

import java.util.ArrayList;


public class ViewContacts extends ListActivity {
    Adptr ad;
    String Name,MobNo;
ArrayList<String>Contact_result=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        ad=new Adptr(getApplicationContext());
        ad.read();
        Cursor s=ad.get_blacklist();
        if(s.moveToFirst())
        {
            do{
                MobNo=s.getString(s.getColumnIndex(Adptr.KEY_CONTACTNO));
                Name=s.getString(s.getColumnIndex(Adptr.KEY_UNAME));
                Contact_result.add(Name+":"+MobNo);


            }
            while (s.moveToNext());
        }
        TextView tView = new TextView(this);
        tView.setText("Stored Contact List");
        getListView().addHeaderView(tView);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Contact_result));
        getListView().setTextFilterEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
