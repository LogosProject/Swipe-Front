package com.logos.mvp.logosswipe.UI.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;

import java.util.ArrayList;

import greendao.Problem;
import greendao.Solution;
import greendao.Value;
import greendao.ValueScore;

/**
 * Created by Sylvain on 03/02/15.
 */
public class ValueRankAdapter extends GenericHeaderAdapter<Value, Problem> {

    private ArrayList<ValueScore> mValueScores;


    public ValueRankAdapter(Problem headerObject, ArrayList<Value> objects) {
        super(headerObject, objects, null, R.layout.header_item, R.layout.listview_item_value_score);
        mValueScores= new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mItemRessourceId, parent, false);
            return new MyItem(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mHeaderRessourceId, parent, false);
            return new VHHeader(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyItem && getmObjects() !=null) {
            MyItem item = (MyItem) holder;
            Value value = getItem(position);
            item.mTitle.setText(value.getName());
            item.mValue=value;
        }else if(holder instanceof GenericHeaderAdapter<?,?>.VHHeader && getmHeaderObject() != null){
            GenericHeaderAdapter.VHHeader item = (GenericHeaderAdapter.VHHeader) holder;
            Problem problem = getmHeaderObject();
            item.mTitle.setText(problem.getName());
            item.mDescription.setText(problem.getDescription());
        }
    }

    class MyItem extends RecyclerView.ViewHolder{
        TextView mTitle;
        SeekBar mRanker;
        Value mValue;
        public MyItem(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mRanker = (SeekBar) itemView.findViewById(R.id.sk_score);
            mRanker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    boolean found= false;
                    for(int i = 0; i<mValueScores.size();i++){
                        if(mValueScores.get(i).getValueId()==mValue.getId()){
                            mValueScores.get(i).setScore((double) seekBar.getProgress());
                            found=true;
                            break;
                        }
                    }
                    if(!found){
                        mValueScores.add(new ValueScore(null,(double) seekBar.getProgress(), mValue.getId(),null));
                    }

                }
            });
        }


    }

}
