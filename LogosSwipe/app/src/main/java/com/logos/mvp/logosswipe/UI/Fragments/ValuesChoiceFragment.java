package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.SolutionsChoiceActivity;
import com.logos.mvp.logosswipe.UI.adapters.ProblemsChoiceAdapter;
import com.logos.mvp.logosswipe.UI.adapters.ValueChoiceAdapter;
import com.logos.mvp.logosswipe.network.RequestQueueSingleton;
import com.logos.mvp.logosswipe.utils.JSONConverter;
import com.logos.mvp.logosswipe.utils.Requests;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
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
public class ValuesChoiceFragment extends Fragment implements AbsListView.OnItemClickListener {

    public static final String TAG="ValuesChoiceFragment";

    public static final String ARG_PROBLEM_ID = "ARG_PROBLEM_ID";

    // TODO: Rename and change types of parameters
    private Long mProblemID;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ValueChoiceAdapter mAdapter;

    private Button buttonNewValue;
    private Button buttonValuesSelected;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_values_choice_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        buttonNewValue = (Button) view.findViewById(R.id.values_choice_new_value_button);
        buttonNewValue.setOnClickListener(handlerButtonNewValue);

        buttonValuesSelected = (Button) view.findViewById(R.id.values_choice_selected_button);
        buttonValuesSelected.setOnClickListener(handlerButtonSelect);
        ValueDao valueDao = App.getInstance().getSession().getValueDao();
        QueryBuilder qb = valueDao.queryBuilder().where(ValueDao.Properties.ProblemId.eq(mProblemID));
        List values = qb.list();
        mAdapter = new ValueChoiceAdapter(getActivity(),
                R.layout.listview_item_values_choice, values);
        mListView.setAdapter(mAdapter);
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
        return view;
    }
    public void notifyRefresh(){
        ValueDao valueDao = App.getInstance().getSession().getValueDao();
        QueryBuilder qb = valueDao.queryBuilder().where(ValueDao.Properties.ProblemId.eq(mProblemID));
        List values = qb.list();
        if(mAdapter!=null) {
            mAdapter.clear();
            mAdapter.addAll(values);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }


    View.OnClickListener handlerButtonNewValue = new View.OnClickListener() {
        public void onClick(View v) {
            // TODO
        }
    };

    View.OnClickListener handlerButtonSelect = new View.OnClickListener() {
        public void onClick(View v) {
            Intent nextIntent = new Intent(ValuesChoiceFragment.this.getActivity(), SolutionsChoiceActivity.class);
            startActivity(nextIntent);
        }
    };

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
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
