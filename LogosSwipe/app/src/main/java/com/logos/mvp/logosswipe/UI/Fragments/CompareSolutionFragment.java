package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.ValuesChoiceActivity;
import com.logos.mvp.logosswipe.network.RequestQueueSingleton;
import com.logos.mvp.logosswipe.utils.JSONConverter;
import com.logos.mvp.logosswipe.utils.Requests;

import org.json.JSONArray;
import org.json.JSONException;

import greendao.Problem;
import greendao.ProblemDao;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompareSolutionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompareSolutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompareSolutionFragment extends Fragment {
    private static final String TAG = "CompareSolutionFragment";


    private long problemId = -1L;
    private long[] valueIds = new long[0];
    private long[] solutionsIds = new long[0];

    private TextView mTitleUp;
    private TextView mTitleDown;
    private TextView mDescriptionUp;
    private TextView mDescriptionDown;

    private VerticalSeekBar mSeekbar;

    private OnFragmentInteractionListener mListener;


    public static CompareSolutionFragment newInstance(long problemId,long[] valueIds, long[] solutionsIds) {
        CompareSolutionFragment fragment = new CompareSolutionFragment();
        Bundle args = new Bundle();
        args.putLong(ValuesChoiceFragment.ARG_PROBLEM_ID, problemId);
        args.putLongArray(SolutionsChoiceFragment.ARG_VALUE_IDS, valueIds);
        args.putLongArray(ValuesRankFragment.ARG_SOLUTION_IDS, solutionsIds);
        fragment.setArguments(args);
        return fragment;
    }

    public CompareSolutionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            problemId = getArguments().getLong(ValuesChoiceFragment.ARG_PROBLEM_ID);
            valueIds = getArguments().getLongArray(SolutionsChoiceFragment.ARG_VALUE_IDS);
            solutionsIds = getArguments().getLongArray(ValuesRankFragment.ARG_SOLUTION_IDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_compare_solution, container, false);
        mTitleUp =(TextView) view.findViewById(R.id.tv_title_up);
        mTitleDown =(TextView) view.findViewById(R.id.tv_title_down);
        mDescriptionUp =(TextView) view.findViewById(R.id.tv_description_up);
        mDescriptionDown =(TextView) view.findViewById(R.id.tv_description_down);

        mSeekbar = (VerticalSeekBar) view.findViewById(R.id.mySeekBar);
        launchVersusRequest();
        return view;
    }
    public void launchVersusRequest(){
        // Instantiate the RequestQueue.
        JsonArrayRequest jReq = new JsonArrayRequest(Requests.getValuesProblemUrl(problemId),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                Log.e(TAG, response.get(i).toString());
                               /* Problem problem = JSONConverter.problemConverter(response.getJSONObject(i));
                                ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
                                problemDao.insertOrReplace(problem);*/
                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                        //notifyRefresh();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //mSwipeRefreshLayout.setRefreshing(false);

                Log.e(TAG,"VolleyError : "+ error.toString());

            }
        });
        RequestQueueSingleton.getInstance(this.getActivity().getApplicationContext()).addToRequestQueue(jReq);

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
