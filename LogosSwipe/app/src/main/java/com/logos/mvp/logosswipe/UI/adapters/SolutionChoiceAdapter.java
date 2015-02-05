package com.logos.mvp.logosswipe.UI.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logos.mvp.logosswipe.R;

import java.util.ArrayList;

import greendao.Problem;
import greendao.Solution;

/**
 * Created by Sylvain on 31/01/15.
 */
public class SolutionChoiceAdapter extends GenericHeaderAdapter<Solution, Problem> {


    public SolutionChoiceAdapter(Problem problem,ArrayList<Solution> values, GenericHeaderAdapter.HeaderAdapterInterface fragment) {
        super(problem,values,fragment,R.layout.item_header,R.layout.listview_item_choice);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mItemRessourceId, parent, false);
            return new VHItem(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mHeaderRessourceId, parent, false);
            return new VHHeader(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GenericHeaderAdapter<?,?>.VHItem && getmObjects() !=null) {
            GenericHeaderAdapter.VHItem item = (GenericHeaderAdapter.VHItem) holder;
            Solution value = getItem(position);
            item.mTitle.setText(value.getName());
            item.mDescription.setText(value.getDescription());
            item.mObject=value;
        }else if(holder instanceof GenericHeaderAdapter<?,?>.VHHeader && getmHeaderObject() != null){
            GenericHeaderAdapter.VHHeader item = (GenericHeaderAdapter.VHHeader) holder;
            Problem problem = getmHeaderObject();
            item.mTitle.setText(problem.getName());
            item.mDescription.setText(problem.getDescription());
        }
    }
}


