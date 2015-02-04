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
public class ProblemsChoiceAdapter extends  GenericHeaderAdapter<Problem,String> {

    public ProblemsChoiceAdapter(String headerObject, ArrayList<Problem> objects, int headerRessource) {
        super(headerObject, objects, null, headerRessource, 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listview_item_problems_choice, parent, false);
            return new ViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mHeaderRessourceId, parent, false);
            return new VHHeader(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder && getmObjects() !=null) {
            ViewHolder item = (ViewHolder) holder;
            Problem problem = getItem(position);
            item.mTitle.setText(problem.getName());
            item.mDescription.setText(problem.getDescription());
            item.bindProblem(problem);

        }else if(holder instanceof GenericHeaderAdapter<?,?>.VHHeader && getmHeaderObject() != null){
            GenericHeaderAdapter.VHHeader item = (GenericHeaderAdapter.VHHeader) holder;
            item.mTitle.setText(getmHeaderObject());
            item.mDescription.setText("Sélectionnez ou créez une problématique ");
        }
    }


}
