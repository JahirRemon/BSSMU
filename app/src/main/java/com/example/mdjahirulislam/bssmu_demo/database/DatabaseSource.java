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
import java.util.Calendar;
import java.util.Date;


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
        values.put(DatabaseHelper.COL_USER_NAME, userModel.getUser_name());
        values.put(DatabaseHelper.COL_USER_PASSWORD, userModel.getPassword());
        values.put(DatabaseHelper.COL_USER_AVATAR, userModel.getUser_avatar());
        values.put(DatabaseHelper.COL_USER_EMAIL, userModel.getUser_email());
        values.put(DatabaseHelper.COL_USER_ROLE, userModel.getRole());
        values.put(DatabaseHelper.COL_USER_DOCTOR_ID, userModel.getDoctor_id());


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

        values.put(DatabaseHelper.COL_TASK_UNIQUE_ID, taskModel.getTaskId());
        values.put(DatabaseHelper.COL_TASK_USER_ID, taskModel.getTaskUserID());
        values.put(DatabaseHelper.COL_TASK_TITLE, taskModel.getTaskName());
        values.put(DatabaseHelper.COL_TASK_LOCATION, taskModel.getTaskLocation());
        values.put(DatabaseHelper.COL_TASK_TIME, taskModel.getTaskTime());
        values.put(DatabaseHelper.COL_TASK_PRIORITY, taskModel.getPriority());
        values.put(DatabaseHelper.COL_TASK_CATEGORY, taskModel.getCategory());
        values.put(DatabaseHelper.COL_TASK_DESCRIPTION, taskModel.getDescription());
        values.put(DatabaseHelper.COL_TASK_CREATED_AT, taskModel.getCreatedAt());
        values.put(DatabaseHelper.COL_TASK_CREATOR_ID, taskModel.getCreator_id());


        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_USER_TASK, null, values);
        this.close();
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }

    public UserModel getUser(String userID){
        UserModel userModel = new UserModel(  );
        this.open();
        Cursor cursor = sqLiteDatabase.rawQuery( "SELECT * FROM " + DatabaseHelper.TABLE_USER_DETAILS +" WHERE " + DatabaseHelper.COL_USER_UNIQUE_ID +" = " + userID,null );

        cursor.moveToFirst();
        if (cursor != null && cursor.getCount()>0){

            String fullName = cursor.getString( cursor.getColumnIndex( DatabaseHelper.COL_USER_FULL_NAME ) );
            String name  = cursor.getString( cursor.getColumnIndex( DatabaseHelper.COL_USER_NAME ) );
            String password = cursor.getString( cursor.getColumnIndex( DatabaseHelper.COL_USER_PASSWORD ) );
            String avatar = cursor.getString( cursor.getColumnIndex( DatabaseHelper.COL_USER_AVATAR ) );
            String email = cursor.getString( cursor.getColumnIndex( DatabaseHelper.COL_USER_EMAIL ) );
            String role = cursor.getString( cursor.getColumnIndex( DatabaseHelper.COL_USER_ROLE ) );
            String doctorId = cursor.getString( cursor.getColumnIndex( DatabaseHelper.COL_USER_DOCTOR_ID ) );

            userModel = new UserModel( userID,fullName, name,avatar,email,password,role,doctorId);
            cursor.moveToNext();
        }
        cursor.close();
        this.close();
        return userModel;
    }


    public ArrayList<TaskModel> getMyAllTask(String userId) {
        ArrayList<TaskModel> taskModelArrayList = new ArrayList<>();
        this.open();
        /*Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DatabaseHelper.TABLE_USER_DETAILS,null);*/

        String myOrder = DatabaseHelper.COL_TASK_TIME+" ASC";

//        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_AD_POST, null, null, null, null, null, myOrder, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_USER_TASK+" WHERE "+DatabaseHelper.COL_TASK_USER_ID+" = "+userId +" ORDER BY "+myOrder,null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                int taskID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TASK_ID));
                String taskUniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_UNIQUE_ID));
                String userUniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_USER_ID));
                String taskTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_TITLE));
                String taskLocation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_LOCATION));
                String taskTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_TIME));
                String taskPriority = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_PRIORITY));
                int taskCategory = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CATEGORY));
                String taskDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_DESCRIPTION));
                String taskCreatedAt = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CREATED_AT));
                String taskCreatorId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CREATOR_ID));


//                travelEventModel = new MedicalHistoryModel(id,dr_id,prescription,name,details,date);
//                Log.d(TAG, "getMyAllPost ----- Post unique id-------" + postUniqueId);
//                Log.d(TAG, "getMyAllPost ----- Post Name-------" + postTitle);

                taskModel = new TaskModel( taskUniqueId,userUniqueId, taskTitle, taskLocation, Long.parseLong( taskTime ),
                        Integer.parseInt( taskPriority), taskCategory,  taskDescription, Long.parseLong( taskCreatedAt),taskCreatorId);

                taskModelArrayList.add(taskModel);
                cursor.moveToNext();
            }
        }
        cursor.close();
        this.close();
        return taskModelArrayList;

    }



    public ArrayList<TaskModel> getCategoryTask(String userId, String category) {
        ArrayList<TaskModel> taskModelArrayList = new ArrayList<>();
        this.open();
        /*Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DatabaseHelper.TABLE_USER_DETAILS,null);*/

        String myOrder = DatabaseHelper.COL_TASK_TIME+" ASC";

//        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_AD_POST, null, null, null, null, null, myOrder, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_USER_TASK+" WHERE "+DatabaseHelper.COL_TASK_USER_ID+" = "+userId + " AND "+ DatabaseHelper.COL_TASK_CATEGORY+" = "+category+" ORDER BY "+myOrder,null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                int taskID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TASK_ID));
                String taskUniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_UNIQUE_ID));
                String userUniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_USER_ID));
                String taskTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_TITLE));
                String taskLocation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_LOCATION));
                String taskTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_TIME));
                String taskPriority = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_PRIORITY));
                int taskCategory = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CATEGORY));
                String taskDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_DESCRIPTION));
                String taskCreatedAt = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CREATED_AT));
                String taskCreatorId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CREATOR_ID));


