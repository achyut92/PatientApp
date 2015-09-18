package com.example.a0134598r.pathfinder.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.ClinicInfoContraints;

public class NotifyMessage extends Activity {

    TextView txt;

    String doctorName ="";
    String queueNo="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_message);

        initGUIAndParams();


        txt.setText("You queueNO is: " + queueNo + "\n" + "You Doctor is: " + doctorName);
        //setContentView(txt);
    }

    protected void initGUIAndParams(){
        txt = (TextView) this.findViewById(R.id.notify_textview);
        doctorName = getIntent().getStringExtra(ClinicInfoContraints.DOCTOR);
        queueNo = getIntent().getStringExtra(ClinicInfoContraints.QUEUE_NUM);

    }

}

