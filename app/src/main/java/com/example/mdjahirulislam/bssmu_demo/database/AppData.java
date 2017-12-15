package com.example.mdjahirulislam.bssmu_demo.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Yasin on 11/6/2017.
 */

public class AppData {
    private static String TAG = AppData.class.getSimpleName();


    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;


    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "GhorerDoktar_Data";
    private static final String KEY_WOMEN_ID = "women_id";
    private static final String KEY_USER_UNIQUE_ID="userUniqueId";
    private static final String KEY_IS_SET_UP="isSetUp";
    private static final String KEY_LAST_MENSTRUATION_DATE="last_menstruation_date";
    private static final String KEY_ELDER_UNIQUE_ID="elderUniqueId";


    public AppData(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setWomenId(String womenId) {
        editor.putString(KEY_WOMEN_ID, womenId);
        editor.commit();
        Log.d(TAG, "WOMEN_ID modified!"+" ###_ID="+womenId);
    }


    public void setUserId(String userId) {
        editor.putString(KEY_USER_UNIQUE_ID, userId);
        editor.commit();
        Log.d(TAG, "USER_ID modified!"+" ###_ID="+userId);
    }

    public void setUp(boolean isSetUp){
        editor.putBoolean(KEY_IS_SET_UP,isSetUp);
        editor.commit();
        Log.d(TAG, "SetUp session modified!"+" ### ID="+isSetUp);
    }

    public void setLastMenstruationDate(Long firstTtTime) {
        editor.putLong(KEY_LAST_MENSTRUATION_DATE, firstTtTime);
        editor.commit();
//        Log.d(TAG, "LAST MENSTRUATION DATE modified!"+" ###DATE IS = "+Utilities.getOrganizeTimeFromMills(firstTtTime));
    }

    public void setElderUniqueId(String elderUniqueId){

        editor.putString( KEY_ELDER_UNIQUE_ID, elderUniqueId );
        editor.commit();

    }

    public String getElderUniqueId(){
        return pref.getString( KEY_ELDER_UNIQUE_ID,null );
    }



    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_WOMEN_ID, false);
    }

    public boolean isSetUp(){
        return pref.getBoolean(KEY_IS_SET_UP, false);
    }
    public String getUserId(){
        return pref.getString(KEY_USER_UNIQUE_ID, "1");
    }
    public String getWomenId(){
        return pref.getString(KEY_WOMEN_ID, null);
    }
    public Long getLastMenstruationDate(){
        return pref.getLong(KEY_LAST_MENSTRUATION_DATE,0);
    }
}
