package com.example.a0134598r.pathfinder.obsolete;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.models.Clinic;
import com.example.a0134598r.pathfinder.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindClinicEstateName extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    //private String API_KEY = getResources().getString(R.string.google_maps_key);

    private String API_KEY = "AIzaSyAXNTxodHtPuBG0N54tgdZYRfNY2FRDej8";


    //hard cording coordinate

    private static double LAT = 1.3161811, LNG = 103.7649377;

    static int zoom=13;



    String estateName;

    ArrayList<Clinic> result = new ArrayList<Clinic>();
    int mark = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_clinic_estate);

        //result = new ArrayList<Clinic>();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        //
        Intent i = getIntent();
        estateName = i.getStringExtra("estate_name");
        //

        setUpMapIfNeeded();
        setPreference();


            retrieveFromCloudByEstateName(estateName);

        //findPlace();

        //findClinic();

        //Log.i("uuuu","finished");

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            //mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
            //      .getMap();

            // in get places.
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        try {
            geoLocate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void setPreference() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void geoLocate() throws IOException {


        String location = estateName;

        if (location.length() == 0) {
            Toast.makeText(this, "Please ENTER an estate name", Toast.LENGTH_LONG).show();
        } else {
            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocationName(location, 1);
            //List<Address> list = gc.getFromLocation(LAT, LNG, 5);
            Address add = list.get(0);

            //String locality = add.getLocality();

            double lat = add.getLatitude();
            double lng = add.getLongitude();

            LAT = lat;
            LNG = lng;

            gotoLocation(lat, lng);
        }
    }

    private void gotoLocation(double lat, double lng) {

        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);


        //mMap.addMarker(new MarkerOptions().position(ll).title("This is a marker"));
        mMap.animateCamera(update);

        Toast.makeText(this, lat+"    "+lng, Toast.LENGTH_LONG).show();

    }



    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Clinic>> {

        private ProgressDialog dialog;
        private Context context;

        public GetPlaces(Context context) {
            this.context = context;

        }

        @Override
        protected void onPostExecute(ArrayList<Clinic> result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (mMap == null) {
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
            }
            //Toast.makeText(getApplicationContext(),String.valueOf(result.size()) , Toast.LENGTH_LONG).show();
            for (int i = 0; i < result.size(); i++) {
                LatLng PERTH = new LatLng(result.get(i).getLATITUDE(), result
                        .get(i).getLONGITUDE());

                mMap.addMarker(new MarkerOptions()
                        .title(result.get(i).getCLINIC())
                        .position(PERTH)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.iconpin))
                        .snippet(result.get(i).getADDRESS_1()));
                Toast.makeText(getApplicationContext(),String.valueOf(result.get(i).getLONGITUDE()) , Toast.LENGTH_LONG).show();
            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(result.get(0).getLATITUDE(), result
                            .get(0).getLONGITUDE())) // Sets the center of the map to
                            // Mountain View
                    .zoom(zoom) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            LatLng PERTH = new LatLng(-31.90, 115.86);
            Marker perth = mMap.addMarker(new MarkerOptions()
                    .position(PERTH)
                    .draggable(true));

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected ArrayList<Clinic> doInBackground(Void... arg0) {
            return result;
        }

    }

    private void retrieveFromCloudByEstateName(String estateName) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Clinic");
        query.whereEqualTo("ESTATE", estateName);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (objects.size() >= 1) {
                    for (ParseObject po : objects) {
                        Clinic clinic = new Clinic(po.getString("CLINIC"),po.getString("ADDRESS_1"),po.getString("ESTATE"),po.getDouble("LATITUDE"),
                                po.getDouble("LONGITUDE"));

                        result.add(clinic);
                        Log.i("uuu", po.getString("CLINIC"));
                    }
                    //theId = objects.get(0).getObjectId();
                    new GetPlaces(FindClinicEstateName.this).execute();
                }else {
                    Toast.makeText(getApplicationContext(),"Clinic's could not be found in this locality!!Sorry.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent (getApplicationContext(),SearchScreen.class);
                    startActivity(intent);
                    finish();

                    //startActivity(this.getParentActivityIntent());
                }



            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
