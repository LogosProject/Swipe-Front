package com.logos.mvp.logosswipe.UI.fragments;

import android.app.Activity;
import android.net.Uri;
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
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.VersusActivity;
import com.logos.mvp.logosswipe.UI.adapters.CommentAdapter;
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
import greendao.Comment;
import greendao.CommentDao;
import greendao.Problem;
import greendao.ProblemDao;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DebateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DebateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebateFragment extends Fragment {

    private static final String TAG = "DebateFragment";

    private OnFragmentInteractionListener mListener;
    private CommentAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar mProgress;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton floattingButton;


    // TODO: Rename and change types and number of parameters
    public static DebateFragment newInstance(String param1, String param2) {
        DebateFragment fragment = new DebateFragment();
        return fragment;
    }

    public DebateFragment() {
        // Required empty public constructor
    }

    public long getCurrentVersusId(){
        if(getActivity() != null && getActivity() instanceof VersusActivity){
            return ((VersusActivity)getActivity()).getmCurrentVersus();
        }else{
            return -1L;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommentDao commentDao = App.getInstance().getSession().getCommentDao();
        QueryBuilder qb  = commentDao.queryBuilder().where(CommentDao.Properties.VersusId.eq(getCurrentVersusId()));
        List comment = qb.list();
        mAdapter = new CommentAdapter(new ArrayList<Comment>(comment),R.layout.listview_item_comment);
    }

    public void requestComments(){
        // Instantiate the RequestQueue.
        JsonArrayRequest jReq = new JsonArrayRequest(Requests.getVersusComments(((VersusActivity)(getActivity())).getmCurrentVersus()),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Comment comment = JSONConverter.commentConverter(response.getJSONObject(i));
                                CommentDao commentDao = App.getInstance().getSession().getCommentDao();
                                commentDao.insertOrReplace(comment);

                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                        reloadComments();
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

    public void reloadComments(){
        CommentDao commentDao = App.getInstance().getSession().getCommentDao();
        QueryBuilder qb = commentDao.queryBuilder().where(CommentDao.Properties.VersusId.eq(getCurrentVersusId()));
        List comments = qb.list();
        mSwipeRefreshLayout.setRefreshing(false);
        if(mAdapter!=null) {
            mAdapter.setItems(new ArrayList<Comment>(comments));
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debate, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_fragment_debate);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getActivity()).build());

        mProgress=(ProgressBar)view.findViewById(R.id.progress);
        floattingButton = (FloatingActionButton) view.findViewById(R.id.bt_new_comment);

        floattingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    CreationDialog dialog = new CreationDialog();
                    Bundle bdl = new Bundle();
                    //TODO : implement CreationDialog
                    bdl.putSerializable(CreationDialog.ARG_MODE, CreationDialog.DIALOG_MODE.SOLUTION);
                    dialog.setArguments(bdl);
                    dialog.show(getActivity().getSupportFragmentManager(), CreationDialog.TAG);

            }
        });
        floattingButton.attachToRecyclerView(mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestComments();
            }
        });
        showLoading();
        return view;
    }

    public void showContent(){
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setEnabled(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        floattingButton.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    public void showLoading(){
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.setVisibility(View.GONE);
        floattingButton.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setIndeterminate(true);
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
