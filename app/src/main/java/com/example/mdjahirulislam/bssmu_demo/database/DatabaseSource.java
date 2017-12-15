package com.example.mdjahirulislam.bssmu_demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mdjahirulislam.bssmu_demo.model.AlarmModel;
import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;
import com.example.mdjahirulislam.bssmu_demo.model.UserModel;

import java.util.ArrayList;


/**
 * Created by Trainer on 3/29/2017.
 */

public class DatabaseSource {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private TaskModel taskModel;
//    private


    private static final String TAG = DatabaseSource.class.getSimpleName();


    public DatabaseSource(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() {
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    // -------------------------- Add SQLite --------------------------

    public boolean addUser(UserModel userModel) {
        this.open();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_USER_UNIQUE_ID, userModel.getUser_unique_id());
        values.put(DatabaseHelper.COL_USER_FULL_NAME, userModel.getUser_full_name());
        values.put(DatabaseHelper.COL_USER_MOBILE_NO, userModel.getUser_mobile_no());
        values.put(DatabaseHelper.COL_USER_EMAIL, userModel.getUser_email());
        values.put(DatabaseHelper.COL_USER_CREATED_AT, userModel.getCreated_at());


        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_USER_DETAILS, null, values);
        this.close();
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean addTask(TaskModel taskModel) {
        this.open();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_TASK_USER_ID, taskModel.getTaskUserID());
        values.put(DatabaseHelper.COL_TASK_TITLE, taskModel.getTaskName());
        values.put(DatabaseHelper.COL_TASK_LOCATION, taskModel.getTaskLocation());
        values.put(DatabaseHelper.COL_TASK_TIME, taskModel.getTaskTime());
        values.put(DatabaseHelper.COL_TASK_PRIORITY, taskModel.getPriority());
        values.put(DatabaseHelper.COL_TASK_DESCRIPTION, taskModel.getDescription());
        values.put(DatabaseHelper.COL_TASK_CREATED_AT, taskModel.getCreatedAt());


        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_USER_TASK, null, values);
        this.close();
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<TaskModel> getMyAllTask(String userId) {
        ArrayList<TaskModel> taskModelArrayList = new ArrayList<>();
        this.open();
        /*Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DatabaseHelper.TABLE_USER_DETAILS,null);*/

        String myOrder = DatabaseHelper.COL_TASK_ID+" DESC";

//        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_AD_POST, null, null, null, null, null, myOrder, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_USER_TASK+" WHERE "+DatabaseHelper.COL_TASK_USER_ID+" = "+userId +" ORDER BY "+myOrder,null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                int taskID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TASK_ID));
                String userUniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_USER_ID));
                String taskTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_TITLE));
                String taskLocation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_LOCATION));
                String taskTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_TIME));
                String taskPriority = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_PRIORITY));
                String taskDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_DESCRIPTION));
                String taskCreatedAt = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CREATED_AT));


//                travelEventModel = new MedicalHistoryModel(id,dr_id,prescription,name,details,date);
//                Log.d(TAG, "getMyAllPost ----- Post unique id-------" + postUniqueId);
//                Log.d(TAG, "getMyAllPost ----- Post Name-------" + postTitle);

                taskModel = new TaskModel( userUniqueId, taskTitle, taskLocation, Long.parseLong( taskTime ),
                        Integer.parseInt( taskPriority), taskDescription, Long.parseLong( taskCreatedAt));

                taskModelArrayList.add(taskModel);
                cursor.moveToNext();
            }
        }
        cursor.close();
        this.close();
        return taskModelArrayList;

    }

    public ArrayList<AlarmModel> getAlarmFromAlarmTable(String userUniqueId) {
        ArrayList<AlarmModel> alarmModelArrayList =new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_USER_TASK + " WHERE "+ DatabaseHelper.COL_TASK_USER_ID +" LIKE "
                +userUniqueId;

        this.open();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor!=null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            for (int i=0; i<cursor.getCount(); i++){
                String uniqueId=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_USER_ID));
                String taskID=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_ID));
                String alarmTime=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_TIME));
                AlarmModel alarmModel=new AlarmModel(uniqueId,taskID,alarmTime);
                alarmModelArrayList.add(alarmModel);

                cursor.moveToNext();
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        // return user
        Log.d(TAG, "Fetching data from Alarm table: ");

        return alarmModelArrayList;
    }

    public void addAlarmToSetAlarmTable(String setAlarmUniqueId,String alarmUniqueId) {

       this.open();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.KEY_SET_ALARM_ID, setAlarmUniqueId);
        values.put(DatabaseHelper.COL_TASK_ID,alarmUniqueId);

        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_SET_ALARM, null, values);
        sqLiteDatabase.close();
        Log.d(TAG, "New data inserted into SET Alarm Table: " + id);


    }

    public void deleteSetAlarm() {
        this.open();
        try {


            sqLiteDatabase.execSQL("delete from "+ DatabaseHelper.TABLE_SET_ALARM);
            Log.d(TAG, "deleting all set alarm");


        }catch (SQLException e){
            Log.d(TAG,e.toString());
        }finally {
            sqLiteDatabase.close();
        }
    }

//    public void whenLogoutUser() {
//        this.open();
//        sqLiteDatabase.delete(DatabaseHelper.TABLE_USER_DETAILS, null, null);
//        sqLiteDatabase.delete(DatabaseHelper.TABLE_AD_POST, null, null);
//        sqLiteDatabase.delete(DatabaseHelper.TABLE_ALL_ADS_IMAGE, null, null);
//        sqLiteDatabase.delete(DatabaseHelper.TABLE_TEMP_POST, null, null);
//        sqLiteDatabase.delete(DatabaseHelper.TABLE_FAVOURITE_ADS, null, null);
//
//        this.close();
//        Log.d(TAG, "Deleted all Table info from SQLite");
//    }


}
