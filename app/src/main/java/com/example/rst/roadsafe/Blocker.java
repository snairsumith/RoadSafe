package com.example.rst.roadsafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;


import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;



public class Blocker extends BroadcastReceiver {
    private static final int MODE_WORLD_READABLE = 1;
    private ITelephony telephonyService;
    private String incommingNumber;
    private String incommingName=null;
    private SharedPreferences myPrefs;
    @Override
    public void onReceive(Context context, Intent intent) {


        myPrefs = context.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String blockingMode=myPrefs.getString("mode", "yes");
        Bundle bb = intent.getExtras();
        String state = bb.getString(TelephonyManager.EXTRA_STATE);
        if ((state != null)&& (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))) {
            incommingNumber = bb.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if(blockingMode.equals("yes"))
            {
                blockCall(context, bb);
            }
        }
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
}
