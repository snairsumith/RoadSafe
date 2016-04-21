package com.example.rst.roadsafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Adptr {

    public static final String MYDATABASE_NAME = "Calllistener";
    public static final String MYDATABASE_TABLE = "Login";
    public static final String MYDATABASE_TABLE1 = "Blacklist";
    public static final String MYDATABASE_TABLE2 = "Personal_info";
    public static final String MYDATABASE_TABLE3 = "Settings";
    public static final String MYDATABASE_TABLE4 = "SMS";
    public static final String MYDATABASE_TABLE5 = "Calltracker";
    public static final String MYDATABASE_TABLE6 = "Smstracker";

    public static final int MYDATABASE_VERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "Username";
    public static final String KEY_PASSWORD = "password";


    public static final String KEY_ID1 = "_id";
    public static final String KEY_UNAME = "Uname";
    public static final String KEY_CONTACTNO = "Contactno";


    public static final String KEY_ID2 = "_id";
    public static final String KEY_MOB = "Mobile";
    public static final String KEY_USERNAME = "Loginname";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_PWD = "paswd";

    public static final String KEY_ID3 = "_id";
    public static final String KEY_APP = "App";
    public static final String KEY_SMS = "Sms";
    public static final String KEY_MAIL = "Email";
    public static final String KEY_LOCATION = "locat";

    public static final String KEY_ID4 = "_id";
    public static final String KEY_INBOX = "inbox";
    public static final String KEY_OUTBOX = "outbox";
    public static final String KEY_INCM = "incm";
    public static final String KEY_OUTCM = "outcm";
    public static final String KEY_APPL = "application";

    public static final String KEY_ID5 = "_id";
    public static final String KEY_INCOME = "income";
    public static final String KEY_OUTCOME = "outcome";

    public static final String KEY_ID6 = "_id";
    public static final String KEY_INBX= "inbx";
    public static final String KEY_OUTBX = "outbx";
    public static final String KEY_DRAFTS = "drafts";
    //create table MY_DATABASE (ID integer primary key, Content text not null);
    private static final String SCRIPT_CREATE_DATABASE =
            "create table " + MYDATABASE_TABLE + " ("
                    + KEY_ID + " integer primary key , "
                    + KEY_NAME + " text , "
                    + KEY_PASSWORD + " text not null);";

    private static final String SCRIPT_CREATE_DATABASE1 =
            "create table " + MYDATABASE_TABLE1 + " ("
                    + KEY_ID1 + " integer primary key , "
                    + KEY_UNAME + " text not null ,"
                    + KEY_CONTACTNO + " text not null);";


    private static final String SCRIPT_CREATE_DATABASE2 =
            "create table " + MYDATABASE_TABLE2 + " ("
                    + KEY_ID2 + " integer primary key , "
                    + KEY_MOB + " text , "
                    + KEY_USERNAME + " text , "
                    + KEY_PWD + " text , "
                    + KEY_EMAIL + " text not null);";



    private static final String SCRIPT_CREATE_DATABASE3 =
            "create table " + MYDATABASE_TABLE3 + " ("
                    + KEY_ID3 + " integer primary key , "
                    + KEY_APP + " text not null ,"
                    + KEY_SMS + " text not null ,"
                    + KEY_MAIL + " text not null ,"
                    + KEY_LOCATION + " text not null);";


    private static final String SCRIPT_CREATE_DATABASE4 =
            "create table " + MYDATABASE_TABLE4 + " ("
                    + KEY_ID4 + " integer primary key , "
                    + KEY_INBOX + " text not null ,"
                    + KEY_OUTBOX + " text not null ,"
                    + KEY_APPL + " text not null ,"
                    + KEY_OUTCM + " text not null ,"
                    + KEY_INCM + " text not null);";


    private static final String SCRIPT_CREATE_DATABASE5 =
            "create table " + MYDATABASE_TABLE5 + " ("
                    + KEY_ID5 + " integer primary key , "
                    + KEY_INCOME + " text not null ,"
                    + KEY_OUTCOME + " text not null);";



    private static final String SCRIPT_CREATE_DATABASE6 =
            "create table " + MYDATABASE_TABLE6 + " ("
                    + KEY_ID6 + " integer primary key , "
                    + KEY_INBX + " text not null ,"
                    + KEY_OUTBX + " text not null ,"
                    + KEY_DRAFTS + " text not null);";


    Context cd;
    public Adptr(Context c){
        cd = c;
    }


    private  helper ck;
    private SQLiteDatabase dk;
    public Adptr write()
    {
        ck=new helper(cd, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        dk=ck.getWritableDatabase();
        return this;
    }

    public Adptr read()
    {
        ck=new helper(cd, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        dk=ck.getReadableDatabase();
        return this;
    }
    public void close()
    {
        ck.close();
    }
    //<-----------------------------Login-------------------------------------------------->
    public long insrt_Login(String name,String paswd)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_PASSWORD, paswd);
        return dk.insert(MYDATABASE_TABLE,null,cv);
    }
    public long update_paswd(String paswd,String name)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_PASSWORD, paswd);

        return dk.update(MYDATABASE_TABLE, cv, KEY_NAME + "='"+name+"'", null);
    }
    public Cursor get_login()
    {
        String[] column=new String[]{KEY_ID,KEY_NAME,KEY_PASSWORD};
        Cursor cs=dk.query(MYDATABASE_TABLE, column, null, null, null, null, null);
        return cs;

    }
    public int deleteAllfrom_Login(){
        return dk.delete(MYDATABASE_TABLE, null, null);
    }


