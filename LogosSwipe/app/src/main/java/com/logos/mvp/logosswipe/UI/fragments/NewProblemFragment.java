package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.logos.mvp.logosswipe.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewProblemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewProblemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewProblemFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;

    private Button buttonCreate;
    private Button buttonCancel;

    public static NewProblemFragment newInstance() {
        NewProblemFragment fragment = new NewProblemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NewProblemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_new_problem, null);

        builder.setView(view);

        buttonCreate = (Button) view.findViewById(R.id.new_problem_create_button);
        buttonCreate.setOnClickListener(handlerButtonCreate);

        buttonCancel = (Button) view.findViewById(R.id.new_problem_cancel_button);
        buttonCancel.setOnClickListener(handlerButtonCancel);

        return builder.create();
    }

    View.OnClickListener handlerButtonCreate = new View.OnClickListener() {
        public void onClick(View v) {
            // TODO save the problem in the list
            NewProblemFragment.this.dismiss();
        }
    };

    View.OnClickListener handlerButtonCancel = new View.OnClickListener() {
        public void onClick(View v) {
            NewProblemFragment.this.dismiss(); // TODO verify
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_problem, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
