package com.example.a0134598r.pathfinder.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.BackGroundTask.GetInsuranceCardImgUrlTask;
import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.ButtonHelper;
import com.example.a0134598r.pathfinder.System.GV;
import com.example.a0134598r.pathfinder.activities.PatientRegistration;
import com.example.a0134598r.pathfinder.activities.QueueActivity;
import com.example.a0134598r.pathfinder.guiModels.QueueSubmit;
import com.example.a0134598r.pathfinder.models.Clinic;
import com.example.a0134598r.pathfinder.models.Place;
import com.example.a0134598r.pathfinder.utils.DownloadImageTask;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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

    String url;
    String url_method;

    ArrayList<ImageView> imageBundle;


    public m_Dialog(final Context c, final Clinic clinic, final Place place) {
        super(c);

        context = c;
        bh = new ButtonHelper();
        qs = qs;
        this.setContentView(R.layout.activity_custom_dialog);
        // Set dialog title
        this.setTitle(clinic.getCLINIC());
        initGUIAndParam();

        new GetInsuranceCardImgUrlTask(context,clinic,imageBundle).execute(url+url_method);

//        image1.setImageResource(R.mipmap.pioneer_card);
//        image2.setImageResource(R.mipmap.msig_card);
//        image3.setImageResource(R.mipmap.prudential_card);


//
//        String url = null;
//        url = "https://lh3.googleusercontent.com/j-MpR5fznDqo_AcNEPeaRrkRrxZH8VGZAOle8w9vRgLa-Ou7fPASPuBd4BXVHkV_rfnvdXVapPcqQibnmMWcgqwfS5rhaFkFA1ngweEJiHYGh22acvg_T8bQmSVp0cJF7hEM6uCc0qUleoFLg4QCyLYVLGZdrRpS_N927jEqd2YKROlbgcUfJNFq2R" +
//                "87Sy3y9b7q4AAvL2CK1I0cQ5aOPqAabUN5Vl52xAIt4_uumdkF5zu2hVCY-cqprbYJR608liWeK9AV-_VAzfgoZX1SR2YtdEBiDfKx5O5ruzn3OfX74uFNM07kBVnhTPwbQqBn3OnEEYBRHTCmD1ybyrR4arhhz7TpxdbphbaGgHhHDtUmFv1Bwwr1IMhSWQ4hd26m9KPGABl9bn0PDC85wCapW5g1Q1fFwELITd7xkiJ04WuQJS0rMXlUiWaOTmc_iGOJHjs0i7t3pv8SJZluc3G5vUFSq5JMsKZN1brRz47ckmo9cKe2isqip6eLjtdZosY43sZg3Qysi-L3j_4oUiMLkp1Wj4imRS0xmWU6_KTmlw=w150-h100-no";
//
//
//        new DownloadImageTask(image4).execute(url);
//        Bitmap bmp = null;
//        try {
//            URL url2 = new URL(url);
//            bmp = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        image4.setImageBitmap(bmp);

        this.show();


        final Button RequestButton = (Button) findViewById(R.id.request_btn);
        // if decline button is clicked, close the custom dialog
        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                final Dialog dialog2 = new submit_Dialog(context, m_Dialog.this, clinic);
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

        Button registerButton = (Button) findViewById(R.id.register_btn);
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


    private void initGUIAndParam(){

        ImageView image1 = (ImageView) findViewById(R.id.card_1);
        ImageView image2 = (ImageView) findViewById(R.id.card_2);
        ImageView image3 = (ImageView) findViewById(R.id.card_3);
        ImageView image4 = (ImageView) findViewById(R.id.card_4);
        url = context.getResources().getString(R.string.serverUrl);
        url_method = context.getResources().getString(R.string.serverInsuranceUrl);
        imageBundle = new ArrayList<ImageView>();
        imageBundle.add(image1);
        imageBundle.add(image2);
        imageBundle.add(image3);
        imageBundle.add(image4);

    }
}
