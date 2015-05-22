package com.example.a0134598r.pathfinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.activities.FindClinicCurrent;
import com.example.a0134598r.pathfinder.activities.FindClinicEstateName;
import com.example.a0134598r.pathfinder.utils.GooglePlacesAutocompleteActivity;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SearchScreen extends Activity {

    RadioGroup rg;
    ArrayAdapter<String> adpt;

    RadioButton estate_name,specific_loc,rb,current_loc;
    AutoCompleteTextView estateText;
    Button find;
    ArrayList<String> neighSpin;
    List<String> neighList;
    Set<String> hset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_clinic);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "dQwUFTzXnMpimfW5Wh8G1J8GLkEXm24JXuUe8pWO", "XrKANWyNzclmbdAIFbVqkJbRdleBT2PPw5zWja5s");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL,true);

        neighSpin = new ArrayList<String>();


        InitialiseWidget();

        RadioListener();

        ButtonListener();





    }

    private void retrieveFromCloud() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ZoneEstate");
       query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
               if(parseObjects!=null){
                   for(ParseObject obj : parseObjects){
                       String estate = obj.getString("ESTATE");
                       neighSpin.add(estate);
                   }

               }else {
                   Log.i("Error",e.getMessage());
               }

           }
       });




    }

    private void ButtonListener() {
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rg.getCheckedRadioButtonId();
                if(selectedId==current_loc.getId()){
                    Intent i = new Intent(getApplicationContext(),FindClinicCurrent.class);
                    startActivity(i);
                }
                if(selectedId==specific_loc.getId()){

                    Intent i = new Intent(getApplicationContext(),GooglePlacesAutocompleteActivity.class);
                    startActivity(i);
                }if(selectedId==estate_name.getId()){
                    Intent i = new Intent(getApplicationContext(),FindClinicEstateName.class);
                    i.putExtra("estate_name",estateText.getText().toString());
                    startActivity(i);

                }

            }
        });
    }

    private void RadioListener() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton)findViewById(checkedId);

                if(rb.getId()==estate_name.getId()){
                    estateText.setVisibility(View.VISIBLE);



                }
                else if(rb.getId()==specific_loc.getId()){




                    estateText.setVisibility(View.GONE);


                }
                else{

                    estateText.setVisibility(View.GONE);

                }
            }

        });
    }

    private void InitialiseWidget() {
        rg  = (RadioGroup)findViewById(R.id.grp_region);

        estateText = (AutoCompleteTextView)findViewById(R.id.estateText);
        estate_name = (RadioButton)findViewById(R.id.estate_name);
        current_loc = (RadioButton)findViewById(R.id.current_loc);
        current_loc.isChecked();
        specific_loc =(RadioButton)findViewById(R.id.specific_loc);
        find = (Button)findViewById(R.id.find);
        retrieveFromCloud();
        adpt = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,neighSpin);
        estateText.setAdapter(adpt);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
