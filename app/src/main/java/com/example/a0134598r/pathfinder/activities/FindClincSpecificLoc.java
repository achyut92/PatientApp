package com.example.a0134598r.pathfinder.activities;

import java.io.IOException;
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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.GV;
import com.example.a0134598r.pathfinder.System.MarkerHelper;
import com.example.a0134598r.pathfinder.dialogs.m_Dialog;
import com.example.a0134598r.pathfinder.models.Clinic;
import com.example.a0134598r.pathfinder.models.Place;
import com.example.a0134598r.pathfinder.utils.CustomInfoWindowAdapter;
import com.example.a0134598r.pathfinder.utils.GPSTracker;
import com.example.a0134598r.pathfinder.utils.LocationHelper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pushbots.push.Pushbots;

//import com.javacodegeeks.androidgoogleplacesautocomplete.R;

public class FindClincSpecificLoc extends ActionBarActivity implements OnItemClickListener{

    private static final String LOG_TAG = GV.LOG_TAG;
    private static final String PLACES_API_BASE = GV.PLACES_API_BASE;
    private static final String TYPE_AUTOCOMPLETE = GV.TYPE_AUTOCOMPLETE;
    private static final String OUT_JSON = GV.OUT_JSON;



    private long lastBackPressTime;
    private Toast toast;
    GoogleMap mMap;
    AutoCompleteTextView address;

    double latitude = 0.00, longitude = 0.00;

    LatLng origin;
    LatLng destination;

    static int zoom = 13;

    String autoCompText;

    ArrayList<String> neighSpin;

    ArrayList<Clinic> result;

    public CustomInfoWindowAdapter adapter;
    public Context applicationContext;

    EditText finNumber;
    Button submitButton;
    Button clearButton;
    Button backButton;

    String ic_num;


    public String getClinic_name() {
        return clinic_name;
    }

    public String clinic_name = null;

    public GoogleMap getmMap() {
        return mMap;
    }

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyAXNTxodHtPuBG0N54tgdZYRfNY2FRDej8";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setPushEnabled(true);
        setContentView(R.layout.activity_clinic_specific_loc);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        initGUIAndParam();

        if (LocationHelper.isProviderEnabled(this)) {
            initializeCurrentLocation();
            setupMapIfNeeded();//refactored
            gotoLocation(latitude, longitude);//refactored
            getGeoPointFromParse();//rename
        }

        address.setOnItemClickListener(setOnItemListeners());

    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMapIfNeeded();
    }

    private void initGUIAndParam() {

        neighSpin = new ArrayList<String>();
        result = new ArrayList<Clinic>();
        applicationContext = getApplicationContext();
        adapter = new CustomInfoWindowAdapter(this,applicationContext);
        address = (AutoCompleteTextView) findViewById(R.id.address);
        address.setText("");
        address.setAdapter(new GooglePlacesAutocompleteAdapter(FindClincSpecificLoc.this, R.layout.list_item));
    }


    private AdapterView.OnItemClickListener setOnItemListeners() {

        AdapterView.OnItemClickListener oc = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setupMapIfNeeded();//pass
                refreshLocation();//pass
                getGeoPointFromParse();//pass

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                autoCompText = null;
                latitude = 0.0;
                longitude = 0.0;
            }
        };
        return oc;
    }


    public void setupMapIfNeeded() {

        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GV.MAIN_PAGE_LAT, GV.MAIN_PAGE_LNG), GV.MAIN_PAGE_ZOOM));
            mMap.setMyLocationEnabled(true);
//            mMap.setInfoWindowAdapter(adapter);
//            mMap.setOnInfoWindowClickListener(adapter);
        }

    }

    private void initializeCurrentLocation() {

        GPSTracker gps = new GPSTracker(FindClincSpecificLoc.this);
        if (gps.canGetLocation) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
            Toast.makeText(this, "gps need to be open!", Toast.LENGTH_LONG);
        }

        origin = new LatLng(latitude, longitude);
    }


    private void gotoLocation(double lat, double lng) {

        LatLng ll = new LatLng(lat, lng);
        MarkerHelper.addMarkerWithCircleAndGoTo(mMap, MarkerHelper.setMarkerOptions(ll), ll);
        Toast.makeText(this, lat + "    " + lng, Toast.LENGTH_LONG).show();
    }

    public void refreshLocation() {
        autoCompText = address.getText().toString();
        Geocoder geoCoder = new Geocoder(FindClincSpecificLoc.this);
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, GV.MARKER_ZOOM));
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


    private void getGeoPointFromParse() {

        ParseGeoPoint object = new ParseGeoPoint(latitude, longitude);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(GV.CLINIC_DETAIL_TABLE_P);
        query.whereWithinKilometers(GV.GEOPOINT, object, GV.PARSE_RADUIUS);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                result = new ArrayList<Clinic>();
                if (parseObjects.size() >= 1) {
                    for (ParseObject po : parseObjects) {
                        ParseGeoPoint userLocation = po.getParseGeoPoint(GV.GEOPOINT);
                        Clinic clinic = new Clinic(po.getString("CLINIC"), po.getString("ADDRESS_1"), po.getString("ESTATE"), userLocation.getLatitude(),
                                userLocation.getLongitude());

                        result.add(clinic);

                    }
                    new GetPlaces(FindClincSpecificLoc.this, result).execute();
                    //theId = objects.get(0).getObjectId();
                } else {
                    Toast.makeText(getApplicationContext(), GV.ERROR_CLINIC_UNFOUND, Toast.LENGTH_LONG).show();
                }
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

            //setMarker(somemap, this.res);
            MarkerHelper.setMarkerFromArrayList(somemap, this.res);
            setListener(somemap); //pending for refactoring
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

    }

    private void setListener(GoogleMap map) {

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //maker info popup
                marker.showInfoWindow();
                //refresh path Builder
                destination = marker.getPosition();
                clinic_name = marker.getTitle();

                //please modify dialogs in package "dialog"
                Clinic clinic = new Clinic(clinic_name);
                Place place = new Place(destination);
                final Dialog dialog = new m_Dialog(FindClincSpecificLoc.this,clinic, place);

                return true;
            }
        });
    }
}

