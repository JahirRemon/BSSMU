package com.example.mdjahirulislam.bssmu_demo.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mdjahirulislam.bssmu_demo.R;

public class AppointmentActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_appointment );
    }

    public void addReminder(View view) {
        Log.d( TAG, "addReminder: " );
    }
}
