package com.example.a0134598r.pathfinder.System;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.activities.QueueActivity;
import com.example.a0134598r.pathfinder.dialogs.submit_Dialog;
import com.example.a0134598r.pathfinder.guiModels.QueueSubmit;

/**
 * Created by jixiang on 3/9/15.
 */
public class ButtonHelper {

//
//    public View.OnClickListener requestButtonListener(final Context activity, final QueueSubmit qs, final Dialog dialog,
//                                                      final ButtonHelper bh) {
//
//        View.OnClickListener oc = new View.OnClickListener() {
//
//            /**
//             * Called when a view has been clicked.
//             *
//             * @param v The view that was clicked.
//             */
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog2 = new submit_Dialog(activity,dialog,qs,bh);
//            }
//        };
//
//        return oc;
//    }
//
//    public View.OnClickListener submitButtonListener(final Context activity, final QueueSubmit qs, final Dialog dialog) {
//
//
//        View.OnClickListener oc = new View.OnClickListener() {
//
//            Intent i;
//
//            /**
//             * Called when a view has been clicked.
//             *
//             * @param v The view that was clicked.
//             */
//            @Override
//            public void onClick(View v) {
//
//                if (qs.getFinNumber().getText().toString().isEmpty()) {
//                    Toast.makeText(activity.getApplicationContext(), "Please fill in your IC Number!!!", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    i = new Intent(activity.getApplicationContext(), QueueActivity.class);
//                    i.putExtra("clinic_name", qs.getClinicName());
//                    i.putExtra("IC_Number", qs.getFinNumber().getText().toString());
//                    activity.startActivity(i);
//
//                    dialog.dismiss();
//                }
//            }
//        };
//
//        return oc;
//    }
//
//
//    public View.OnClickListener clearButtonListener(final Context activity, final QueueSubmit qs) {
//
//        View.OnClickListener oc = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                qs.getFinNumber().setText("");
//            }
//        };
//        return oc;
//    }
//
//    public View.OnClickListener backButtonListener(final Context activity, final Dialog dialog) {
//
//        View.OnClickListener oc = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        };
//        return oc;
//    }
//
//    public View.OnClickListener routeButtonListener(final Context activity, final Dialog dialog,final QueueSubmit qs) {
//
//        View.OnClickListener oc = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + qs.getDestination().latitude + "," + qs.getDestination().longitude + "");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                activity.startActivity(mapIntent);
//                dialog.dismiss();
//            }
//        };
//        return oc;
//    }

}
