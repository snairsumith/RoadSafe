package com.example.sumithsnair.roadsafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.android.internal.telephony.ITelephony;
import com.example.rst.roadsafe.R;

import java.lang.reflect.Method;


public class MainActivity extends ActionBarActivity {
       private SharedPreferences.Editor editor;
    Context context;
    private static final int MODE_WORLD_READABLE = 1;
    private ITelephony telephonyService;
    private String incommingNumber;
    private String incommingName=null;
    private SharedPreferences myPrefs;
    String MobNo,Name;
    Adptr ad;
    Intent intent;
    GPSTracker gps;
    double latitude,longitude;
    String place,userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        editor=myPrefs.edit();
        setContentView(R.layout.activity_main);
        Button btnAddContact=(Button)findViewById(R.id.button);
        Button btnViewContact=(Button)findViewById(R.id.button4);
        btnViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent strint=new Intent(MainActivity.this,ViewContacts.class);
                startActivity(strint);
            }
        });
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent strint=new Intent(MainActivity.this,Display.class);
                startActivity(strint);
            }
        });
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        ToggleButton toggleEmergency = (ToggleButton) findViewById(R.id.toggleButton2);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putString("mode", "on");
                    editor.commit();
                } else {
                    editor.putString("mode", "off");
                    editor.commit();
                }
            }
        });
        toggleEmergency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gps = new GPSTracker(MainActivity.this);

                    // check if GPS enabled
                    if(gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        place = gps.getLocationName(latitude, longitude);

                        userMessage = "I am in Panic Situvation .Please help me.My Current Location:" + place.substring(0,70);
                    }
                    else
                    {
                        userMessage = "I am in Panic Situvation .Please help me.My Latitude :" + "null" + " & Logitude :" + "null" + " & Place Name :" + "null";
                    }
                        //Send Message
                    ad=new Adptr(getApplicationContext());
                    ad.read();
                    Cursor s=ad.get_blacklist();
                    if(s.moveToFirst())
                    {
                        do{
                            MobNo=s.getString(s.getColumnIndex(Adptr.KEY_CONTACTNO));
                            Name=s.getString(s.getColumnIndex(Adptr.KEY_UNAME));
                            Toast.makeText(MainActivity.this,MobNo+Name,Toast.LENGTH_LONG).show();
                            System.out.println("Mob No :"+MobNo+" ,Name :"+Name+",UserMessage :"+userMessage);
//                            SmsManager sm=SmsManager.getDefault();
//                            sm.sendTextMessage(MobNo, null, userMessage, null, null);
                        }
                        while (s.moveToNext());
			 			    }

                   //System.out.println("Mob No :"+MobNo+" ,Name :"+Name+",UserMessage :"+userMessage);

                }
                else {

                }
                }

        });



        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location) {
                try {
                    location.getLatitude();

//                 Toast.makeText(context, "Current speed:" + location.getSpeed(), Toast.LENGTH_SHORT).show();
                    myPrefs = context.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
                    String blockingMode = myPrefs.getString("mode", "yes");
                    Bundle bb = intent.getExtras();
                    String state = bb.getString(TelephonyManager.EXTRA_STATE);
                    if ((state != null) && (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))) {
                        incommingNumber = bb.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        if (blockingMode.equals("yes")) {
                            blockCall(context, bb);
                        }
                    }
                }
                catch (NullPointerException ex)
                {
                    //Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    }
    public void blockCall(Context c, Bundle b)
    {

        TelephonyManager telephony = (TelephonyManager)
                c.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class cls = Class.forName(telephony.getClass().getName());
            Method m = cls.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            //telephonyService.silenceRinger();
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
