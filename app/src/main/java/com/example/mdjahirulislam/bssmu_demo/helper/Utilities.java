package com.example.mdjahirulislam.bssmu_demo.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.service.AlarmReceiver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mdjahirulislam on 14/12/17.
 */

public class Utilities {


    public static String BASE_URL = "https://doctors.com.bd/doctors/";
    public static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(Utilities.BASE_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
    }

    public static boolean setAlarm(Context context, long alarmTimeInMills, String alarmId, String taskID) {
        Log.d( "utility ", "setAlarm: "+taskID );
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( alarmTimeInMills );
        calendar.set(Calendar.SECOND,00);
        if (isItToday( calendar.getTimeInMillis() )) {
            if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
                Intent alarmIntent = new Intent( context, AlarmReceiver.class );
                alarmIntent.putExtra( "taskTime", alarmTimeInMills );
                PendingIntent pendingIntent = PendingIntent.getBroadcast( context, Integer.parseInt( alarmId ), alarmIntent, PendingIntent.FLAG_ONE_SHOT );
                AlarmManager manager = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
                manager.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent );
                Log.d( "set-Alarm", "Alarm set at" + calendar.getTime() );
                return true;
            } else {
                Log.d( "set-Check_current", "Alarm time is lower then present time - " + calendar.getTime() );
                return false;
            }
        } else {
            Log.d( "set-ToDayCheck", "Alarm time is not today - " + calendar.getTime() );
            return false;
        }

    }

    public static boolean isItToday(long dateInMills) {

        Calendar calendar=Calendar.getInstance();
        Calendar calendar2=Calendar.getInstance();
        calendar2.setTimeInMillis(dateInMills);

        if (calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)) {
            return true;
        }else if (System.currentTimeMillis() < calendar2.getTimeInMillis()){
            Log.d( "Utility ", "isItToday: future" );
            return false;
        }else {
            Log.d( "Utility ", "isItToday: past" );
            return false;
        }
    }
//2017-12-18 02:19:12","doctors_id":542,"creator_id":542}

    public static long stringDateTimeToMills(String dateTime) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(dateTime);
        long millis = date.getTime();
        return millis;
    }

    public static String dateTimeFormation(long timeInMills){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis( timeInMills );
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateString=simpleDateFormat.format(calendar.getTime());
        return dateString.toLowerCase();

    }
    public static String dateFormation(long timeInMills){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis( timeInMills );
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String dateString=simpleDateFormat.format(calendar.getTime());
        return dateString.toLowerCase();

    }
    public static String timeFormation(long timeInMills){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis( timeInMills );
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
        String dateString=simpleDateFormat.format(calendar.getTime());
        return dateString.toLowerCase();

    }



    public static void CopyReadAssets(Context context)
    {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(context, "External Storage is not Available", Toast.LENGTH_SHORT).show();
        }
        File pdfDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFs");
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }
        File file = new File(pdfDir + "/BD_Human_Anatomy_VL_1.pdf");

        try
        {
            in = assetManager.open("BD_Human_Anatomy_VL_1.pdf");
            out = new BufferedOutputStream(new FileOutputStream(file));
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }
        if (file.exists()) //Checking for the file is exist or not
        {
            Uri path = Uri.fromFile(file);
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
//            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent intent1 = Intent.createChooser(objIntent, "Open PDF with..");
            try {
                context.startActivity(intent1);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Activity Not Found Exception ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "The file not exists! ", Toast.LENGTH_SHORT).show();
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }



}
