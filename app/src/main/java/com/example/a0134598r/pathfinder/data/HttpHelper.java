package com.example.a0134598r.pathfinder.data;

import android.util.Log;

import com.example.a0134598r.pathfinder.System.ClinicInfoContraints;
import com.example.a0134598r.pathfinder.System.HttpConstraints;
import com.example.a0134598r.pathfinder.System.JsonConstraints;
import com.example.a0134598r.pathfinder.models.Clinic;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jixiang on 11/9/15.
 */
public class HttpHelper {

    public static ArrayList<Clinic> jreadAllClinicList(String url) {
        ArrayList<Clinic> list = new ArrayList<Clinic>();
        JSONObject a = JSONParser.getJSONFromUrlByHttpPost(url);
        if (a == null){
            return list;
        }
        try {
            JSONArray jsonArray = a.getJSONArray(JsonConstraints.j_result);
            int jsonLength = jsonArray.length();
            //List<String> list= new ArrayList<String>();
            for(int i=0;i<jsonLength;i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(ClinicInfoContraints.CLINIC) && jsonObject.has(ClinicInfoContraints.ADDRESS_1)&&
                        jsonObject.has(ClinicInfoContraints.ESTATE)&&jsonObject.has(ClinicInfoContraints.LATITUDE)&&
                        jsonObject.has(ClinicInfoContraints.LONGTITUDE)){

                    String tempLat = jsonObject.getString(ClinicInfoContraints.LATITUDE);
                    String tempLng = jsonObject.getString(ClinicInfoContraints.LONGTITUDE);

                    double resultLat = DataFormat.Stringtodouble(tempLat);
                    double resultLng = DataFormat.Stringtodouble(tempLng);
                    list.add(new Clinic(jsonObject.getString(ClinicInfoContraints.CLINIC),jsonObject.getString(ClinicInfoContraints.ADDRESS_1),
                            jsonObject.getString(ClinicInfoContraints.ESTATE),resultLat,
                            resultLng));
                    //clinic_id = jsonObject.getString("id");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return(list);
    }


    public static ArrayList<Clinic> jreadClinicListByKilo(String url,String src_lat,String src_lng,String kilo) {
        ArrayList<Clinic> list = new ArrayList<Clinic>();
        String resultUrl = url+"?"+ HttpConstraints.HTTP_GET_SRC_LAT+"="+src_lat+"&"+HttpConstraints.HTTP_GET_SRC_LNG+"="+src_lng+"&"+
                HttpConstraints.HTTP_GET_KILO+"="+kilo;
        JSONObject a = JSONParser.getJSONFromUrlByHttpGet(resultUrl);
        if (a == null){
            return list;
        }
        try {
            JSONArray jsonArray = a.getJSONArray(JsonConstraints.j_result);
            int jsonLength = jsonArray.length();
            //List<String> list= new ArrayList<String>();
            for(int i=0;i<jsonLength;i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(ClinicInfoContraints.CLINIC) && jsonObject.has(ClinicInfoContraints.ADDRESS_1)&&
                        jsonObject.has(ClinicInfoContraints.ESTATE)&&jsonObject.has(ClinicInfoContraints.LATITUDE)&&
                        jsonObject.has(ClinicInfoContraints.LONGTITUDE)){

                    String tempLat = jsonObject.getString(ClinicInfoContraints.LATITUDE);
                    String tempLng = jsonObject.getString(ClinicInfoContraints.LONGTITUDE);

                    double resultLat = DataFormat.Stringtodouble(tempLat);
                    double resultLng = DataFormat.Stringtodouble(tempLng);
                    list.add(new Clinic(jsonObject.getString(ClinicInfoContraints.CLINIC),jsonObject.getString(ClinicInfoContraints.ADDRESS_1),
                            jsonObject.getString(ClinicInfoContraints.ESTATE),resultLat,
                            resultLng));
                    //clinic_id = jsonObject.getString("id");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return(list);
    }

    public static String doHttpPostJreadInsuranceImgUrl(Clinic clinic, String url) {

        String result = "";
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);//"http://10.10.2.211:5000/createClinic"
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair(ClinicInfoContraints.CLINIC, clinic.getCLINIC()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            // Log.d("Create Response", response.toString());

                    /*Checking response */
            if (response.getStatusLine().getStatusCode() == HttpConstraints.HTTP_RESPONSE_200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {

                    String json_res = EntityUtils.toString(response.getEntity());
                    JSONObject jObj = new JSONObject(json_res);
                    if (jObj.has(JsonConstraints.JSON_RESPONSE_ERROR)) {
                        String json_error = jObj.getString(JsonConstraints.JSON_RESPONSE_ERROR);
                        if (json_error.equals(JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_CLINIC_NAME)) {
                            result = JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_CLINIC_NAME;
                        } else if (json_error.equals(JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_INSURANCE_CARD)) {
                            result = JsonConstraints.JSON_RESPONSE_ERROR_TYPE_NO_INSURANCE_CARD;
                        }
                    } else {
                        result = json_res;
                    }
                }
                Log.d("inputStream:", result);
            } else {
                return HttpConstraints.HTTP_RESPONSE_NOT200;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
