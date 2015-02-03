package com.logos.mvp.logosswipe.UI.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;

import java.util.ArrayList;

import greendao.Solution;

/**
 * Created by Sylvain on 31/01/15.
 */
public class SolutionChoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<Solution> getSolutions() {
        return mSolutions;
    }

    ArrayList<Solution> mSolutions;

    public ArrayList<Solution> getSelectedSolutions() {
        return mSelectedSolutions;
    }

    ArrayList<Solution> mSelectedSolutions;


    public interface SolutionChoiceAdapterInterface {
        public void onItemsSelected();
    }
    SolutionChoiceAdapterInterface mFragmentListener;

    public SolutionChoiceAdapter(ArrayList<Solution> values, SolutionChoiceAdapterInterface fragment) {
        this.mSolutions =values;
        mSelectedSolutions =new ArrayList<>();
        mFragmentListener= fragment;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item_choice, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new VHItem(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( mSolutions !=null) {
            VHItem item = (VHItem) holder;
            Solution solution = getItem(position);
            item.mTitle.setText(solution.getName());
            item.mDescription.setText(solution.getDescription());
            item.mSolution =solution;
        }
    }

    @Override
    public int getItemCount() {
        return mSolutions == null ? 0 : mSolutions.size();
    }


    private Solution getItem(int position) {
        return mSolutions.get(position);

    }

    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitle;
        TextView mDescription;
        Solution mSolution;
        public VHItem(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mDescription = (TextView)itemView.findViewById(R.id.tv_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.isSelected()){
                v.setSelected(false);
                v.setBackgroundColor(v.getResources().getColor(android.R.color.transparent));
                mSelectedSolutions.remove(mSolution);
            }else{
                v.setSelected(true);
                mSelectedSolutions.add(mSolution);
                v.setBackgroundColor(v.getResources().getColor(R.color.selection_item));
            }
            mFragmentListener.onItemsSelected();
        }
    }

    public void setSolutions(ArrayList<Solution> solutions) {
        this.mSolutions = solutions;
    }
}


