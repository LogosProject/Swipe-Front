package com.logos.mvp.logosswipe.UI.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.fragments.ProblemsChoiceFragment;
import com.logos.mvp.logosswipe.network.RequestQueueSingleton;
import com.logos.mvp.logosswipe.utils.JSONConverter;
import com.logos.mvp.logosswipe.utils.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import greendao.Problem;
import greendao.ProblemDao;

/**
 * Created by Sylvain on 02/02/15.
 */
public class CreationDialog extends DialogFragment {
    public final static String TAG = "CreationDialog";
    public final static String ARG_MODE="ARG_MODE";

    private EditText mEtTitle;
    private EditText mEtDescription;
    public enum DIALOG_MODE {
        NONE,
        PROBLEM,
        VALUE
    }
    private DIALOG_MODE mDialogMode;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments()!=null){
            mDialogMode = (DIALOG_MODE)getArguments().getSerializable(ARG_MODE);

        }
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_creator, null);
        mEtTitle = (EditText) dialogView.findViewById(R.id.et_title);
        mEtDescription = (EditText) dialogView.findViewById(R.id.et_description);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                StringRequest postRequest = new StringRequest(Request.Method.POST, Requests.postProblemUrl(),
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                switch (mDialogMode){
                                    case PROBLEM:
                                        ((ProblemsChoiceFragment)getTargetFragment()).launchRequest();
                                        Log.d(TAG,response);
                                        break;
                                    case VALUE:
                                        break;
                                }
                                Log.d("Response", response.toString());
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                            }
                        } )
                {

                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("name", mEtTitle.getText().toString());
                        params.put("description", mEtDescription.getText().toString());

                        return params;
                    }
                };
                RequestQueueSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(postRequest);

            }
        })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setCancelable(true);
        switch (mDialogMode){
            case PROBLEM:
                builder.setTitle("Créer une problématique");
                break;
            case VALUE:
                builder.setTitle("Créer une valeur");
                break;
        }

        // Create the AlertDialog object and return it
        return builder.create();

    }
}
