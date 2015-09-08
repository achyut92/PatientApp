package com.example.a0134598r.pathfinder.activities;

/**
 * Created by Muhamad Adib Ishak on 9/27/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.HttpConstraints;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PatientRegistration extends Activity {

    /**
     * Displays text to the user
     */
    //static String result = "";
    TextView id, name, address1, address2, mobile, ic_num;
    Button register_btn, cancel_btn;
    Spinner blood;

    /**
     * JSON node names
     */
    //String patientID = null;
    String patientName = null;
    String patientAddress1 = null;
    String patientAddress2 = null;
    String patient_ic_num = null;
    String bloodGroup = null;
    String patientPhone = null;
    String uuid = null;

    String url = null;
    String url_operation = null;

    String clinic_name = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        /**
         * Getting values from Patient Registration View
         */
        initGUI();

        clinic_name = getIntent().getStringExtra("clinic_name");

        /**
         * test
         */

        //setPatientAttributes();

        /**
         * Setting Listeners
         */

        setListeners();


        url = getResources().getString(R.string.serverUrl);
        url_operation = getResources().getString(R.string.serverRegistration);

    }


    private void initGUI() {

        //id = (TextView) findViewById(R.id.patientId);
        name = (TextView) findViewById(R.id.PATIENT_NAME);
        address1 = (TextView) findViewById(R.id.PATIENT_ADDRESS_1);
        address2 = (TextView) findViewById(R.id.PATIENT_ADDRESS_2);
        ic_num = (TextView) findViewById(R.id.reg_ic_num);
        blood = (Spinner) findViewById(R.id.BLOOD_GROUP);
        mobile = (TextView) findViewById(R.id.PATIENT_PHONE);
        register_btn = (Button) findViewById(R.id.reg_reg);
        cancel_btn = (Button) findViewById(R.id.reg_cancel);

    }

    private void setPatientAttributes() {

        id.setText("12333");
        name.setText("testname");
        address1.setText("testaddress_1");
        address2.setText("testaddress_2");
        ic_num.setText("test_ic_num");
        //blood.setText("testblood");
        mobile.setText("testmobile");
    }


    private void getPatientAttributes() {

        //patientID = id.getText().toString();
        patientName = name.getText().toString();
        patientAddress1 = address1.getText().toString();
        patientAddress2 = address2.getText().toString();
        patient_ic_num = ic_num.getText().toString();
        bloodGroup = String.valueOf(blood.getSelectedItem());
        patientPhone = mobile.getText().toString();
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    private void setListeners() {

        register_btn.setOnClickListener(setRegButtonListener());
        cancel_btn.setOnClickListener(setCancelButtonListener());
    }

    private View.OnClickListener setRegButtonListener() {

        View.OnClickListener oc = new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {


                //getAttributes
                getPatientAttributes();
                if(isEmpty()) {
                    if ((patient_ic_num.matches("[A-Z][0-9]{8}[A-Z]"))) {

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        /**
                         * Calling Async Task to post JSON patient
                         */
                        new HttpAsyncTask().execute(url + url_operation);
                        Intent i;
                        i = new Intent(getApplicationContext(), FindClincSpecificLoc.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplication(),"Please enter the correct NRIC/FIN",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplication(),"Please Enter all the Details",Toast.LENGTH_SHORT).show();
                }

            }
        };

        return oc;
    }

    private boolean isEmpty() {
        if(patientName.isEmpty()||patientAddress1.isEmpty()||patientAddress2.isEmpty()||patient_ic_num.isEmpty()||bloodGroup.isEmpty()||
        patientPhone.isEmpty()){
            return false;
        }else{
            return true;
        }
    }


    private View.OnClickListener setCancelButtonListener() {

        View.OnClickListener oc = new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                Intent i;
                i = new Intent(getApplicationContext(), FindClincSpecificLoc.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        };

        return oc;
    }

    private String postRequest(String urls) {

        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        org.apache.http.HttpResponse response;
        JSONObject json = new JSONObject();

        //InputStream in = null;
        try {
            HttpPost post = new HttpPost(urls);
            //json.put("patient_id", patientID);
            json.put("name", patientName);
            json.put("patient_address_1", patientAddress1);
            json.put("patient_address_2", patientAddress2);
            json.put("ic_num",patient_ic_num);
            json.put("blood_group", bloodGroup);
            json.put("patient_phone", patientPhone);
            json.put("uuid", uuid);
            json.put("clinic_name",clinic_name);
            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);

            /**
             * Checking response
             */
            if (response.getStatusLine().getStatusCode() == HttpConstraints.HTTP_RESPONSE_200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                    if (result.equals(HttpConstraints.HTTP_REQUEST_SUCCESS)){
                        return HttpConstraints.HTTP_REQUEST_SUCCESS;
                    }else if (result.equals(HttpConstraints.HTTP_REQUEST_DUPLICATED)){
                        return HttpConstraints.HTTP_REQUEST_DUPLICATED;
                    }else{
                        return HttpConstraints.HTTP_REQUEST_NOTEXPECTED;
                    }
                }
                Log.d("inputStream:", result);
            } else {
                return HttpConstraints.HTTP_RESPONSE_NOT200;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return HttpConstraints.HTTP_REQUEST_NOTEXPECTED;
    }

    /**
     * A class for turning a byte stream into a character stream
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    /**
     * Async Task class to post JSON by making HTTP call
     */
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return postRequest(urls[0]);
        }

        /**
         * Update parsed JSON data into Patient Registration View
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            if (result.equals(HttpConstraints.HTTP_REQUEST_SUCCESS)) {
                Toast.makeText(getBaseContext(), "Register succeed", Toast.LENGTH_SHORT).show();
                Intent i;
                i = new Intent(getApplicationContext(), FindClincSpecificLoc.class);
                startActivity(i);
            }else if (result.equals(HttpConstraints.HTTP_REQUEST_DUPLICATED)){
                Toast.makeText(getBaseContext(), "ic_num Duplicated", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getBaseContext(), "Register failed", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                //id.setText("Patient ID:" + jsonObject.getString("patientId"));
                name.setText("Name:" + jsonObject.getString("PATIENT_NAME"));
                address1.setText("Address:" + jsonObject.getString("PATIENT_ADDRESS_1"));
                address2.setText("Postal Code:" + jsonObject.getString("PATIENT_ADDRESS_2"));
                //blood.setText("Blood Group:" + jsonObject.getString("BLOOD_GROUP"));
                mobile.setText("Mobile:" + jsonObject.getString("PATIENT_MOBILE"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inflate the menu; this add items to the action bar if it is present
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will automatically handle clicks on the
     * Home/Up button, so long as you specify a parent activity in AndroidManifest.xml
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /**
         * No inspection Simplifiable If Statement
         */
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}