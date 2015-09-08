package com.example.a0134598r.pathfinder.obsolete;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.app.Dialog;
import android.view.View.OnClickListener;

import com.example.a0134598r.pathfinder.R;

public class CustomDialog extends Activity {


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog);


        // Create custom dialog object
        final Dialog dialog = new Dialog(CustomDialog.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.activity_custom_dialog);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button

        ImageView image = (ImageView) dialog.findViewById(R.id.card_2);
        image.setImageResource(R.mipmap.ic_launcher);

        dialog.show();

        Button requestButton = (Button) dialog.findViewById(R.id.request_btn);
        Button routeButton = (Button) dialog.findViewById(R.id.route_btn);
        // if decline button is clicked, close the custom dialog
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        routeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}