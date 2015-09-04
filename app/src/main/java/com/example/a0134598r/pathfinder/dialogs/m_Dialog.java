package com.example.a0134598r.pathfinder.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;

import com.example.a0134598r.pathfinder.R;
import com.example.a0134598r.pathfinder.System.ButtonHelper;
import com.example.a0134598r.pathfinder.guiModels.QueueSubmit;

/**
 * Created by jixiang on 3/9/15.
 */
public class m_Dialog extends Dialog {
    /**
     * Create a Dialog window that uses the default dialog frame style.
     *
     * @param context The Context the Dialog is to run it.  In particular, it
     *                uses the window manager and theme in this context to
     *                present its UI.
     */



    final Button RequestButton = (Button) this.findViewById(R.id.request_btn);
    final Button RegisterButton = (Button) this.findViewById(R.id.register_btn);
    final Button RouteButton = (Button) this.findViewById(R.id.route_btn);
    ButtonHelper bh;
    QueueSubmit qs;

    public m_Dialog(Context context,QueueSubmit qs) {
        super(context);

        bh = new ButtonHelper();
        qs = qs;
        this.setContentView(R.layout.activity_custom_dialog);
        // Set dialog title
        this.setTitle("Custom Dialog");
        ImageView image = (ImageView) this.findViewById(R.id.imageDialog);
        image.setImageResource(R.mipmap.ic_launcher);


        this.show();

        RequestButton.setOnClickListener(bh.requestButtonListener(context,qs,this,bh));
        RouteButton.setOnClickListener(bh.routeButtonListener(context,this,qs));
    }
}
