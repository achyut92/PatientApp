package com.example.a0134598r.pathfinder.utils;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.ClinicInfoContraints;
import com.example.a0134598r.pathfinder.activities.NotifyMessage;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by achyut on 03-09-2015.
 */
public class GcmMessageHandler extends IntentService {

    private static final int NOTIFY_ME_ID = 1337;
    String mes;
    private Handler handler;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    String doc;
    String que;

    //    @Override
//    public void onCreate() {
//        // TODO Auto-generated method stub
//        super.onCreate();
//        handler = new Handler();
//
//    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        doc = extras.getString("doctor");
        que = extras.getString("queue_num");
        if (null!=doc && null!=que) {
            showToast();
            Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));
            GcmBroadcastReceiver.completeWakefulIntent(intent);
//        notification();
        }

    }

    public void showToast() {
        handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                notification();
            }
        });

    }

    public void notification() {

        final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification note = new Notification(R.mipmap.clinic, "Your Queue is ready", System.currentTimeMillis());

        Intent i = new Intent(this, NotifyMessage.class);
        i.putExtra(ClinicInfoContraints.DOCTOR, doc);
        i.putExtra(ClinicInfoContraints.QUEUE_NUM, que);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

        note.setLatestEventInfo(this, "From ClinicLoc",
                "Your Queue Number is:" + que + "\n Doctor:" + doc, pendingIntent);

        notificationManager.notify(NOTIFY_ME_ID, note);
    }


}
