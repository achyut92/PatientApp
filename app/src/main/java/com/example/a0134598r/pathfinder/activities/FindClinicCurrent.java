package com.example.a0134598r.pathfinder.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.models.Clinic;
import com.example.a0134598r.pathfinder.models.Place;
import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.utils.DirectionsJSONParser;
import com.example.a0134598r.pathfinder.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindClinicCurrent extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }


    //private String API_KEY = getResources().getString(R.string.google_maps_key);

    private String API_KEY = "AIzaSyAXNTxodHtPuBG0N54tgdZYRfNY2FRDej8";


    //hard cording coordinate
//
    private double LAT = 0.00,
            LNG = 0.00;
//

    String type = "hospital";
    ArrayList<Clinic> result;

    LatLng origin;
    LatLng destination;
    Polyline line;

    RadioButton rbDriving;
    RadioButton rbWalking;
    RadioGroup rgModes;
    RadioButton rbTransit;
    int mMode = 0;
    final int MODE_DRIVING = 0;
    final int MODE_WALKING = 1;
    final int MODE_TRANSIT = 2;

    static int zoom =13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_clinic_current);

        // Getting reference to rb_driving
        rbDriving = (RadioButton) findViewById(R.id.rb_driving);


        // Getting reference to rb_walking
        rbWalking = (RadioButton) findViewById(R.id.rb_walking);

        // Getting reference to rb_transit
        rbTransit = (RadioButton) findViewById(R.id.rb_transit);

        // Getting Reference to rg_modes
        rgModes = (RadioGroup) findViewById(R.id.rg_modes);


        setUpMapIfNeeded();

        gotoLocation(LAT, LNG);
        retrieveGeoPoint();

        //findPlace();

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
        setPreference();
        initializeCurrentLocation();
        //setListener();
    }


    private void initializeCurrentLocation() {

        GPSTracker gps = new GPSTracker(FindClinicCurrent.this);
        if (gps.canGetLocation) {
            LAT = gps.getLatitude();
            LNG = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        origin = new LatLng(LAT, LNG);
    }

    private void setPreference() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }




    private void gotoLocation(double lat, double lng) {

        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        MarkerOptions option = new MarkerOptions();
        option.position(ll);
        option.title("YOU");

        mMap.addMarker(option);

        //mMap.addMarker(new MarkerOptions().position(ll).title("This is a marker"));
        mMap.animateCamera(update);

        Toast.makeText(this, lat + "    " + lng, Toast.LENGTH_LONG).show();

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(ll)
                .strokeWidth(5)
                .radius(3000)
                .strokeColor(Color.RED));

    }


    private void retrieveGeoPoint(){

        ParseGeoPoint object = new ParseGeoPoint(LAT,LNG);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ClinicDetail");
        query.whereWithinKilometers("GEOPOINT",object,3.00);
        query.findInBackground(new FindCallback<ParseObject>() {


            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {


                result = new ArrayList<Clinic>();
                if (parseObjects.size() >= 1) {
                    for (ParseObject po : parseObjects) {
                        ParseGeoPoint userLocation = po.getParseGeoPoint("GEOPOINT");
                        Clinic clinic = new Clinic(po.getString("CLINIC"),po.getString("ADDRESS_1"),po.getString("ESTATE"),userLocation.getLatitude(),
                                userLocation.getLongitude());

                        result.add(clinic);

                    }
                    //theId = objects.get(0).getObjectId();
                }

                Log.i("rrr", String.valueOf(result.size()));


                new GetPlaces(FindClinicCurrent.this, result).execute();

            }
        });

    }


    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Clinic>> {

        private ProgressDialog dialog;
        private Context context;
        private String places;
        private ArrayList<Clinic> res;

        public GetPlaces(Context context,ArrayList<Clinic> res) {
            this.context = context;
            this.places = places;
            this.res = res;
        }

        @Override
        protected void onPostExecute(ArrayList<Clinic> result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            GoogleMap somemap = FindClinicCurrent.this.getmMap();
            if (somemap == null) {
                //GoogleMap somemap = FindClinicCurrent.this.getmMap();
                somemap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
            }

            setMarker(somemap, this.res);
            setListener(somemap);
            //Toast.makeText(getApplicationContext(),String.valueOf(result.size()) , Toast.LENGTH_LONG).show();


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(result.get(0).getLATITUDE(), result
                            .get(0).getLONGITUDE())) // Sets the center of the map to
                            // Mountain View
                    .zoom(zoom) // Sets the zoom
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
        protected ArrayList<Clinic> doInBackground(Void... arg0) {
            return this.res;
        }


        private void setMarker(GoogleMap map,ArrayList<Clinic> result){

            for (int i = 0; i < result.size(); i++) {
                LatLng PERTH = new LatLng(result.get(i).getLATITUDE(), result
                        .get(i).getLONGITUDE());

                map.addMarker(new MarkerOptions()
                        .title(result.get(i).getCLINIC())
                        .position(PERTH)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.iconpin))
                        .snippet(result.get(i).getADDRESS_1()));
                //clinicMarker.set(i,tempMarker);
                //Toast.makeText(getApplicationContext(),String.valueOf(result.get(i).getLONGITUDE()) , Toast.LENGTH_LONG).show();

            }

        }
        private void setListener(GoogleMap map) {

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //maker info popup
                   marker.showInfoWindow();
                    //refresh path Builder
                    if (line != null) {
                        line.remove();
                    }
                    destination = marker.getPosition();
                    Log.i("iiii", destination.latitude + " " + destination.longitude);
                    pathBuilder();


                    return true;
                }
            });
        }

    }

    private void pathBuilder(){
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(this.origin, this.destination);

        DownloadTask downloadTask = new DownloadTask();

        //Start downloading json data from Google Directions API
        downloadTask.execute(url);

    }
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        String mode = "mode=driving";

        if(rbDriving.isChecked()){
            mode = "mode=driving";
            mMode = 0 ;
        }else if(rbWalking.isChecked()){
            mode = "mode=walking";
            mMode = 1;
        }else if(rbTransit.isChecked()){
            mode = "mode=transit";
            mMode = 2;
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    // A method to download json data from url
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloa", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    // A class to download data from Google Directions URL
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    // A class to parse the Google Directions in JSON format
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;


            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                //lineOptions.color(Color.RED);

                // Changing the color polyline according to the mode
                if(mMode==MODE_DRIVING)
                    lineOptions.color(Color.RED);
                else if(mMode==MODE_WALKING)
                    lineOptions.color(Color.BLUE);
                else if(mMode==MODE_TRANSIT)
                    lineOptions.color(Color.GREEN);

            }

            // Drawing polyline in the Google Map for the i-th route
            line = mMap.addPolyline(lineOptions);

        }
    }

}




