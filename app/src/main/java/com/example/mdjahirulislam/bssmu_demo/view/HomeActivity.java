package com.example.mdjahirulislam.bssmu_demo.view;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.database.AppData;
import com.example.mdjahirulislam.bssmu_demo.database.DatabaseSource;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout appintmentLL;
    private LinearLayout taskLL;
    private LinearLayout libraryLL;
    private LinearLayout ebookLL;
    private AppData appData;
    private DatabaseSource db;

    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        taskLL = findViewById( R.id.taskLL );
        libraryLL = findViewById( R.id.libraryLL );
        setSupportActionBar( toolbar );
        db = new DatabaseSource( this );
        appData = new AppData( this );
        myTTS = new TextToSpeech( getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    myTTS.setLanguage( Locale.UK);
                    myTTS.setPitch(  .6f );
                    myTTS.setSpeechRate( .9f );
                }
            }
        } );


        if (appData.getUserId().equals( "null" )){
            startActivity( new Intent( HomeActivity.this,LoginActivity.class ) );
            finish();
        }else {
            Log.d( TAG, "onCreate: " + appData.getUserId() );
        }


//        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
//        fab.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
//                        .setAction( "Action", null ).show();
//            }
//        } );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.home, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        noinspection SimplifiableIfStatement
        if (id == R.id.logOut) {
            if (appData.logout()){
                boolean status = db.whenLogoutUser();
                startActivity( new Intent( HomeActivity.this, LoginActivity.class ) );
            }
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    public void goToTaskActivity(View view) {

        startActivity( new Intent( HomeActivity.this,LoginActivity.class ) );


    }

    public void testToSpeech(View view) {

        startActivity( new Intent( HomeActivity.this,AppointmentActivity.class ) );
    }

    public void goToLibraryActivity(View view) {
        startActivity( new Intent( HomeActivity.this,LibraryActivity.class ) );
    }

    public void gotoMeetingActivity(View view) {

        startActivity( new Intent( HomeActivity.this,MeetingActivity.class ) );
    }

    public void goToOperationActivity(View view) {
        startActivity( new Intent( HomeActivity.this,OperationActivity.class ) );
    }

    public void goToEbookActivity(View view) {
        startActivity( new Intent( HomeActivity.this,BookReaderActivity.class ) );
    }
}
