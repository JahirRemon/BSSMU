package com.example.mdjahirulislam.bssmu_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;
import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mdjahirulislam on 11/12/17.
 */

public class CustomAdapter extends ArrayAdapter<TaskModel> {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd/MM/yyyy");
    Calendar calendar = Calendar.getInstance();
    private Context context;
    public CustomAdapter(Context context, ArrayList<TaskModel> users) {
        super( context, 0, users );
        this.context = context;
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
        LinearLayout mainLL = (LinearLayout) convertView.findViewById( R.id.singleListLL );
        // Populate the data into the template view using the data object
        calendar.setTimeInMillis( taskModel.getTaskTime() );
        tvTaskName.setText( taskModel.getTaskName() );
        tvLocation.setText( "Location : "+taskModel.getTaskLocation() );
        tvTaskTime.setText( sdf.format( calendar.getTimeInMillis() ));

        if (Utilities.isItToday( calendar.getTimeInMillis() )){
            mainLL.setBackgroundColor( context.getResources().getColor( R.color.toDayColor ) );
        }
        // Return the completed view to render on screen
        return convertView;
    }
}

