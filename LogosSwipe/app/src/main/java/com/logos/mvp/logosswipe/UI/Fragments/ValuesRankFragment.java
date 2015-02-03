package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.ValuesRankActivity;
import com.logos.mvp.logosswipe.UI.activities.VersusActivity;
import com.logos.mvp.logosswipe.UI.adapters.SolutionChoiceAdapter;
import com.logos.mvp.logosswipe.UI.adapters.ValueChoiceAdapter;
import com.logos.mvp.logosswipe.UI.adapters.ValueRankAdapter;
import com.logos.mvp.logosswipe.UI.dialogs.CreationDialog;
import com.melnykov.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import greendao.Problem;
import greendao.ProblemDao;
import greendao.Solution;
import greendao.SolutionDao;
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
public class ValuesRankFragment extends Fragment implements AbsListView.OnItemClickListener {
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

               /* // TODO : do
                Intent nextIntent = new Intent(v.getContext(), ValuesRankActivity.class);
                nextIntent.putExtra(ValuesChoiceFragment.ARG_PROBLEM_ID, mProblemId);
                /*nextIntent.putExtra(SolutionsChoiceFragment.ARG_VALUE_IDS, mValueIds);
                long[] array = new long[mAdapter.getmSelectedObjects().size()];
                for(int i = 0; i< mAdapter.getmSelectedObjects().size();i++){
                    array[i]=mAdapter.getmSelectedObjects().get(i).getId();
                }
                nextIntent.putExtra(SolutionsChoiceFragment.ARG_VALUE_IDS, array);
                v.getContext().startActivity(nextIntent);*/


            }
        });
        floattingButton.attachToRecyclerView(mRecyclerView);

        return view;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    View.OnClickListener handlerButtonSelect = new View.OnClickListener() {
        public void onClick(View v) {
            Intent nextIntent = new Intent(ValuesRankFragment.this.getActivity(), VersusActivity.class);
            startActivity(nextIntent);
        }
    };

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
      /*  View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }*/
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
