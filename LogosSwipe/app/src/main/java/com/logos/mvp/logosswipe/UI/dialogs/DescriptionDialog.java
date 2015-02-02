package com.logos.mvp.logosswipe.UI.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * Created by Sylvain on 02/02/15.
 */
public class DescriptionDialog extends DialogFragment {
    public final static String ARG_TITLE="ARG_TITLE";
    public final static String ARG_DESCRIPTION="ARG_DESCRIPTION";

    private String mTitle;
    private String mDescription;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments()!=null){
            mTitle=getArguments().getString(ARG_TITLE);
            mDescription=getArguments().getString(ARG_DESCRIPTION);
        }
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mDescription)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setTitle(mTitle);

        // Create the AlertDialog object and return it
        return builder.create();

    }
}