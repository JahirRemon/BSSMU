package com.example.mdjahirulislam.bssmu_demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;
import com.example.mdjahirulislam.bssmu_demo.model.BookItem;
import com.example.mdjahirulislam.bssmu_demo.model.TaskModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mdjahirulislam on 15/12/17.
 */

public class CustomBookAdapter extends ArrayAdapter<BookItem> {
    SimpleDateFormat sdf = new SimpleDateFormat( "hh:mm a dd/MM/yyyy" );
    Calendar calendar = Calendar.getInstance();
    private Context context;

    public CustomBookAdapter(Context context, ArrayList<BookItem> books) {
        super( context, 0, books );
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        BookItem bookItem = getItem( position );
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.single_book_list_item, parent, false );
        }
        // Lookup view for data population
        TextView tvBookName = (TextView) convertView.findViewById( R.id.book_name );
        TextView tvAuthor = (TextView) convertView.findViewById( R.id.author_name );
        TextView tvEdition = (TextView) convertView.findViewById( R.id.edition );
        TextView tvCategory = (TextView) convertView.findViewById( R.id.category );
//        TextView tvStatus = (TextView) convertView.findViewById( R.id.status );
        ImageView ivUrl = (ImageView) convertView.findViewById( R.id.eBookStatusIV );
        ImageView ivUBookrl = (ImageView) convertView.findViewById( R.id.bookImageIV );
//        LinearLayout mainLL = (LinearLayout) convertView.findViewById( R.id.singleListLL );
        // Populate the data into the template view using the data object
//        calendar.setTimeInMillis( bookItem.getTaskTime() );
        tvBookName.setText( bookItem.getBookName() );
        tvAuthor.setText( bookItem.getAuthorName() );
        tvEdition.setText( bookItem.getEdition() );
        tvCategory.setText( bookItem.getCategory() );
//        tvStatus.setText( bookItem.getE_bookStatus() );
        if (bookItem.getPath() == 0){
            ivUBookrl.setImageResource( R.drawable.booknocoverimage );
        }else {
            ivUBookrl.setImageResource( bookItem.getPath() );
        }

        Log.d( "book_adapter", "getView: "+bookItem.getPath() );
        if (bookItem.getE_bookStatus().equals( "Available" )){
            ivUrl.setImageResource( R.drawable.confirm );
        }else {
            ivUrl.setImageResource( R.drawable.not_confirm_border );
        }


//        if (Utilities.isItToday( calendar.getTimeInMillis() )){
//            mainLL.setBackgroundColor( context.getResources().getColor( R.color.toDayColor ) );
//        }
        // Return the completed view to render on screen
        return convertView;
    }
}