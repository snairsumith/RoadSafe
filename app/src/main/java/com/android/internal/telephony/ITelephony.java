package com.android.internal.telephony;

/**
 * Created by sumith on 4/13/2016.
 */
public interface ITelephony {
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}
