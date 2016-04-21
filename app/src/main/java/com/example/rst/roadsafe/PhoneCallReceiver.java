package com.example.rst.roadsafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;



/**
 * Created by sumith on 4/13/2016.
 */
public class PhoneCallReceiver extends BroadcastReceiver {
    Context context = null;
    private static final String TAG = "Phone call";
    private ITelephony telephonyService;
    private SharedPreferences myPrefs;
    private static final int MODE_WORLD_READABLE = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        myPrefs = context.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
          ITelephony  telephonyService = (ITelephony) m.invoke(tm);
            Bundle bundle = intent.getExtras();
            String phoneNumber = bundle.getString("incoming_number");
            Log.d("INCOMING", phoneNumber);
            if ((phoneNumber != null)) {
                String blockingMode = myPrefs.getString("mode", "yes");
                if (blockingMode.equals("on")) {
                    telephonyService.endCall();
                    Log.d("HANG UP", phoneNumber);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
