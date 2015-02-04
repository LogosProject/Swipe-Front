package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.ValuesRankActivity;
import com.logos.mvp.logosswipe.UI.activities.VersusActivity;
import com.logos.mvp.logosswipe.UI.adapters.ValueRankAdapter;
import com.logos.mvp.logosswipe.network.RequestQueueSingleton;
import com.logos.mvp.logosswipe.utils.Requests;
import com.melnykov.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import greendao.Problem;
import greendao.ProblemDao;
import greendao.Value;
import greendao.ValueDao;

public class ValuesRankFragment extends Fragment{
    public static final String TAG="ValuesRankFragment";
    public static final String ARG_SOLUTION_IDS="ARG_SOLUTION_IDS";

    public long getProblemId() {
        return mProblemId;
    }

    private long mProblemId;
    private long[] mValueIds;
    private long[] mSolutionIds;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ValueRankAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    private FloatingActionButton floattingButton;

    private int mNbValueScoresToSend;

    public static ValuesRankFragment newInstance(long problemID, long[] valueIds, long[] solutionIds) {
        ValuesRankFragment fragment = new ValuesRankFragment();
        Bundle args = new Bundle();
        args.putLong(ValuesChoiceFragment.ARG_PROBLEM_ID, problemID);
        args.putLongArray(SolutionsChoiceFragment.ARG_VALUE_IDS, valueIds);
        args.putLongArray(ARG_SOLUTION_IDS, solutionIds);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ValuesRankFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNbValueScoresToSend =0;
        if (getArguments() != null) {
            mProblemId = getArguments().getLong(ValuesChoiceFragment.ARG_PROBLEM_ID);
            mValueIds = getArguments().getLongArray(SolutionsChoiceFragment.ARG_VALUE_IDS);
            mSolutionIds = getArguments().getLongArray(ARG_SOLUTION_IDS);
        }

        ValueDao valueDao = App.getInstance().getSession().getValueDao();
        ArrayList<WhereCondition> whereConditions = new ArrayList<>();
        QueryBuilder qb = valueDao.queryBuilder();
        if(mValueIds.length>1) {
            for (int i = 0; i < mValueIds.length; i++) {
                whereConditions.add(ValueDao.Properties.Id.eq(mValueIds[i]));
            }
            WhereCondition[] conditionsArray = new WhereCondition[whereConditions.size()];
            conditionsArray = whereConditions.toArray(conditionsArray);
            qb.whereOr(conditionsArray[0], conditionsArray[1], Arrays.copyOfRange(conditionsArray, 2, conditionsArray.length));
        }else if(mValueIds.length==1){
            qb.where(ValueDao.Properties.Id.eq(mValueIds[0]));
        }
        List values = qb.list();

        ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
        QueryBuilder qb2 = problemDao.queryBuilder().where(ProblemDao.Properties.Id.eq(mProblemId));
        Problem problem = (Problem)qb2.list().get(0);

        mAdapter = new ValueRankAdapter(problem,new ArrayList<Value>(values));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_valuesrank, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getActivity()).build());
        // Set the adapter

        floattingButton = (FloatingActionButton) view.findViewById(R.id.bt_new_value);

        floattingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNbValueScoresToSend =mAdapter.getmValueScores().size();
                //TODO : place this again when the server is working
                publishRanking();


            }
        });
        floattingButton.attachToRecyclerView(mRecyclerView);

        return view;
    }

    public void publishRanking(){
        if(mNbValueScoresToSend >0) {
            StringRequest postRequest = new StringRequest(Request.Method.POST, Requests.postValueScoreUrl(mAdapter.getmValueScores().get(mNbValueScoresToSend - 1).getValueId()),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (mNbValueScoresToSend > 0) {
                                Log.d(TAG, response);
                                mNbValueScoresToSend--;
                                publishRanking();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("score", String.valueOf(mAdapter.getmValueScores().get(mNbValueScoresToSend - 1).getScore()));
                    //TODO : correct this
                    params.put("userId", "1");

                    return params;
                }
            };
            RequestQueueSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(postRequest);
        }else{
            Log.d(TAG, "finished");
            Intent nextIntent = new Intent(getActivity(), VersusActivity.class);
            nextIntent.putExtra(ValuesChoiceFragment.ARG_PROBLEM_ID, mProblemId);
            nextIntent.putExtra(SolutionsChoiceFragment.ARG_VALUE_IDS, mValueIds);
            long[] array = new long[mAdapter.getmSelectedObjects().size()];
            for(int i = 0; i< mAdapter.getmSelectedObjects().size();i++){
                array[i]=mAdapter.getmSelectedObjects().get(i).getId();
            }
            nextIntent.putExtra(ValuesRankFragment.ARG_SOLUTION_IDS, array);
            getActivity().startActivity(nextIntent);

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
