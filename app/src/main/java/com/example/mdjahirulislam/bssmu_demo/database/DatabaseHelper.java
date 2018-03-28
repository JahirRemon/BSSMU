package com.example.mdjahirulislam.bssmu_demo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Trainer on 3/29/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "task.bd";
    public static final int DATABASE_VERSION = 3;


    public static final String TABLE_USER_DETAILS = "user_registration_table";
    public static final String TABLE_USER_TASK = "user_task_table";
    public static final String TABLE_SET_ALARM = "alarm_set_table";
//    public static final String ORDER_BY = "COL_AD_POST_ID";



    // column of TABLE_USER_DETAILS

    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_UNIQUE_ID = "user_unique_id";
    public static final String COL_USER_FULL_NAME = "user_full_name";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_USER_PASSWORD = "user_password";
    public static final String COL_USER_AVATAR = "user_avatar";
    public static final String COL_USER_EMAIL = "user_email";
    public static final String COL_USER_ROLE = "role";
    public static final String COL_USER_DOCTOR_ID = "doctors_id";

    public static final String COL_TASK_ID = "task_id";
    public static final String COL_TASK_UNIQUE_ID = "task_unique_id";
    public static final String COL_TASK_USER_ID = "task_user_id";
    public static final String COL_TASK_TITLE = "task_title";
    public static final String COL_TASK_LOCATION = "task_location";
    public static final String COL_TASK_TIME = "task_time";
    public static final String COL_TASK_PRIORITY = "task_priority";
    public static final String COL_TASK_DESCRIPTION = "task_description";
    public static final String COL_TASK_CATEGORY = "task_category";
    public static final String COL_TASK_CREATED_AT = "task_created_at";
    public static final String COL_TASK_CREATOR_ID = "task_creator_id";


    public static final String KEY_SET_ALARM_ID = "set_alarm_id";




    /*public static final String CREATE_MOVIE_TABLE1 = "create table tbl_movie(tbl_id integer primary key, tbl_name text, tbl_year text);";*/

    public static final String CREATE_USER_REGISTRATION_TABLE = "create table "+ TABLE_USER_DETAILS +"("+
            COL_USER_ID +" integer primary key, "+
            COL_USER_UNIQUE_ID +" integer, "+
            COL_USER_FULL_NAME +" text, "+
            COL_USER_NAME +" text, "+
            COL_USER_PASSWORD +" text, "+
            COL_USER_AVATAR +" text, "+
            COL_USER_EMAIL +" text, "+
            COL_USER_ROLE +" text, "+
            COL_USER_DOCTOR_ID +" text);";

    public static final String CREATE_USER_TASK_TABLE = "create table "+ TABLE_USER_TASK +"("+
            COL_TASK_ID +" integer , "+
            COL_TASK_UNIQUE_ID +" integer primary key, "+
            COL_TASK_USER_ID +" integer, "+
            COL_TASK_TITLE +" text, "+
            COL_TASK_LOCATION +" text, "+
            COL_TASK_TIME +" text, "+
            COL_TASK_PRIORITY +" text, "+
            COL_TASK_DESCRIPTION +" text, "+
            COL_TASK_CATEGORY +" text, "+
            COL_TASK_CREATED_AT +" text, "+
            COL_TASK_CREATOR_ID +" text);";

    public static final String CREATE_SET_ALARM_TABLE = "CREATE TABLE " + TABLE_SET_ALARM + "("
            + KEY_SET_ALARM_ID + " TEXT UNIQUE,"
            + COL_TASK_ID + " TEXT );";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_REGISTRATION_TABLE);
        db.execSQL(CREATE_USER_TASK_TABLE);
        db.execSQL(CREATE_SET_ALARM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+ TABLE_USER_DETAILS);
        db.execSQL("drop table if exists "+ TABLE_USER_TASK);
        db.execSQL("drop table if exists "+ TABLE_SET_ALARM);
        onCreate(db);
    }
}
