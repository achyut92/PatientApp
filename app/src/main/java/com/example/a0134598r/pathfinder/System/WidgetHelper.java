package com.example.a0134598r.pathfinder.System;

import android.graphics.Color;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.models.Clinic;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by jixiang on 1/9/15.
 */
public class WidgetHelper {



    public static void setMarkerFromArrayList(GoogleMap mMap,ArrayList<Clinic>result){
        for (int i = 0; i < result.size(); i++) {
            LatLng PERTH = new LatLng(result.get(i).getLATITUDE(), result
                    .get(i).getLONGITUDE());

            mMap.addMarker(new MarkerOptions()
                    .title(result.get(i).getCLINIC())
                    .position(PERTH)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.clinic))
                    .snippet(result.get(i).getADDRESS_1()));
            //clinicMarker.set(i,tempMarker);
            //Toast.makeText(getApplicationContext(),String.valueOf(result.get(i).getLONGITUDE()) , Toast.LENGTH_LONG).show();

        }
    }

    public static void addMarkerAndGoTO(GoogleMap mMap, MarkerOptions option, LatLng ll) {

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, GV.MARKER_ZOOM);

        mMap.addMarker(option);
        //mMap.addMarker(new MarkerOptions().position(ll).title("This is a marker"));
        mMap.animateCamera(update);

    }

    public static void addMarkerWithCircleAndGoTo(GoogleMap mMap, MarkerOptions option, LatLng ll) {

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, GV.MARKER_ZOOM);

        mMap.addMarker(option);
        //mMap.addMarker(new MarkerOptions().position(ll).title("This is a marker"));
        mMap.animateCamera(update);

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(ll)
                .strokeWidth(GV.CIRCLE_STROKE)
                .radius(GV.CIRCLE_RADIUS)
                .strokeColor(Color.RED));
    }

    public static MarkerOptions setMarkerOptions(LatLng ll) {

        MarkerOptions option = new MarkerOptions();
        option.position(ll);
        option.title("YOU");

        return option;
    }

}
