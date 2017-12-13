package com.example.mdjahirulislam.bssmu_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mdjahirulislam on 11/12/17.
 */

public class CustomAdapter extends ArrayAdapter<TaskModel> {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a dd/MM/yyyy");
    Calendar calendar = Calendar.getInstance();
    public CustomAdapter(Context context, ArrayList<TaskModel> users) {
        super( context, 0, users );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        TaskModel taskModel = getItem( position );
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.single_item_view, parent, false );
        }
        // Lookup view for data population
        TextView tvTaskName = (TextView) convertView.findViewById( R.id.taskNameTV );
        TextView tvLocation = (TextView) convertView.findViewById( R.id.taskLocationTV );
        TextView tvTaskTime = (TextView) convertView.findViewById( R.id.taskTimeTV );
        // Populate the data into the template view using the data object
        calendar.setTimeInMillis( taskModel.getTaskTime() );
        tvTaskName.setText( taskModel.getTaskName() );
        tvLocation.setText( "Location : "+taskModel.getTaskLocation() );
        tvTaskTime.setText( sdf.format( calendar.getTimeInMillis() ));
        // Return the completed view to render on screen
        return convertView;
    }
}

