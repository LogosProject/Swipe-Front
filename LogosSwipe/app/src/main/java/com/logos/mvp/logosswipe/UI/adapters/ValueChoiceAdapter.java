package com.logos.mvp.logosswipe.UI.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;

import java.util.ArrayList;

import greendao.Value;

/**
 * Created by Sylvain on 31/01/15.
 */
public class ValueChoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<Value> getValues() {
        return mValues;
    }

    ArrayList<Value> mValues;

    public ArrayList<Value> getSelectedValues() {
        return mSelectedValues;
    }

    ArrayList<Value> mSelectedValues;


    public interface ValueChoiceAdapterInterface {
        public void onItemsSelected();
    }
    ValueChoiceAdapterInterface mFragmentListener;

    public ValueChoiceAdapter(ArrayList<Value> values, ValueChoiceAdapterInterface fragment) {
       this.mValues=values;
       mSelectedValues=new ArrayList<>();
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
        if ( mValues !=null) {
            VHItem item = (VHItem) holder;
            Value value = getItem(position);
            item.mTitle.setText(value.getName());
            item.mDescription.setText(value.getDescription());
            item.mValue=value;
        }
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }


    private Value getItem(int position) {
        return mValues.get(position);

    }

    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitle;
        TextView mDescription;
        Value mValue;
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
                mSelectedValues.remove(mValue);
            }else{
                v.setSelected(true);
                mSelectedValues.add(mValue);
                v.setBackgroundColor(v.getResources().getColor(R.color.selection_item));
            }
            mFragmentListener.onItemsSelected();
        }
    }

    public void setValues(ArrayList<Value> mValues) {
        this.mValues = mValues;
    }
 }


