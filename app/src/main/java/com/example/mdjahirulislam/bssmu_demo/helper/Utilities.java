package com.example.mdjahirulislam.bssmu_demo.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.service.AlarmReceiver;

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


    public static void CopyReadAssets(Context context){
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;

//        File file = new File(context.getFilesDir(), "Rebecca_Skloot_The_Immortal_Life_of_Henrietta_Lack.pdf");
        File file = new File(context.getExternalFilesDir( Environment.getExternalStorageState()), "Rebecca_Skloot_The_Immortal_Life_of_Henrietta_Lack.pdf");
        Log.d( "remon ", "CopyReadAssets: " +file.getPath().toString());
        try
        {

//            in = assetManager.open(filename);
//            String outDir = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//            File outFile = new File(outDir, filename);
//            if(outFile.createNewFile()){
//                return;
//            }
//
//            out = new FileOutputStream(outFile);
//


            in = assetManager.open("Rebecca_Skloot_The_Immortal_Life_of_Henrietta_Lack.pdf");
            String outDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File outFile = new File(outDir, "Rebecca_Skloot_The_Immortal_Life_of_Henrietta_Lack.pdf");
            if(outFile.createNewFile()){
                Log.d( "if-----> ", "CopyReadAssets: " );
                return;
            }
            out = new FileOutputStream(outFile);
//            Log.d( "Tag 1", "CopyReadAssets: in ---> "+in.toString() );
//            out = context.openFileOutput(file.getName(), Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
//            Log.d( "Tag 2", "CopyReadAssets: in ---> "+out.toString() );

            copyFile(in, out);
            Log.d( "Tag 3", "CopyReadAssets: in ---> " +outFile.getPath());
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage()+"----> "+e.getLocalizedMessage());
        }

        Intent intent = new Intent( Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//            Log.d( "Remon", "CopyReadAssets: " );
//            Uri apkURI = FileProvider.getUriForFile( context,
//                    context.getApplicationContext().getPackageName() + ".provider", file);
            intent.setDataAndType(apkURI, "application/pdf");
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            intent.setDataAndType(
                    Uri.parse("file://" + context.getFilesDir() + "/Rebecca_Skloot_The_Immortal_Life_of_Henrietta_Lack.pdf"),
                    "application/pdf");
        }


//        intent.getData();
        context.startActivity(intent);
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
