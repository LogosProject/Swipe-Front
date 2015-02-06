package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.SolutionsPresentationActivity;
import com.logos.mvp.logosswipe.UI.activities.ValuesChoiceActivity;
import com.logos.mvp.logosswipe.UI.activities.ValuesRankActivity;
import com.logos.mvp.logosswipe.UI.views.SolutionView;
import com.logos.mvp.logosswipe.network.RequestQueueSingleton;
import com.logos.mvp.logosswipe.utils.JSONConverter;
import com.logos.mvp.logosswipe.utils.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import greendao.Problem;
import greendao.ProblemDao;
import greendao.Solution;
import greendao.SolutionDao;
import greendao.Value;
import greendao.ValueDao;
import greendao.Versus;
import greendao.VersusDao;

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
    public static final String PREF_CURRENT_VERSUS = "PREF_CURRENT_VERSUS" ;
    public static final String KEY_CURRENT_VERSUS = "KEY_CURRENT_VERSUS" ;



    private long problemId = -1L;
    private long[] valueIds = new long[0];
    private long[] solutionsIds = new long[0];

    private SolutionView mSolutionViewUp;
    private SolutionView mSolutionViewDown;
    private ProgressBar mProgress;
    private SeekBar mSeekbar;
    private Button mNextVersus;
    private TextView mValueTitle;

    private OnFragmentInteractionListener mListener;

    float[] hsv = {150.0f,0.5f,1.0f};


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
        mSolutionViewUp =(SolutionView) view.findViewById(R.id.solution_up);
        mSolutionViewDown =(SolutionView) view.findViewById(R.id.solution_down);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mSeekbar = (SeekBar) view.findViewById(R.id.mySeekBar);
        mValueTitle = (TextView) view.findViewById(R.id.tv_value);
        mSeekbar.setMax(100);
        mSeekbar.setProgress(50);
        mSolutionViewUp.setBackgroundColor(Color.HSVToColor(hsv));

        mSolutionViewDown.setBackgroundColor(Color.HSVToColor(hsv));
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float saturation = ((100.0f-progress)/100.0f);
                hsv[1]=saturation;
                mSolutionViewUp.setBackgroundColor(Color.HSVToColor(hsv));
                saturation = ((progress)/100.0f);
                hsv[1]=saturation;
                mSolutionViewDown.setBackgroundColor(Color.HSVToColor(hsv));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mNextVersus =(Button) view.findViewById(R.id.bt_next_versus);
        mNextVersus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"NEXT VERSUS");
                postVersusResponse();
            }
        });
        launchVersusRequest();
        showLoading();
        return view;
    }
    public void showContent(){
        mSolutionViewDown.setVisibility(View.VISIBLE);
        mSolutionViewUp.setVisibility(View.VISIBLE);
        mSeekbar.setVisibility(View.VISIBLE);
        mNextVersus.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    public void showLoading(){
        mSolutionViewDown.setVisibility(View.GONE);
        mSolutionViewUp.setVisibility(View.GONE);
        mSeekbar.setVisibility(View.GONE);
        mNextVersus.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setIndeterminate(true);
    }
    public double getScore(){
        double res = (((double)mSeekbar.getProgress())/10.0f)-5.0f;
        Log.d(TAG,"Score : " +res);
        return res;
    }
    public void postVersusResponse(){
        showLoading();
        SharedPreferences preferences = App.getInstance().getSharedPreferences(CompareSolutionFragment.PREF_CURRENT_VERSUS, Context.MODE_PRIVATE);
        long versusId = preferences.getLong(CompareSolutionFragment.KEY_CURRENT_VERSUS, -1L);
        if(versusId == -1L){
            Log.e(TAG,"ERROR : versusId null");
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST,Requests.postVersusScoreUrl(versusId),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        Log.d("PostVersusResponse", response.toString());
                        launchVersusRequest();
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
                params.put("response",String.valueOf(getScore()));
                params.put("userId",Requests.USER_ID);


                return params;
            }
        };
        RequestQueueSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(postRequest);

    }

    public void launchVersusRequest(){
        // Instantiate the RequestQueue.
        final StringRequest jReq = new StringRequest(Requests.getNextVersusProblem(problemId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(!response.isEmpty()) {
                                Versus versus = JSONConverter.versusConverter(new JSONObject(response));
                                SolutionDao solutionDao = App.getSession().getSolutionDao();
                                Solution solution1 = solutionDao.load(versus.getSolution1Id());
                                Solution solution2 = solutionDao.load(versus.getSolution2Id());
                                mSolutionViewUp.setTitle(solution1.getName());
                                mSolutionViewUp.setContent(solution1.getDescription());
                                mSolutionViewDown.setTitle(solution2.getName());
                                mSolutionViewDown.setContent(solution2.getDescription());
                                VersusDao versusDao= App.getSession().getVersusDao();
                                versusDao.insertOrReplace(versus);
                                SharedPreferences preferences = App.getInstance().getSharedPreferences(PREF_CURRENT_VERSUS, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong(KEY_CURRENT_VERSUS,versus.getId());
                                editor.commit();

                                ValueDao valueDao = App.getSession().getValueDao();
                                Value value = valueDao.load(versus.getValueID());
                                mValueTitle.setText(value.getName());
                                if(mListener != null) {
                                    mListener.onNextVersusReceived(versus);
                                }
                            }else{
                                Intent nextIntent = new Intent(getActivity(), SolutionsPresentationActivity.class);
                                //nextIntent.putExtra(ValuesChoiceFragment.ARG_PROBLEM_ID, mProblemId);
                                //nextIntent.putExtra(SolutionsChoiceFragment.ARG_VALUE_IDS, mValueIds);
                                //long[] array = new long[mAdapter.getmSelectedObjects().size()];
                                //for(int i = 0; i< mAdapter.getmSelectedObjects().size();i++){
                                //    array[i]=mAdapter.getmSelectedObjects().get(i).getId();
                                //}
                                //nextIntent.putExtra(ValuesRankFragment.ARG_SOLUTION_IDS, array);
                                getActivity().startActivity(nextIntent);
                            }
                            showContent();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //notifyRefresh();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //mSwipeRefreshLayout.setRefreshing(false);

                Log.e(TAG,"VolleyError LaunchVersusRequest : "+ error.toString());

            }
        });
        Log.d(TAG,jReq.getUrl().toString());
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
        public void onNextVersusReceived(Versus versus);
    }

}
