package com.logos.mvp.logosswipe.UI.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.activities.ValuesChoiceActivity;
import com.logos.mvp.logosswipe.UI.fragments.ValuesChoiceFragment;

import java.util.ArrayList;

import greendao.Problem;

/**
 * Created by Sylvain on 31/01/15.
 */
public class ProblemsChoiceAdapter extends  RecyclerView.Adapter<ProblemsChoiceAdapter.ViewHolder> {


    private ArrayList<Problem> mProblems;

    public ProblemsChoiceAdapter(ArrayList<Problem> problems){
        mProblems=problems;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mDescription;
        private Problem mProblem;

        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tv_title);
            mDescription = (TextView)v.findViewById(R.id.tv_description);
            v.setOnClickListener(this);

        }

        public void bindProblem(Problem problem) {
            mProblem = problem;
        }

        @Override
        public void onClick(View v) {
            if(mProblem != null){
                Intent nextIntent = new Intent(v.getContext(), ValuesChoiceActivity.class);
                nextIntent.putExtra(ValuesChoiceFragment.ARG_PROBLEM_ID, mProblem.getId());
                v.getContext().startActivity(nextIntent);
            }
        }
    }

    @Override
    public ProblemsChoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_item_problems_choice, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProblemsChoiceAdapter.ViewHolder viewHolder, int i) {
        Problem problem = mProblems.get(i);
        viewHolder.mTitle.setText(problem.getName());
        viewHolder.mDescription.setText(problem.getDescription());
        viewHolder.bindProblem(problem);

    }

    @Override
    public int getItemCount() {
        return mProblems == null ? 0 : mProblems.size();
    }

    public void setProblems(ArrayList<Problem> mProblems) {
        this.mProblems = mProblems;
    }

}
