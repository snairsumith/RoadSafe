package com.example.sumithsnair.roadsafe;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddContacts extends ActionBarActivity {
    String user,contact,name,pswd;
    EditText txtNm,txtph;
    Adptr adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        Button btnSave=(Button)findViewById(R.id.button2);
         txtNm=(EditText)findViewById(R.id.txtName);
         txtph=(EditText)findViewById(R.id.txtPhno);
        adp=new Adptr(getApplicationContext());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=txtNm.getText().toString();
                contact=txtph.getText().toString();
                adp.write();
                long i=adp.insert_black(user,contact);
                adp.close();
                if(i>0){
                    Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contacts, menu);
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
