package com.example.a0134598r.pathfinder.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.GV;
import com.example.a0134598r.pathfinder.activities.PatientRegistration;
import com.example.a0134598r.pathfinder.activities.QueueActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;

/**
 * Created by jixiang on 1/9/15.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {


    private Activity activity;
    private View view;
    private TextView addressV;
    private TextView estateV;
    private ImageView image;

    EditText finNumber;
    Button submitButton;
    Button clearButton;
    Button backButton;
    Button registerButton;

    String clinicId,ic_num;
    String clinicName;
    LatLng destination;

    Context appContext;


    public CustomInfoWindowAdapter(Activity activity, Context appContext) {
        this.activity = activity;
        this.appContext = appContext;
        view = activity.getLayoutInflater().inflate(R.layout.custom_info_window,
                null);
        image = ((ImageView) view.findViewById(R.id.badge));
        addressV = ((TextView) view.findViewById(R.id.title));
        estateV = ((TextView) view.findViewById(R.id.snippet));

    }

    @Override
    public View getInfoContents(Marker marker) {

        if (marker != null
                && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {

        final String address1 = marker.getTitle();
//            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (address1 != null) {
            addressV.setText(address1);
        } else {
            addressV.setText("");
        }

        final String estate = marker.getSnippet();

        if (estate != null) {
            estateV.setText(estate);
        } else {
            estateV.setText("");
        }

        new DownloadImageTask(image)
                .execute("https://www.google.com.sg/images/srpr/logo11w.png");

        getInfoContents(marker);
        return view;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_custom_dialog);
        // Set dialog title
        dialog.setTitle(clinicName);
        ImageView image1 = (ImageView) dialog.findViewById(R.id.card_1);
        ImageView image2 = (ImageView) dialog.findViewById(R.id.card_2);
        ImageView image3 = (ImageView) dialog.findViewById(R.id.card_3);
        ImageView image4 = (ImageView) dialog.findViewById(R.id.card_4);
        image1.setImageResource(R.mipmap.pioneer_card);
        image2.setImageResource(R.mipmap.msig_card);
        image3.setImageResource(R.mipmap.prudential_card);
        image4.setImageResource(R.mipmap.aviva_card);


        dialog.show();

        final Button RequestButton = (Button) dialog.findViewById(R.id.request_btn);
        // if decline button is clicked, close the custom dialog
        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                final Dialog dialog2 = new Dialog(activity);
                dialog2.setContentView(R.layout.activity_custom_dialog_que);
                // Set dialog title

                finNumber = (EditText) dialog2.findViewById(R.id.fin);
                submitButton = (Button) dialog2.findViewById(R.id.queSubmit);
                clearButton = (Button) dialog2.findViewById(R.id.queClear);
                backButton = (Button) dialog2.findViewById(R.id.queBack);


                dialog2.setTitle("Request for Queue Number");
                //Create touch listeners for all buttons.
                submitButton.setOnClickListener(new View.OnClickListener() {

                    Intent i;
                    @Override
                    public void onClick(View v) {
                        ic_num = finNumber.getText().toString();
                        GoogleCloudMessaging gcm = null;
                        String regid = "";
                        String projectNumber = GV.PROJECT_NUMBER;
                        i = new Intent(activity.getApplicationContext(), QueueActivity.class);
                        if (ic_num.isEmpty()) {
                            Toast.makeText(activity.getApplicationContext(), "Please fill in your correct IC Number!!!", Toast.LENGTH_SHORT).show();
                        } else if(ic_num.matches("[A-Z][0-9]{8}[A-Z]")) {

                            String msg = "";
                            try {
                                if (gcm == null) {
                                    gcm = GoogleCloudMessaging.getInstance(activity.getApplicationContext());
                                }
                                regid = gcm.register(projectNumber);
                                msg = "Device registered, registration ID=" + regid;
                                Log.i("GCM", msg);

                            } catch (IOException ex) {
                                msg = "Error :" + ex.getMessage();
                            }

                            i.putExtra("reg_id", regid);


                            i.putExtra("clinic_name", clinicName);
                            i.putExtra("IC_Number", ic_num);
                            activity.startActivity(i);

                            dialog2.dismiss();
                        }else {
                            Toast.makeText(activity.getApplication(),"Please enter the correct NRIC/FIN",Toast.LENGTH_SHORT).show();
                        }
                    }


                });

                clearButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        finNumber.setText("");

                    }
                });

                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog2.dismiss();

                    }
                });

                dialog2.show();
                dialog.dismiss();
            }
        });

        Button RouteButton = (Button) dialog.findViewById(R.id.route_btn);
        RouteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Close dialog
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destination.latitude + "," + destination.longitude + "");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
                dialog.dismiss();
            }
        });

        Button registerButton = (Button) dialog.findViewById(R.id.register_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                Intent i;
                i = new Intent(activity.getApplicationContext(), PatientRegistration.class);
                i.putExtra("clinic_name", clinicName);
                activity.startActivity(i);
                dialog.dismiss();

            }
        });

    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }
}