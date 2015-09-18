package com.example.a0134598r.pathfinder.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.ButtonHelper;
import com.example.a0134598r.pathfinder.System.GV;
import com.example.a0134598r.pathfinder.activities.QueueActivity;
import com.example.a0134598r.pathfinder.guiModels.QueueSubmit;
import com.example.a0134598r.pathfinder.models.Clinic;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by jixiang on 3/9/15.
 */
public class submit_Dialog extends Dialog {


    EditText finNumber;
    Button submitButton;
    Button clearButton;
    Button backButton;

    String ic_num;

    GoogleCloudMessaging gcm = null;
    String clinic_name = null;

    /**
     * Create a Dialog window that uses the default dialog frame style.
     *
     * @param context The Context the Dialog is to run it.  In particular, it
     *                uses the window manager and theme in this context to
     *                present its UI.
     */
    public submit_Dialog(final Context context,final Dialog dialog1,final Clinic clinic) {
        super(context);

        this.setContentView(R.layout.activity_custom_dialog_que);
        // Set dialog title

        finNumber = (EditText) findViewById(R.id.fin);
        submitButton = (Button) findViewById(R.id.queSubmit);
        clearButton = (Button)findViewById(R.id.queClear);
        backButton = (Button) findViewById(R.id.queBack);

        clinic_name = clinic.getCLINIC();

        this.setTitle("Request for Queue Number");
        //Create touch listeners for all buttons.
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ic_num = finNumber.getText().toString();
                if (ic_num.isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), "Please fill in your correct IC Number!!!", Toast.LENGTH_SHORT).show();
                } else if (ic_num.matches("[A-Z][0-9]{7}[A-Z]")) {

                    registerWithGCM();
                    submit_Dialog.this.dismiss();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Please enter the correct NRIC/FIN", Toast.LENGTH_SHORT).show();
                }
            }


        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finNumber.setText("");

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submit_Dialog.this.dismiss();

            }
        });

        submit_Dialog.this.show();
        dialog1.dismiss();
     }

    public void registerWithGCM(){

        new AsyncTask<Void, Void, String>() {
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
                        gcm = GoogleCloudMessaging.getInstance(getContext().getApplicationContext());
                    }
                    msg = gcm.register(GV.PROJECT_NUMBER);
                    Log.i("GCM", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }

                return msg;

            }

            /**
             * <p>Runs on the UI thread after {@link #doInBackground}. The
             * specified result is the value returned by {@link #doInBackground}.</p>
             * <p/>
             * <p>This method won't be invoked if the task was cancelled.</p>
             *
             * @param msg The result of the operation computed by {@link #doInBackground}.
             * @see #onPreExecute
             * @see #doInBackground
             * @see #onCancelled(Object)
             */
            @Override
            protected void onPostExecute(String msg) {
                super.onPostExecute(msg);
                Intent i = new Intent(getContext(), QueueActivity.class);
                i.putExtra("reg_id", msg);
                i.putExtra("clinic_name",clinic_name);
                i.putExtra("IC_Number", ic_num);
                getContext().startActivity(i);
            }
        }.execute();

    }
}
