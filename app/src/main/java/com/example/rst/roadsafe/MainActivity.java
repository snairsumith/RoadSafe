package com.example.rst.roadsafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.rst.roadsafe.R;

import java.lang.reflect.Method;

import internal.telephony.ITelephony;


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
    double speed;
    String place,userMessage;

    double lat1 = 0,lng1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initial my Preferance
        myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        editor=myPrefs.edit();

        setContentView(R.layout.activity_main);
        final TextView tv=(TextView)findViewById(R.id.textView);
        Chronometer ch=(Chronometer)findViewById(R.id.chronometer);
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
                Intent strint = new Intent(MainActivity.this, Display.class);
                startActivity(strint);
            }
        });
            startService(new Intent(this,MyService.class));


        /*ch.start();
        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            double lat2, lat1, lng2, lng1;
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                gps=new GPSTracker(MainActivity.this);
                double lat2 = gps.getLatitude();
                double lng2 = gps.getLongitude();
                double dLat = Math.toRadians(lat2 - lat1);
                double dLon = Math.toRadians(lng2 - lng1);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);
                double c = 2 * Math.asin(Math.sqrt(a));
                long distanceInMeters = Math.round(6371000 * c);
                tv.setText(String.valueOf(distanceInMeters));
                lat1=lat2;
                lng1=lng2;
            }
        });


        ch.start();
        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {
                    tv.setText(gps.getSpeed() + " : " + gps.getLatitude() + " : " + gps.getLongitude());
                }
            }
        });*/
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
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        speed = gps.getSpeed();
                        place = gps.getLocationName(latitude, longitude);


                        userMessage = "I am in Panic Situation. Please help me.My Current Location:" + place.substring(0, 70);
                        /*MobNo="9447092904";
                        SmsManager sm=SmsManager.getDefault();
                        sm.sendTextMessage(MobNo, null, userMessage, null, null);*/

                        ad = new Adptr(getApplicationContext());
                        ad.read();
                        Cursor s = ad.get_blacklist();
                        if (s.moveToFirst()) {
                            do {
                                MobNo = s.getString(s.getColumnIndex(Adptr.KEY_CONTACTNO)).toString();
                                Name = s.getString(s.getColumnIndex(Adptr.KEY_UNAME)).toString();
                                Toast.makeText(MainActivity.this, MobNo + Name, Toast.LENGTH_LONG).show();
                                //System.out.println("Mob No :"+MobNo+" ,Name :"+Name+",UserMessage :"+userMessage);
                                SmsManager sm = SmsManager.getDefault();
                                sm.sendTextMessage(MobNo, null, userMessage, null, null);
                            }
                            while (s.moveToNext());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No Contacts", Toast.LENGTH_SHORT).show();
                    }
                    //Send Message
                    ad = new Adptr(getApplicationContext());
                    ad.read();
                    Cursor s = ad.get_blacklist();
                    if (s.moveToFirst()) {
                        do {
                            MobNo = s.getString(s.getColumnIndex(Adptr.KEY_CONTACTNO));
                            Name = s.getString(s.getColumnIndex(Adptr.KEY_UNAME));
                            Toast.makeText(MainActivity.this, MobNo + Name, Toast.LENGTH_LONG).show();
                            //System.out.println("Mob No :"+MobNo+" ,Name :"+Name+",UserMessage :"+userMessage);
                            SmsManager sm = SmsManager.getDefault();
                            sm.sendTextMessage(MobNo, null, userMessage, null, null);
                        }
                        while (s.moveToNext());
                    }

                    //System.out.println("Mob No :"+MobNo+" ,Name :"+Name+",UserMessage :"+userMessage);

                } else {

                }
            }

        });



//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        // Define a listener that responds to location updates
//        LocationListener locationListener = new LocationListener()
//        {
//            public void onLocationChanged(Location location) {
//                try {
//                    location.getLatitude();
//
//                    Toast.makeText(context, "Current speed:" + location.getSpeed(), Toast.LENGTH_SHORT).show();
//                    myPrefs = context.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
//                    String blockingMode = myPrefs.getString("mode", "yes");
//                    Bundle bb = intent.getExtras();
//                    String state = bb.getString(TelephonyManager.EXTRA_STATE);
//                    if ((state != null) && (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))) {
//                        incommingNumber = bb.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                        if (blockingMode.equals("yes")) {
//                            blockCall(context, bb);
//                        }
//                    }
//                }
//                catch (NullPointerException ex)
//                {
//                    //Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//            public void onProviderEnabled(String provider) {
//            }
//            public void onProviderDisabled(String provider) {
//            }
//        };
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    }
    @Override
    public void onResume() {
        super.onResume();

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));
    }
    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            Toast.makeText(MainActivity.this, "Haiiiiii:Got Msg"+message, Toast.LENGTH_SHORT).show();


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
    };

    public void CallBlock()
    {


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

        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.help:
                helpMenuItem();
                break;
            case R.id.about:
                aboutMenuItem();
                break;
        }
        return  true;
    }
    private void helpMenuItem(){
        Intent intent = new Intent(this, help.class);
        this.startActivity(intent);

    }
    private void aboutMenuItem(){
        Intent intent = new Intent(this, aboutapp.class);
        this.startActivity(intent);

    }
}
