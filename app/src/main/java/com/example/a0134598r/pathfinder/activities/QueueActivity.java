package com.example.a0134598r.pathfinder.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QueueActivity extends Activity {


    TextView tvIsConnected;
    static String result = "";
    TextView queue, doctor;
    Button copy;


    String clinicName = null;
    String finNo = null;
    String uuid = null;
    String qu = null;
    String doc = null;

    String url =null;
    String urlFunction =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        initGUIParam();
        Log.d("clinic name", clinicName + uuid);

        if (isConnected()) {
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("Connected");
        } else {
            tvIsConnected.setText("Not Connected");
        }

        setButtonListner();

        new HttpAsyncTask().execute(url+urlFunction);


    }


    private void initGUIParam(){

        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        queue = (TextView) findViewById(R.id.qNum);
        doctor = (TextView) findViewById(R.id.doctor);

        copy = (Button)findViewById(R.id.copy);

        clinicName = getIntent().getStringExtra("clinic_name");                           //Get clinic name from FindClinicSpecificLoc Activity, POST Parameter
        finNo = getIntent().getStringExtra("IC_Number");

        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //POST Parameter
        url = getResources().getString(R.string.serverUrl);
        urlFunction = getResources().getString(R.string.serverQueue);

    }

    private void setButtonListner() {

        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData[] clipData = new ClipData[1];

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipData[0] = ClipData.newPlainText("Q details",clinicName+"\n");
            }
        });
    }

    private String postRequest(String urls) {
        //StringBuilder sb = new StringBuilder();

        HttpClient client = new DefaultHttpClient();
        //HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        org.apache.http.HttpResponse response;
        JSONObject json = new JSONObject();

        InputStream in = null;
        try {
            HttpPost post = new HttpPost(urls);
            json.put("clinic_name", clinicName);
            json.put("uuid", uuid);
            json.put("fin_no",finNo);
            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response =  client.execute(post);

                    /*Checking response */
            if (response != null) {
                in = response.getEntity().getContent();
                result = convertInputStreamToString(in);
                Log.d("inputStream:", result);
            } else {
                result = "Did not work!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return postRequest(urls[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received", Toast.LENGTH_SHORT).show();
            //etResponse.setText(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                qu = jsonObject.getString("queue_num");
                doc = jsonObject.getString("doctor");
                queue.setText("Your Queue Number is " + qu);
                doctor.setText("Doctor:" + doc);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
