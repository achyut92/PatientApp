package com.example.a0134598r.pathfinder.guiModels;

import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jixiang on 3/9/15.
 */
public class QueueSubmit {

    EditText finNumber;
    Button submitButton;
    Button clearButton;
    Button backButton;
    String clinicName;
    LatLng destination;

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public QueueSubmit(EditText finNumber, Button submitButton, Button clearButton, Button backButton,String clinicName
                        ,LatLng destination) {

        this.finNumber = finNumber;
        this.submitButton = submitButton;
        this.clearButton = clearButton;
        this.backButton = backButton;
        this.clinicName = clinicName;
        this.destination = destination;

    }


    public EditText getFinNumber() {
        return finNumber;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void setFinNumber(EditText finNumber) {
        this.finNumber = finNumber;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(Button submitButton) {
        this.submitButton = submitButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public void setClearButton(Button clearButton) {
        this.clearButton = clearButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public void setBackButton(Button backButton) {
        this.backButton = backButton;
    }
}
