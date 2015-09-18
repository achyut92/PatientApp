package com.example.a0134598r.pathfinder.BackGroundTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.InsuranceContraints;
import com.example.a0134598r.pathfinder.data.HttpHelper;
import com.example.a0134598r.pathfinder.data.JSONParser;
import com.example.a0134598r.pathfinder.models.Clinic;
import com.example.a0134598r.pathfinder.utils.DownloadImageTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jixiang on 18/9/15.
 */
public class GetInsuranceCardImgUrlTask extends AsyncTask<String, Void, String> {

    Context c;
    Clinic clinic;
    ArrayList<String> urls;
    ArrayList<ImageView> imageViews;

    public GetInsuranceCardImgUrlTask(Context c, Clinic clinic, ArrayList<ImageView> imageViews) {
        this.c = c;
        this.clinic = clinic;
        urls = new ArrayList<String>();
        this.imageViews = imageViews;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        //String clinicName = params[0];
        String url = params[0];
        //Clinic clinic = new Clinic(clinicName);
        return HttpHelper.doHttpPostJreadInsuranceImgUrl(clinic, url);
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        urls = JSONParser.parseImgUrl(s);

        Iterator<String> i = urls.iterator();
        int indexOfImageViews = 0;
        while (i.hasNext()) {
            String url = i.next();
            ImageView tempView = imageViews.get(indexOfImageViews);
            if (null == tempView) {
                continue;
            } else {

                switch (url) {
                    case InsuranceContraints.IMG_AVIVA:
                        tempView.setImageResource(R.mipmap.aviva_card);
                        break;
                    case InsuranceContraints.IMG_MSIG:
                        tempView.setImageResource(R.mipmap.msig_card);
                        break;
                    case InsuranceContraints.IMG_PIONEER:
                        tempView.setImageResource(R.mipmap.pioneer_card);
                        break;
                    case InsuranceContraints.IMG_PRUDENTIAL:
                        tempView.setImageResource(R.mipmap.prudential_card);
                        break;
                    default:
                        break;

                }
                indexOfImageViews++;
            }


        }
    }
}