package com.example.a0134598r.pathfinder.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.models.Place;
import com.example.a0134598r.pathfinder.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FindClinicEstateName extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    //private String API_KEY = getResources().getString(R.string.google_maps_key);

    private String API_KEY = "AIzaSyAXNTxodHtPuBG0N54tgdZYRfNY2FRDej8";


    //hard cording coordinate

    private static double LAT = 1.3161811,
            LNG = 103.7649377;



    String estateName;
    String type = "hospital";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_direction3);

        //
        Intent i = getIntent();
        estateName = i.getStringExtra("estate_name");
        //

        setUpMapIfNeeded();
        setPreference();
        findPlace();

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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
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
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);


        //mMap.addMarker(new MarkerOptions().position(ll).title("This is a marker"));
        mMap.animateCamera(update);

        Toast.makeText(this, lat+"    "+lng, Toast.LENGTH_LONG).show();

    }


    private void findPlace(){

        /*
        ArrayList<Place> places = findPlaces(CLEMENTI_LAT,CLEMENTI_LNG,"hospital");

        if (places.size() != 0){
            Toast.makeText(this, places.get(0).getName(), Toast.LENGTH_LONG).show();
        }

       */

        //Toast.makeText(this, makeUrl(CLEMENTI_LAT,CLEMENTI_LNG,"hospital"), Toast.LENGTH_LONG).show();

        //tv.setText(makeUrl(CLEMENTI_LAT,CLEMENTI_LNG,"hospital"));

        //getJSON()
        /*
        tv.setText(makeUrl(CLEMENTI_LAT,CLEMENTI_LNG,"hospital"));

        String urlContents = getUrlContents(makeUrl(CLEMENTI_LAT,CLEMENTI_LNG,"hospital"));
        Log.i("URL",urlContents);
        */

        //Log.i("URL",makeUrl(CLEMENTI_LAT,CLEMENTI_LNG,"hospital"));
        //String urlContents = getUrlContents(makeUrl(CLEMENTI_LAT,CLEMENTI_LNG,"hospital"));
        //Log.i("URL",urlContents);



        new GetPlaces(FindClinicEstateName.this,
                type.toLowerCase().replace(
                        "-", "_").replace(" ", "_")).execute();




    }


    //https://maps.googleapis.com/maps/api/place/search/json?&location=1.3161811,103.7649377&radius=1000&types=hospital&sensor=false&key=AIzaSyAXNTxodHtPuBG0N54tgdZYRfNY2FRDej8
    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=1.3161811,103.7649377&radius=500&types=hospital&key=AIzaSyAXNTxodHtPuBG0N54tgdZYRfNY2FRDej8
    private String makeUrl(double latitude, double longitude, String place) {
        StringBuilder urlString = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/search/json?");

        if (place.equals("")) {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=1000");
            // urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
        } else {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=1000");
            urlString.append("&types=" + place);
            urlString.append("&sensor=false&key=" + API_KEY);
        }
        return urlString.toString();
    }


    protected String getJSON(String url) {
        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }


    public ArrayList<Place> findPlaces(double latitude, double longitude,
                                       String placeSpacification) {

        String urlString = makeUrl(latitude, longitude, placeSpacification);

        Log.i("IIIII",urlString);
        //try {
        Log.i("jjjjj","hello");
        String json = getJSON(urlString);

        JSONObject object = null;
        try {
            object = new JSONObject(json);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray array = null;
        try {
            array = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayList<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    //Log.i("IIIII",arrayList.get(i).getName());
                    Place place = Place
                            .jsonToPontoReferencia((JSONObject) array.get(i));
                    Log.v("Places Services ", "" + place);
                    arrayList.add(place);

                } catch (Exception e) {
                }
            }
            return arrayList;



    }



    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

        private ProgressDialog dialog;
        private Context context;
        private String places;

        public GetPlaces(Context context, String places) {
            this.context = context;
            this.places = places;
        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            for (int i = 0; i < result.size(); i++) {
                mMap.addMarker(new MarkerOptions()
                        .title(result.get(i).getName())
                        .position(
                                new LatLng(result.get(i).getLatitude(), result
                                        .get(i).getLongitude()))
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.iconpin))
                        .snippet(result.get(i).getVicinity()));
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(result.get(0).getLatitude(), result
                            .get(0).getLongitude())) // Sets the center of the map to
                            // Mountain View
                    .zoom(14) // Sets the zoom
                    .tilt(30) // Sets the tilt of the camera to 30 degrees
                    .build(); // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
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
        protected ArrayList<Place> doInBackground(Void... arg0) {

            ArrayList<Place> findPlaces = findPlaces(LAT, // 28.632808
                    LNG, places); // 77.218276

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                Log.i("Answer:", "places : " + placeDetail.getName());
            }
            return findPlaces;
        }

    }


    /*
    private void writeToFile(String data) {

        File file;
        FileOutputStream outputStream;
        try {
            // file = File.createTempFile("MyCache", null, getCacheDir());
            file = new File(getCacheDir(), "json.txt");

            outputStream = new FileOutputStream(file);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/

}