//                travelEventModel = new MedicalHistoryModel(id,dr_id,prescription,name,details,date);
//                Log.d(TAG, "getMyAllPost ----- Post unique id-------" + postUniqueId);
//                Log.d(TAG, "getMyAllPost ----- Post Name-------" + postTitle);

                taskModel = new TaskModel( taskUniqueId,userUniqueId, taskTitle, taskLocation, Long.parseLong( taskTime ),
                        Integer.parseInt( taskPriority), taskCategory,  taskDescription, Long.parseLong( taskCreatedAt),taskCreatorId);

                taskModelArrayList.add(taskModel);
                cursor.moveToNext();
            }
        }
        cursor.close();
        this.close();
        return taskModelArrayList;

    }


    public TaskModel getSingleTask(String taskTime) {
        TaskModel taskModel = new TaskModel(  );
        this.open();
        /*Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DatabaseHelper.TABLE_USER_DETAILS,null);*/

//        String myOrder = DatabaseHelper.COL_TASK_TIME+" ASC";

//        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_AD_POST, null, null, null, null, null, myOrder, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_USER_TASK+" WHERE "+DatabaseHelper.COL_TASK_TIME+" = "+taskTime +" ;",null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
                int taskID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TASK_ID));
                String taskUniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_UNIQUE_ID));
                String userUniqueId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_USER_ID));
                String taskTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_TITLE));
                String taskLocation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_LOCATION));
                String taskPriority = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_PRIORITY));
                String taskDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_DESCRIPTION));
                String taskCreatedAt = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CREATED_AT));
                String taskCreatorId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CREATOR_ID));
                int taskCategory = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TASK_CATEGORY));


//                travelEventModel = new MedicalHistoryModel(id,dr_id,prescription,name,details,date);
//                Log.d(TAG, "getMyAllPost ----- Post unique id-------" + postUniqueId);
//                Log.d(TAG, "getMyAllPost ----- Post Name-------" + postTitle);

                taskModel = new TaskModel(taskUniqueId, userUniqueId, taskTitle, taskLocation, Long.parseLong( taskTime ),
                        Integer.parseInt( taskPriority),taskCategory,  taskDescription, Long.parseLong( taskCreatedAt),taskCreatorId);

                cursor.moveToNext();

        }
        cursor.close();
        this.close();
        return taskModel;

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

    public boolean deletePreviousTask(String taskId) {
        Log.d( TAG, "deletePreviousTask: start " );
        this.open();
        try {

            Log.d( TAG, "deletePreviousTask: enter try" );
            long id = sqLiteDatabase.delete( DatabaseHelper.TABLE_USER_TASK,DatabaseHelper.COL_TASK_UNIQUE_ID + " = ? ", new String[]{taskId} );
            Log.d( TAG, "deletePreviousTask: id----> "+String.valueOf( id ) );
            this.close();
            if (id > 0) {
                return true;
            } else {
                return false;
            }
        }catch (SQLException e){
            Log.d(TAG,"deletePreviousTask catch -->  "+e.toString());
            return false;
        }finally {
            sqLiteDatabase.close();
        }
    }
    public boolean updateDatabase(){
        long currentDateTime = Calendar.getInstance().getTimeInMillis();
        this.open();
        String selectQuery = "SELECT "+ DatabaseHelper.COL_TASK_UNIQUE_ID +" FROM " + DatabaseHelper.TABLE_USER_TASK + " WHERE "+ DatabaseHelper.COL_TASK_TIME +" < "
                +currentDateTime;
        Log.d( TAG, "updateDatabase: Select Query -->"+selectQuery );
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
            Log.d( TAG, "updateDatabase: cursor --> "+String.valueOf( cursor.getCount() ) );
            cursor.moveToFirst();
            if (cursor!=null && cursor.getCount() > 0) {

                for (int i=0; i<cursor.getCount(); i++){
                    String taskID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TASK_UNIQUE_ID));
                    if (deletePreviousTask( taskID )){
                        Log.d( TAG, "updateDatabase: delete task id ---> "+ taskID);

                    }

                    cursor.moveToNext();
                }

                return true;
            }else {
                Log.d( TAG, "updateDatabase: cursor else task not found"  );
                return false;
            }

            }catch (Exception e){
            Log.d( TAG, "updateDatabase: exception " +e.getLocalizedMessage());
            return false;

        }
//        return false;
    }

    public boolean whenLogoutUser() {
        this.open();
        sqLiteDatabase.delete(DatabaseHelper.TABLE_USER_DETAILS, null, null);
        sqLiteDatabase.delete( DatabaseHelper.TABLE_USER_TASK,null,null );
        this.close();
        Log.d(TAG, "Deleted all Table info from SQLite");
        return true;
    }


}
