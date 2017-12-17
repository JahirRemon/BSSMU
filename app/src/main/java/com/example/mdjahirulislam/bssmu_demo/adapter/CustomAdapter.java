package com.example.mdjahirulislam.bssmu_demo.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
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
    SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
    SimpleDateFormat time = new SimpleDateFormat( "hh:mm aa" );
    Calendar calendar = Calendar.getInstance();
    Calendar toDayCalendar = Calendar.getInstance();
    int count = 0;
    boolean status;
    private Context context;

    public CustomAdapter(Context context, ArrayList<TaskModel> users) {
        super( context, 0, users );
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        // Get the data item for this position

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.single_item_view, parent, false );
            holder = new ViewHolder();
            holder.tvTaskToDay =(TextView) convertView.findViewById( R.id.toDay );
            holder.tvTaskName = (TextView) convertView.findViewById( R.id.taskNameTV );
            holder.tvLocation = (TextView) convertView.findViewById( R.id.taskLocationTV );
            holder.tvTaskDescription = (TextView) convertView.findViewById( R.id.taskDescriptionTV );
            holder.tvTaskTime = (TextView) convertView.findViewById( R.id.taskTimeTV );
            holder.tvTaskPriority = (TextView) convertView.findViewById( R.id.taskPriorityTV );
            holder.mainLL = (LinearLayout) convertView.findViewById( R.id.singleListLL );
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();


        }
        // Lookup view for data population
        TaskModel taskModel = getItem( position );
        // Populate the data into the template view using the data object
        calendar.setTimeInMillis( taskModel.getTaskTime() );
        holder.tvTaskName.setText( taskModel.getTaskName() );
        holder.tvLocation.setText( "Location : " + taskModel.getTaskLocation() );
        holder.tvTaskTime.setText( "  "+time.format( calendar.getTimeInMillis() ) );
        holder.tvTaskDescription.setText( taskModel.getDescription() );

        int priority = taskModel.getPriority();
        if (priority == 1) {
            holder.tvTaskPriority.setText( "High Priority" );
            holder.tvTaskPriority.setBackground( context.getResources().getDrawable( R.drawable.high_prority_background ) );
        } else if (priority == 2) {
            holder.tvTaskPriority.setText( "Normal Priority" );
            holder.tvTaskPriority.setBackground( context.getResources().getDrawable( R.drawable.normal_proriry_background ) );
        } else if (priority == 3) {
            holder.tvTaskPriority.setText( "Low Priority" );
            holder.tvTaskPriority.setBackground( context.getResources().getDrawable( R.drawable.low_prority_background ) );
        }
//        Log.d( "adapter", "getView: " );
        if (Utilities.isItToday( calendar.getTimeInMillis() )) {
            if (status) {
                holder.tvTaskToDay.setVisibility( View.GONE );
            }else {

                holder.tvTaskToDay.setVisibility( View.VISIBLE );
                holder.tvTaskToDay.setText( "To Day" );
                status = true;
            }
            holder.mainLL.setBackgroundColor( context.getResources().getColor( R.color.toDayBackgroundColor ) );
        } else {
            holder.tvTaskToDay.setVisibility( View.VISIBLE );
            holder.tvTaskToDay.setText( sdf.format( calendar.getTimeInMillis() ) );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mainLL.setBackground( context.getDrawable( R.drawable.home_item_background ) );
            }
        }
        notifyDataSetChanged();
        // Return the completed view to render on screen
        return convertView;
    }

     static class ViewHolder {
        private TextView tvTaskToDay;
        private TextView tvTaskName;
        private TextView tvLocation;
        private TextView tvTaskDescription;
        private TextView tvTaskTime;
        private TextView tvTaskPriority;
        private LinearLayout mainLL;
    }
}

