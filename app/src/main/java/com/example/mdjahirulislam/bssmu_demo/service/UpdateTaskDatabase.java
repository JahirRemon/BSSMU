package com.example.mdjahirulislam.bssmu_demo.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.database.AppData;
import com.example.mdjahirulislam.bssmu_demo.database.DatabaseSource;
import com.example.mdjahirulislam.bssmu_demo.helper.ConnectionApi;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;


/**
 * Created by mdjahirulislam on 17/12/17.
 */

public class UpdateTaskDatabase extends Thread {
    private final String TAG = UpdateTaskDatabase.class.getSimpleName();

    private ConnectionApi connectionApi;
    private DatabaseSource db;
    private Context context;

    public UpdateTaskDatabase(Context context){
        this.context = context;
        connectionApi = Utilities.getRetrofit().create( ConnectionApi.class );
        db = new DatabaseSource( context );

    }

    @Override
    public void run() {
        super.run();

        Log.d( TAG, "run: Start" );

//        try{
//            Log.d( TAG, "run: Thread sleep 5 second" );
//            Thread.sleep(5000);
//
//        }catch(InterruptedException e){
//            System.out.println(e);
//        }
//        Toast.makeText( context, "Thread sleep 5 second", Toast.LENGTH_SHORT ).show();
        boolean status = db.updateDatabase();
        if (status){
            Log.d( TAG, "run: if database updated" );
        }else {
            Log.d( TAG, "run: else database not updated" );
        }


    }
}
