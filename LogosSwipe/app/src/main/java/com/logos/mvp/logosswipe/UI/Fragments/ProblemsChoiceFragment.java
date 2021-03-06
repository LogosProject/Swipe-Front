package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.adapters.ProblemsChoiceAdapter;
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

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ProblemsChoiceFragment extends Fragment {
    public static final String TAG="ProblemsChoiceFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private OnFragmentInteractionListener mListener;
    /**
     * The fragment's ListView/GridView.
     */
    private RecyclerView mRecyclerView;
    private ProblemsChoiceAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private SwipeRefreshLayout mSwipeRefreshLayout;


    private FloatingActionButton buttonNewProblem;


    public static ProblemsChoiceFragment newInstance(String param1, String param2) {
        ProblemsChoiceFragment fragment = new ProblemsChoiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProblemsChoiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
        QueryBuilder qb = problemDao.queryBuilder();
        List problems = qb.list();
        mAdapter = new ProblemsChoiceAdapter(new String("Problématiques"),new ArrayList<Problem>(problems),R.layout.item_header);
        launchRequest();

    }

    public void launchRequest(){
        // Instantiate the RequestQueue.
        JsonArrayRequest jReq = new JsonArrayRequest(Requests.getProblemsUrl(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Problem problem = JSONConverter.problemConverter(response.getJSONObject(i));
                                ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
                                problemDao.insertOrReplace(problem);
                            } catch (JSONException e) {
                                Log.e(TAG,e.toString());
                            }
                        }
                        notifyRefresh();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);

                Log.e(TAG,"VolleyError : "+ error.toString());

            }
        });
        RequestQueueSingleton.getInstance(this.getActivity().getApplicationContext()).addToRequestQueue(jReq);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problems_choice_list, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getActivity()).build());

        buttonNewProblem = (FloatingActionButton) view.findViewById(R.id.bt_new_problem);
        buttonNewProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreationDialog dialog = new CreationDialog();
                Bundle bdl = new Bundle();
                bdl.putSerializable(CreationDialog.ARG_MODE, CreationDialog.DIALOG_MODE.PROBLEM);
                dialog.setArguments(bdl);
                dialog.setTargetFragment(ProblemsChoiceFragment.this, 0);
                dialog.show(getFragmentManager(), CreationDialog.TAG);

            }
        });
        buttonNewProblem.attachToRecyclerView(mRecyclerView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                launchRequest();
            }
        });

        return view;
    }


    public void notifyRefresh(){
        ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
        QueryBuilder qb = problemDao.queryBuilder();
        List problems = qb.list();
        mSwipeRefreshLayout.setRefreshing(false);

        if(mAdapter!=null) {
            mAdapter.setmObjects(new ArrayList<Problem>(problems));
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
