package com.logos.mvp.logosswipe.UI.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.VersusActivity;
import com.logos.mvp.logosswipe.UI.fragments.CompareSolutionFragment;
import com.logos.mvp.logosswipe.UI.fragments.DebateFragment;
import com.logos.mvp.logosswipe.UI.fragments.ProblemsChoiceFragment;
import com.logos.mvp.logosswipe.UI.fragments.SolutionsChoiceFragment;
import com.logos.mvp.logosswipe.UI.fragments.ValuesChoiceFragment;
import com.logos.mvp.logosswipe.network.RequestQueueSingleton;
import com.logos.mvp.logosswipe.utils.Requests;

import java.util.HashMap;
import java.util.Map;

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
        VALUE,
        SOLUTION,
        COMMENT
    }
    private DIALOG_MODE mDialogMode;
    private long mCurentVersus = -1L;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments()!=null){
            mDialogMode = (DIALOG_MODE)getArguments().getSerializable(ARG_MODE);
            mCurentVersus=getArguments().getLong(CompareSolutionFragment.KEY_CURRENT_VERSUS);
        }
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_creator, null);
        mEtTitle = (EditText) dialogView.findViewById(R.id.et_title);
        mEtDescription = (EditText) dialogView.findViewById(R.id.et_description);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String url = "";
                switch (mDialogMode){
                    case PROBLEM:
                       url=Requests.postProblemUrl();
                        break;
                    case VALUE:
                        url=Requests.postValueProblemUrl(((ValuesChoiceFragment)getTargetFragment()).getProblemID());
                        break;
                    case SOLUTION:
                        url=Requests.postSolutionProblemUrl(((SolutionsChoiceFragment) getTargetFragment()).getProblemId());
                        break;
                    case COMMENT:
                        url=Requests.postCommentVersusUrl(mCurentVersus);
                        break;
                }
                StringRequest postRequest = new StringRequest(Request.Method.POST,url,
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
                                        ((ValuesChoiceFragment)getTargetFragment()).launchRequest();
                                        Log.d(TAG,response);
                                        break;
                                    case SOLUTION:
                                        ((SolutionsChoiceFragment)getTargetFragment()).launchRequest();
                                        Log.d(TAG,response);
                                        break;
                                    case COMMENT:
                                        ((DebateFragment)getTargetFragment()).requestComments();
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
                        if(mDialogMode!=DIALOG_MODE.COMMENT) {
                            params.put("description", mEtDescription.getText().toString());
                        }else {
                            params.put("content", mEtDescription.getText().toString());
                            params.put("userId",Requests.USER_ID);
                        }

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
            case SOLUTION:
                builder.setTitle("Créer une solution");
                break;
        }

        // Create the AlertDialog object and return it
        return builder.create();

    }
}