//<-------------------------------------Blacklist---------------------------------->

    public long insert_black(String name,String contact)

    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_UNAME,name);
        cv.put(KEY_CONTACTNO, contact);
        return dk.insert(MYDATABASE_TABLE1, null, cv);
    }

    public Cursor get_blacklist() {
        String[] column=new String[]{KEY_ID1,KEY_UNAME,KEY_CONTACTNO};
        Cursor cs=dk.query(MYDATABASE_TABLE1, column, null, null, null, null, null);
        return cs;
    }
    public int deletefrom_Bl(String name){
        return dk.delete(MYDATABASE_TABLE1,KEY_UNAME + "='"+name+"'", null);
    }


//<--------------------------------------------Details---------------------------------------------->



    public long insert_Details(String mob,String email,String uname,String paswd)

    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_MOB,mob);
        cv.put(KEY_EMAIL, email);
        cv.put(KEY_USERNAME, uname);
        cv.put(KEY_PWD, paswd);
        return dk.insert(MYDATABASE_TABLE2, null, cv);
    }

    public long update_details(String email,String mob,String uname)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_EMAIL, email);
        cv.put(KEY_MOB, mob);

        return dk.update(MYDATABASE_TABLE2, cv, KEY_USERNAME + " = '"+ uname + "'" , null);
    }


    public Cursor get_person_info(String name) {
        String[] column=new String[]{KEY_ID2,KEY_USERNAME,KEY_EMAIL,KEY_PWD,KEY_MOB};
        Cursor cs=dk.query(MYDATABASE_TABLE2, column, KEY_USERNAME+" = '" + name + "'", null, null, null, null);
        return cs;
    }

    public Cursor get_person_mob() {
        String[] column=new String[]{KEY_ID2,KEY_EMAIL,KEY_MOB};
        Cursor cs=dk.query(MYDATABASE_TABLE2, column, null, null, null, null, null);
        return cs;
    }

//================================settings==========================================================


    public long insert_settings(String app,String location,String sms,String mail)

    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_APP,app);
        cv.put(KEY_SMS, sms);
        cv.put(KEY_MAIL, mail);
        cv.put(KEY_LOCATION, location);
        return dk.insert(MYDATABASE_TABLE3, null, cv);
    }

    public Cursor get_location() {
        String[] column=new String[]{KEY_ID3,KEY_LOCATION};
        Cursor cs=dk.query(MYDATABASE_TABLE3, column,null, null, null, null, null);
        return cs;
    }
/*public int deletefrom_settings(){
return dk.delete(MYDATABASE_TABLE1,null, null);
}
*/

//================================sms==========================================================

    public long insert_sms(String app,String inbox,String outbox,String incm,String outcm)

    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_INBOX,inbox);
        cv.put(KEY_OUTBOX, outbox);
        cv.put(KEY_INCM, incm);
        cv.put(KEY_OUTCM, outcm);
        cv.put(KEY_APPL, app);

        return dk.insert(MYDATABASE_TABLE4, null, cv);
    }


//================================calltracker==========================================================

    public long insert_calltracker(String incom,String outcom)

    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_INCOME,incom);
        cv.put(KEY_OUTCOME, outcom);
        return dk.insert(MYDATABASE_TABLE5, null, cv);
    }


//================================smstracker==========================================================


    public long insert_smstracker(String inbx,String outbx,String draft)

    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_INBX,inbx);
        cv.put(KEY_OUTBX, outbx);
        cv.put(KEY_DRAFTS, draft);
        return dk.insert(MYDATABASE_TABLE6, null, cv);
    }

    public class helper extends SQLiteOpenHelper {

        public helper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }






        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(SCRIPT_CREATE_DATABASE);
            db.execSQL(SCRIPT_CREATE_DATABASE1);
            db.execSQL(SCRIPT_CREATE_DATABASE2);
            db.execSQL(SCRIPT_CREATE_DATABASE3);
            db.execSQL(SCRIPT_CREATE_DATABASE4);
            db.execSQL(SCRIPT_CREATE_DATABASE5);
            db.execSQL(SCRIPT_CREATE_DATABASE6);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }


    }
}
