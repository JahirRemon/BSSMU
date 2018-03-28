package com.example.mdjahirulislam.bssmu_demo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mdjahirulislam.bssmu_demo.R;
import com.example.mdjahirulislam.bssmu_demo.adapter.CustomBookAdapter;
import com.example.mdjahirulislam.bssmu_demo.helper.Utilities;
import com.example.mdjahirulislam.bssmu_demo.model.BookItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    private ListView bookLV;
    private Spinner categorySP;
    private CustomBookAdapter customBookAdapter;
    private ArrayList<String> spinnerItem;
    private ArrayAdapter<String> stringArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_library );
        bookLV = findViewById( R.id.bookList );
        categorySP = findViewById( R.id.spinner );

        spinnerItem = new ArrayList<>(  );
        spinnerItem.add( "Please Select Category" );
        spinnerItem.add( "All Books" );
        spinnerItem.add( "Terminology" );
        spinnerItem.add( "Cancer" );
        spinnerItem.add( "Ebola Virus" );
        spinnerItem.add( "Brain" );
        spinnerItem.add( "Diagnosis" );
        spinnerItem.add( "Gynecology" );
        spinnerItem.add( "Physiology" );
        stringArrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item,spinnerItem );
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySP.setAdapter( stringArrayAdapter );


        categorySP.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectCategory = categorySP.getSelectedItem().toString();
                Log.d( "remon", "onItemSelected: "+selectCategory );
                ArrayList<BookItem> resultList = new ArrayList<>(  );
                for (BookItem bookItem :
                        getBookItems()) {
                    if (selectCategory.toLowerCase().equals( bookItem.getCategory().toLowerCase() )){
                        resultList.add( bookItem );
                    }

                }

                if (resultList.size()>0){
                    customBookAdapter = new CustomBookAdapter( LibraryActivity.this,resultList );
                    bookLV.setAdapter( customBookAdapter );
                }
                if (selectCategory.toLowerCase().equals( "all books".toLowerCase() )){
                    customBookAdapter = new CustomBookAdapter( LibraryActivity.this,getBookItems() );
                    bookLV.setAdapter( customBookAdapter );
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
        customBookAdapter = new CustomBookAdapter( LibraryActivity.this,getBookItems() );
        bookLV.setAdapter( customBookAdapter );

        bookLV.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (getBookItems().get( i ).e_bookStatus.toString().toLowerCase().equals( "Available".toLowerCase() )){
                    Utilities.CopyReadAssets(LibraryActivity.this);
//                    Toast.makeText( LibraryActivity.this, " if Available", Toast.LENGTH_SHORT ).show();
                }else {
                    Toast.makeText( LibraryActivity.this, "E-book is not available", Toast.LENGTH_SHORT ).show();
                }


            }
        } );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("remon......");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                ArrayList<BookItem> resultList = new ArrayList<>(  );
                for (BookItem bookItem :
                        getBookItems()) {
                    if (query.toLowerCase().equals( bookItem.getCategory().toLowerCase() )){
                        resultList.add( bookItem );
                    }

                }

                if (resultList.size()>0){
                    customBookAdapter = new CustomBookAdapter( LibraryActivity.this,resultList );
                    bookLV.setAdapter( customBookAdapter );
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null & !newText.isEmpty()){
                    Log.d( "remon", "onQueryTextChange: "+newText );

//                    Toast.makeText( LibraryActivity.this, newText, Toast.LENGTH_SHORT ).show();
                    ArrayList<BookItem> bookResult = new ArrayList<>(  );
                    for (BookItem book :
                            getBookItems()) {

                        if (book.bookName.contains( newText ) ){


                            bookResult.add( book );
                        }
                    }
                    customBookAdapter = new CustomBookAdapter( LibraryActivity.this,bookResult );
                    bookLV.setAdapter( customBookAdapter );
                }else {
                    Log.d( "adp", "onQueryTextChange: " );
                    bookLV.deferNotifyDataSetChanged();
                }

                return false;
            }
        });

        return true;
    }

    public ArrayList<BookItem> getBookItems() {
        ArrayList<BookItem> arrayList = new ArrayList<>();

        arrayList.add(new BookItem("Human Anatomy - Upper Limb  &amp; Thorax (Volume 1) ","Rebecca Skloot ",
                "6th edition","terminology","Available",R.drawable.human_anatomy));

        arrayList.add(new BookItem("Stiff: The Curious Lives of Human Cadavers","Mary Roach ",
                "9th edition","terminology","Available",R.drawable.images_3));

        arrayList.add(new BookItem("Complications: A Surgeon's Notes on an Imperfect Science ","Atul Gawande",
                "11th edition","terminology","Not Available",R.drawable.images_4));

        arrayList.add(new BookItem("The Emperor of All Maladies: A Biography of Cancer ","Siddhartha Mukherjee ",
                "8th edition","Cancer","Not Available",R.drawable.images_5));

        arrayList.add(new BookItem("Medical Terminology - A Body Systems Approach","Barbara Gylys & F A Davis",
                "6th edition","terminology","Available"));

        arrayList.add(new BookItem("Being Mortal: Medicine and What Matters in the End  ","Atul Gawande",
                "3rd edition","Medicine","Available"));

        arrayList.add(new BookItem("The Hot Zone: The Terrifying True Story of the Origins of the Ebola Virus","Richard Preston",
                "9th edition","Ebola Virus","Available"));

        arrayList.add(new BookItem("When the Air Hits Your Brain: Tales of Neurosurgery","Frank T. Vertosick Jr.",
                "13th edition","Brain","Available"));

        arrayList.add(new BookItem("Every Patient Tells a Story: Medical Mysteries and the Art of Diagnosis","Lisa Sanders",
                "10th edition","Diagnosis","Not Available"));

        arrayList.add(new BookItem("100 Cases Histories in Clinical Medicine for MRCP (Part-1)","Farrukh Iqbal",
                "4th edition","terminology","Available"));

        arrayList.add(new BookItem("A Handbook of Prenatal Diagnosis Reproductive Genetics","Kamini A Rao",
                "5th edition","Diagnosis","Available"));

        arrayList.add(new BookItem("A Practical Guide to Obstetrics & Gynecology","Richa Saxena",
                "14th edition","Gynecology","Available"));

        arrayList.add(new BookItem("A Textbook of Practical Physiology","CL Ghai ",
                "11th edition","Physiology","Available"));

        return arrayList;
    }
}
