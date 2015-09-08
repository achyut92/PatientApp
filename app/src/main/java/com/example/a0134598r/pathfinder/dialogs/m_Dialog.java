package com.example.a0134598r.pathfinder.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.ButtonHelper;
import com.example.a0134598r.pathfinder.System.GV;
import com.example.a0134598r.pathfinder.activities.PatientRegistration;
import com.example.a0134598r.pathfinder.activities.QueueActivity;
import com.example.a0134598r.pathfinder.guiModels.QueueSubmit;
import com.example.a0134598r.pathfinder.models.Clinic;
import com.example.a0134598r.pathfinder.models.Place;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by jixiang on 3/9/15.
 */
public class m_Dialog extends Dialog {
    /**
     * Create a Dialog window that uses the default dialog frame style.
     *
     * @param context The Context the Dialog is to run it.  In particular, it
     * uses the window manager and theme in this context to
     * present its UI.
     */


    final Button RequestButton = (Button) this.findViewById(R.id.request_btn);
    final Button RegisterButton = (Button) this.findViewById(R.id.register_btn);
    final Button RouteButton = (Button) this.findViewById(R.id.route_btn);
    ButtonHelper bh;
    QueueSubmit qs;

    EditText finNumber;
    Button submitButton;
    Button clearButton;
    Button backButton;

    String ic_num;

    Context context;


    public m_Dialog(final Context c, final Clinic clinic, final Place place) {
        super(c);

        context = c;
        bh = new ButtonHelper();
        qs = qs;
        this.setContentView(R.layout.activity_custom_dialog);
        // Set dialog title
        this.setTitle("Clinic Name");
        ImageView image1 = (ImageView) findViewById(R.id.card_1);
        ImageView image2 = (ImageView) findViewById(R.id.card_2);
        ImageView image3 = (ImageView) findViewById(R.id.card_3);
        ImageView image4 = (ImageView) findViewById(R.id.card_4);
        image1.setImageResource(R.mipmap.pioneer_card);
        image2.setImageResource(R.mipmap.msig_card);
        image3.setImageResource(R.mipmap.prudential_card);
        image4.setImageResource(R.mipmap.aviva_card);

        this.show();


        final Button RequestButton = (Button) findViewById(R.id.request_btn);
        // if decline button is clicked, close the custom dialog
        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                final Dialog dialog2 = new submit_Dialog(context, m_Dialog.this,clinic);
            }
        });

        Button RouteButton = (Button) findViewById(R.id.route_btn);
        RouteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Close dialog
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place.getLatLng().latitude + "," + place.getLatLng().longitude + "");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
                m_Dialog.this.dismiss();
            }
        });

        Button registerButton = (Button)findViewById(R.id.register_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                Intent i;
                i = new Intent(context.getApplicationContext(), PatientRegistration.class);
                i.putExtra("clinic_name", clinic.getCLINIC());
                context.startActivity(i);
                m_Dialog.this.dismiss();

            }
        });
    }
}
