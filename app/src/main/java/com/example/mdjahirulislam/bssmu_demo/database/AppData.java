package com.example.mdjahirulislam.bssmu_demo.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mdjahirulislam.bssmu_demo.helper.ConnectionApi;

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

    private static final String USER_NAME_ = "username";
    private static final String PASSWORD_ = "password";

    private static final String KEY_USER_UNIQUE_ID="userUniqueId";



    public AppData(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void setUsername(String username) {
        editor.putString(USER_NAME_, username);
        editor.commit();
    }

    public String getUsername() {

        //  return sharedPreferences.getString(USERNAME_, "");
        return pref.getString( USER_NAME_,null ); //keep it like this until login panel will be implemented
    }

    public void setPassword(String password) {
        editor.putString(PASSWORD_, password);
        editor.commit();
    }

    public String getPassword() {

        //return sharedPreferences.getString(PASSWORD_, "");
        return pref.getString( PASSWORD_,null ); //keep it like this until login panel will be implemented
    }

    public void setUserId(String userId) {
        editor.putString(KEY_USER_UNIQUE_ID, userId);
        editor.commit();
        Log.d(TAG, "USER_ID modified!"+" ###_ID="+userId);
    }


    public String getUserId(){
        return pref.getString(KEY_USER_UNIQUE_ID, null);
    }

    public boolean logout(){
        editor.clear();
        editor.commit();
        return true;
    }

}
