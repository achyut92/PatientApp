package com.example.a0134598r.pathfinder.obsolete;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a0134598r.pathfinder.R;

public class CustomDialogQue extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog_que);

        final Dialog dialog2 = new Dialog(CustomDialogQue.this);
        dialog2.setContentView(R.layout.activity_custom_dialog_que);
        // Set dialog title

        final EditText finNumber = (EditText) findViewById(R.id.fin);
        Button submitButton = (Button) findViewById(R.id.queSubmit);
        Button clearButton = (Button) findViewById(R.id.queClear);
        Button backButton = (Button) findViewById(R.id.queBack);

        dialog2.setTitle("Custom Dialog");
        //Create touch listeners for all buttons.
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog2.dismiss();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finNumber.setText("");
                dialog2.dismiss();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog2.dismiss();

            }
        });


        dialog2.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_dialog_que, menu);
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
