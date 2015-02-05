package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.SolutionsChoiceActivity;
import com.logos.mvp.logosswipe.UI.adapters.GenericHeaderAdapter;
import com.logos.mvp.logosswipe.UI.adapters.ValueChoiceAdapter;
import com.logos.mvp.logosswipe.UI.dialogs.CreationDialog;
import com.logos.mvp.logosswipe.network.RequestQueueSingleton;
import com.logos.mvp.logosswipe.utils.JSONConverter;
import com.logos.mvp.logosswipe.utils.Requests;
import com.melnykov.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import greendao.Problem;
import greendao.ProblemDao;
import greendao.Value;
import greendao.ValueDao;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ValuesChoiceFragment extends Fragment implements GenericHeaderAdapter.HeaderAdapterInterface{

    public static final String TAG="ValuesChoiceFragment";

    public static final String ARG_PROBLEM_ID = "ARG_PROBLEM_ID";

    public Long getProblemID() {
        return mProblemID;
    }

    // TODO: Rename and change types of parameters
    private Long mProblemID;

    private OnFragmentInteractionListener mListener;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ValueChoiceAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton floattingButton;

    private TextView mTvTitle;
    private TextView mTvDescription;

    private boolean isItemSelected =false;

    public static ValuesChoiceFragment newInstance(Long problemId) {
        ValuesChoiceFragment fragment = new ValuesChoiceFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PROBLEM_ID, problemId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ValuesChoiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mProblemID = getArguments().getLong(ARG_PROBLEM_ID);
        }
        ValueDao valueDao = App.getInstance().getSession().getValueDao();
        QueryBuilder qb = valueDao.queryBuilder().where(ValueDao.Properties.ProblemId.eq(mProblemID));
        List values = qb.list();
        ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
        QueryBuilder qb2 = problemDao.queryBuilder().where(ProblemDao.Properties.Id.eq(mProblemID));
        Problem problem = (Problem)qb2.list().get(0);
        mAdapter = new ValueChoiceAdapter(problem,new ArrayList<Value>(values),this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_values_choice_list, container, false);
        ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
        QueryBuilder qb = problemDao.queryBuilder().where(ProblemDao.Properties.Id.eq(mProblemID));
        Problem problem = (Problem)qb.list().get(0);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_value_choice);

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
                if(!isItemSelected()) {
                    CreationDialog dialog = new CreationDialog();
                    Bundle bdl = new Bundle();
                    bdl.putSerializable(CreationDialog.ARG_MODE, CreationDialog.DIALOG_MODE.VALUE);
                    dialog.setArguments(bdl);
                    dialog.setTargetFragment(ValuesChoiceFragment.this, 0);
                    dialog.show(getFragmentManager(), CreationDialog.TAG);
                }else{
                    Intent nextIntent = new Intent(v.getContext(), SolutionsChoiceActivity.class);
                    nextIntent.putExtra(ValuesChoiceFragment.ARG_PROBLEM_ID, mProblemID);
                    long[] array = new long[mAdapter.getmSelectedObjects().size()];
                    for(int i = 0; i< mAdapter.getmSelectedObjects().size();i++){
                        array[i]=mAdapter.getmSelectedObjects().get(i).getId();
                    }
                    nextIntent.putExtra(SolutionsChoiceFragment.ARG_VALUE_IDS, array);
                    v.getContext().startActivity(nextIntent);
                }

            }
        });
        floattingButton.attachToRecyclerView(mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                launchRequest();
            }
        });
        //buttonValuesSelected = (Button) view.findViewById(R.id.values_choice_selected_button);
        //buttonValuesSelected.setOnClickListener(handlerButtonSelect);
        launchRequest();

        return view;
    }
    public void launchRequest(){
        // Instantiate the RequestQueue.
        JsonArrayRequest jReq = new JsonArrayRequest(Requests.getValuesProblemUrl(mProblemID),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Value value = JSONConverter.valueConverter(response.getJSONObject(i),mProblemID);
                                ValueDao valueDao = App.getInstance().getSession().getValueDao();
                                valueDao.insertOrReplace(value);
                                Log.d(TAG,value.getName());
                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                        notifyRefresh();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"VolleyError : "+ error.toString());

            }
        });
        RequestQueueSingleton.getInstance(this.getActivity().getApplicationContext()).addToRequestQueue(jReq);
    }

    public void notifyRefresh(){
        ValueDao valueDao = App.getInstance().getSession().getValueDao();
        QueryBuilder qb = valueDao.queryBuilder().where(ValueDao.Properties.ProblemId.eq(mProblemID));
        List values = qb.list();
        mSwipeRefreshLayout.setRefreshing(false);
        if(mAdapter!=null) {
            mAdapter.setmObjects(new ArrayList<Value>(values));
            mAdapter.notifyDataSetChanged();
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

    public boolean isItemSelected() {
        return isItemSelected;
    }

    public void resetSelection(){
        ArrayList<Value> temp = mAdapter.getmObjects();
        Problem temp2 = mAdapter.getmHeaderObject();
        mAdapter=new ValueChoiceAdapter(temp2,temp,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        floattingButton.setImageResource(R.drawable.ic_content_add);
        isItemSelected=false;
    }

    @Override
    public void onItemsSelected() {
        if(mAdapter.getmSelectedObjects().isEmpty()){
            floattingButton.setImageResource(R.drawable.ic_content_add);
            isItemSelected=false;
        }else{
            floattingButton.setImageResource(R.drawable.ic_navigation_check);
            isItemSelected=true;
        }
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
        public void onFragmentInteraction(String id);
    }

}
