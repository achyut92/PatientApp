package com.example.a0134598r.pathfinder.dialogs;

import android.app.Dialog;
import android.content.Context;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.ButtonHelper;
import com.example.a0134598r.pathfinder.guiModels.QueueSubmit;

/**
 * Created by jixiang on 3/9/15.
 */
public class submit_Dialog extends Dialog {

    /**
     * Create a Dialog window that uses the default dialog frame style.
     *
     * @param context The Context the Dialog is to run it.  In particular, it
     *                uses the window manager and theme in this context to
     *                present its UI.
     */
    public submit_Dialog(Context context,Dialog dialog1, QueueSubmit qs,ButtonHelper bh) {
        super(context);

        this.setTitle("Custom Dialog");
        qs.getSubmitButton().setOnClickListener(bh.submitButtonListener(context, qs, this));
        qs.getClearButton().setOnClickListener(bh.clearButtonListener(context, qs));
        qs.getBackButton().setOnClickListener(bh.backButtonListener(context, this));

        this.show();
        dialog1.dismiss();
    }
}
