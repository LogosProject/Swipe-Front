package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.logos.mvp.logosswipe.UI.activities.ValuesRankActivity;
import com.logos.mvp.logosswipe.UI.adapters.SolutionChoiceAdapter;
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
import greendao.Solution;
import greendao.SolutionDao;
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
public class SolutionsChoiceFragment extends Fragment implements SolutionChoiceAdapter.SolutionChoiceAdapterInterface {
    public static final String TAG="SolutionsChoiceFragment";

    public static final String ARG_VALUE_IDS = "ARG_VALUE_IDS";

    public long getProblemId() {
        return mProblemId;
    }

    // TODO: Rename and change types of parameters
    private long mProblemId;
    private long[] mValueIds;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private SolutionChoiceAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton floattingButton;

    private TextView mTvTitle;
    private TextView mTvDescription;

    private boolean isItemSelected =false;


    // TODO: Rename and change types of parameters
    public static SolutionsChoiceFragment newInstance(Long param1, long[] param2) {
        SolutionsChoiceFragment fragment = new SolutionsChoiceFragment();
        Bundle args = new Bundle();
        args.putLong(ValuesChoiceFragment.ARG_PROBLEM_ID, param1);
        args.putLongArray(ARG_VALUE_IDS, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SolutionsChoiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mProblemId = getArguments().getLong(ValuesChoiceFragment.ARG_PROBLEM_ID);
            mValueIds = getArguments().getLongArray(ARG_VALUE_IDS);
        }
        SolutionDao solutionDao = App.getInstance().getSession().getSolutionDao();
        QueryBuilder qb = solutionDao.queryBuilder().where(ValueDao.Properties.ProblemId.eq(mProblemId));
        List values = qb.list();

        mAdapter = new SolutionChoiceAdapter(new ArrayList<Solution>(values),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solution, container, false);
        ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
        QueryBuilder qb = problemDao.queryBuilder().where(ProblemDao.Properties.Id.eq(mProblemId));
        Problem problem = (Problem)qb.list().get(0);

        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvDescription = (TextView) view.findViewById(R.id.tv_description);
        mTvTitle.setText(problem.getName());
        mTvDescription.setText(problem.getDescription());
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
                    bdl.putSerializable(CreationDialog.ARG_MODE, CreationDialog.DIALOG_MODE.SOLUTION);
                    dialog.setArguments(bdl);
                    dialog.setTargetFragment(SolutionsChoiceFragment.this, 0);
                    dialog.show(getFragmentManager(), CreationDialog.TAG);
                }else{
                   // TODO : do
                    Intent nextIntent = new Intent(v.getContext(), ValuesRankActivity.class);
                    nextIntent.putExtra(ValuesChoiceFragment.ARG_PROBLEM_ID, mProblemId);
                    nextIntent.putExtra(SolutionsChoiceFragment.ARG_VALUE_IDS, mValueIds);
                    long[] array = new long[mAdapter.getSelectedSolutions().size()];
                    for(int i = 0; i< mAdapter.getSelectedSolutions().size();i++){
                        array[i]=mAdapter.getSelectedSolutions().get(i).getId();
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
        JsonArrayRequest jReq = new JsonArrayRequest(Requests.getSolutionsProblemUrl(mProblemId),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Solution solution = JSONConverter.solutionConverter(response.getJSONObject(i), mProblemId);
                                SolutionDao solutionDao = App.getInstance().getSession().getSolutionDao();
                                solutionDao.insertOrReplace(solution);
                                Log.d(TAG, solution.getName());
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
        SolutionDao solutionDao = App.getInstance().getSession().getSolutionDao();
        QueryBuilder qb = solutionDao.queryBuilder().where(ValueDao.Properties.ProblemId.eq(mProblemId));
        List solutions = qb.list();
        mSwipeRefreshLayout.setRefreshing(false);
        if(mAdapter!=null) {
            mAdapter.setSolutions(new ArrayList<Solution>(solutions));
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

    public void resetSelection(){
        ArrayList<Solution> temp = mAdapter.getSolutions();
        mAdapter=new SolutionChoiceAdapter(temp,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        floattingButton.setImageResource(R.drawable.ic_content_add);
        isItemSelected=false;
    }

    @Override
    public void onItemsSelected() {
        if(mAdapter.getSelectedSolutions().isEmpty()){
            floattingButton.setImageResource(R.drawable.ic_content_add);
            isItemSelected=false;
        }else{
            floattingButton.setImageResource(R.drawable.ic_navigation_check);
            isItemSelected=true;
        }
    }

    public boolean isItemSelected() {
        return isItemSelected;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
