package com.example.a0134598r.pathfinder.models;

/**
 * Created by jixiang on 22/5/15.
 */
public class Clinic {

    private String objectId;
    private String ADDRESS_1;
    private String ADDRESS_2;
    private String CLINIC;
    private String ESTATE;
    private double LATITUDE;
    private double LONGITUDE;

    public Clinic(String objectId, String ADDRESS_1, String ADDRESS_2, String CLINIC,
                  String ESTATE, double LATITUDE, double LONGITUDE) {

        this.objectId = objectId;
        this.ADDRESS_1 = ADDRESS_1;
        this.ADDRESS_2 = ADDRESS_2;
        this.CLINIC = CLINIC;
        this.ESTATE = ESTATE;
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;
    }

    public Clinic(String CLINIC, String ADDRESS_1, String ESTATE, double LATITUDE, double LONGITUDE) {
        this.CLINIC = CLINIC;
        this.ADDRESS_1 = ADDRESS_1;
        this.ESTATE = ESTATE;
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;

    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getADDRESS_1() {
        return ADDRESS_1;
    }

    public void setADDRESS_1(String ADDRESS_1) {
        this.ADDRESS_1 = ADDRESS_1;
    }

    public String getADDRESS_2() {
        return ADDRESS_2;
    }

    public void setADDRESS_2(String ADDRESS_2) {
        this.ADDRESS_2 = ADDRESS_2;
    }

    public String getCLINIC() {
        return CLINIC;
    }

    public void setCLINIC(String CLINIC) {
        this.CLINIC = CLINIC;
    }

    public String getESTATE() {
        return ESTATE;
    }

    public void setESTATE(String ESTATE) {
        this.ESTATE = ESTATE;
    }

    public double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public double getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }



}
