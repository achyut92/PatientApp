package com.example.a0134598r.pathfinder.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.models.Clinic;
import com.example.a0134598r.pathfinder.utils.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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

//import com.javacodegeeks.androidgoogleplacesautocomplete.R;

public class FindClincSpecificLoc extends ActionBarActivity implements OnItemClickListener {

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    GoogleMap mMap;

    public GoogleMap getmMap() {
        return mMap;
    }

    MarkerOptions mOption;

    AutoCompleteTextView address;
    Button findLoc;

    double latitude = 0.00, longitude = 0.00;

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

    static int zoom = 13;

    ArrayList<Clinic> result;


    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyAXNTxodHtPuBG0N54tgdZYRfNY2FRDej8";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_loc);
        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.address);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        address = (AutoCompleteTextView) findViewById(R.id.address);
        findLoc = (Button) findViewById(R.id.findLoc);
        rbDriving = (RadioButton) findViewById(R.id.rb_driving);


        // Getting reference to rb_walking
        rbWalking = (RadioButton) findViewById(R.id.rb_walking);

        // Getting reference to rb_transit
        rbTransit = (RadioButton) findViewById(R.id.rb_transit);

        // Getting Reference to rg_modes
        rgModes = (RadioGroup) findViewById(R.id.rg_modes);


        setupMapIfNeeded();
    }

    public void setupMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mOption = new MarkerOptions();
        }
        if (mMap != null) {
            setupMap();
        }
    }

    public void setupMap() {

        findLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (mOption.isVisible()) {
                    mMap.clear();
                }
                getCoordinates();
                retrieveGeoPoint();
            }
        });
    }

    public void getCoordinates() {
        String autoCompText = address.getText().toString();
        Geocoder geoCoder = new Geocoder(FindClincSpecificLoc.this);

        LatLng pos;

        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(autoCompText, 1);
            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }

        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }


        origin = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(origin).title("YOU"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, zoom));
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:sg");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    ////////////
    private void retrieveGeoPoint() {

        ParseGeoPoint object = new ParseGeoPoint(latitude, longitude);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ClinicDetail");
        query.whereWithinKilometers("GEOPOINT", object, 2.00);
        query.findInBackground(new FindCallback<ParseObject>() {


            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {


                result = new ArrayList<Clinic>();
                if (parseObjects.size() >= 1) {
                    for (ParseObject po : parseObjects) {
                        ParseGeoPoint userLocation = po.getParseGeoPoint("GEOPOINT");
                        Clinic clinic = new Clinic(po.getString("CLINIC"), po.getString("ADDRESS_1"), po.getString("ESTATE"), userLocation.getLatitude(),
                                userLocation.getLongitude());

                        result.add(clinic);

                    }
                    //theId = objects.get(0).getObjectId();
                }

                Log.i("rrr", String.valueOf(result.size()));


                new GetPlaces(FindClincSpecificLoc.this, result).execute();

            }
        });

    }


    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Clinic>> {

        private ProgressDialog dialog;
        private Context context;
        private String places;
        private ArrayList<Clinic> res;

        public GetPlaces(Context context, ArrayList<Clinic> res) {
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

            GoogleMap somemap = FindClincSpecificLoc.this.getmMap();
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


        private void setMarker(GoogleMap map, ArrayList<Clinic> result) {

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

                Log.i("iiii", origin.latitude + " " + origin.longitude);
                Log.i("iiii", destination.latitude + " " + destination.longitude);
                pathBuilder();


                return true;
            }
        });
    }



    private void pathBuilder(){
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, destination);

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
