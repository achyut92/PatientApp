package com.example.a0134598r.pathfinder.BackGroundTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by jixiang on 4/9/15.
 */
public class GetGoogleNotificationIdTask extends AsyncTask<Void, Void, String>{

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "114034277568";
    Context c;

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public GetGoogleNotificationIdTask(Context c) {
        super();
        this.c = c;
    }

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param s The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Creates a new asynchronous task. This constructor must be invoked on the UI thread.
     */


    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(Void... params) {
        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(c);
            }
            regid = gcm.register(PROJECT_NUMBER);
            msg = "Device registered, registration ID=" + regid;
            Log.i("GCM", msg);


        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();

        }
        return regid;
    }
}
