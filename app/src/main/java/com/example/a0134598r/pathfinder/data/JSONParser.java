package com.example.a0134598r.pathfinder.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.System.HttpConstraints;
import com.example.a0134598r.pathfinder.System.JsonConstraints;
import com.example.a0134598r.pathfinder.utils.LocationHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class JSONParser {
    //private
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public static String getStreamByHttpGet(String url) {
        String result = "";
//        String clinic_id = id;
//        url = url+"/"+id;
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        try {
            URI uri = new URI(url);
            request.setURI(uri);
            try {
                HttpResponse response = httpclient.execute(request);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine != null && statusLine.getStatusCode() == 200) {
                    HttpEntity entity2 = response.getEntity();
                    if (entity2 != null) {
                        result = EntityUtils.toString(response.getEntity());
                        if (!result.contains(JsonConstraints.JSON_RESPONSE_ERROR)) {
                            return result;
                        } else {
                            result = JsonConstraints.JSON_RESPONSE_PARAM_NULL_NOTICE;
                        }
                    } else {
                        result = JsonConstraints.JSON_RESPONSE_NULL_NOTICE;
                    }
                } else {
                    result = JsonConstraints.JSON_RESPONSE_ERROR_NOTICE;
                }

                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getStreamByHttpPost(String url) {
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {

            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, HttpConstraints.TIME_OUT);
            HttpConnectionParams.setSoTimeout(params, HttpConstraints.ASYNTASK_TIME_OUT);
            DefaultHttpClient httpClient = new DefaultHttpClient(params);
            HttpGet httpPost = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return (sb.toString());
    }


    public static JSONArray getJSONArrayFromUrl(String url) {
        JSONArray jArray = null;
        try {
            jArray = new JSONArray(getStreamByHttpPost(url));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing array " + e.toString());
        }
        return jArray;
    }

    public static JSONObject getJSONFromUrlByHttpPost(String url) {
        JSONObject jObj = null;
        try {
            String json = getStreamByHttpPost(url);
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    public static JSONObject getJSONFromUrlByHttpGet(String url) {
        JSONObject jObj = null;
        try {
            String json = getStreamByHttpGet(url);
            if (isJson(json)) {
                jObj = new JSONObject(json);
            } else {
                Log.e("JSON Parser", "Error parsing data " + json);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    public static boolean isJson(String str) {

        try {
            new JSONObject(str);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


    public static ArrayList<String> parseImgUrl(String json) {

        ArrayList<String> result = new ArrayList<String>();
        String url = "";

        if (result.equals(JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_CLINIC_NAME)) {
            Log.i("Json_error",JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_CLINIC_NAME_REPLY);
            //Toast.makeText(c.getApplicationContext(), JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_CLINIC_NAME_REPLY, Toast.LENGTH_LONG).show();
        } else if (result.equals(JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_INSURANCE_CARD)) {
            Log.i("Json_error",JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_INSURANCE_CARD_REPLY);
            //Toast.makeText(c.getApplicationContext(), JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_INSURANCE_CARD_REPLY, Toast.LENGTH_LONG).show();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(json);
                for (int i = 0; i < jsonObject.length(); i++) {

                    url = jsonObject.getString("url_"+DataFormat.intToString(i));
                    result.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}


