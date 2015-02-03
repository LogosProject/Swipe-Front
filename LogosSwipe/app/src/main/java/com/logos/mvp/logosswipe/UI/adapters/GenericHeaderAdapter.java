package com.logos.mvp.logosswipe.UI.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;

import java.util.ArrayList;

/**
 * Created by Sylvain on 03/02/15.
 */
public abstract class GenericHeaderAdapter<T,H>  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ArrayList<T> getmObjects() {
        return mObjects;
    }

    public void setmObjects(ArrayList<T> mObjects) {
        this.mObjects = mObjects;
    }

    public ArrayList<T> mObjects;

    public ArrayList<T> getmSelectedObjects() {

        return mSelectedObjects;
    }

    public void setmSelectedObjects(ArrayList<T> mSelectedObjects) {
        this.mSelectedObjects = mSelectedObjects;
    }

    public ArrayList<T> mSelectedObjects;


    public interface HeaderAdapterInterface {
        public void onItemsSelected();
    }
    HeaderAdapterInterface mListener;

    private H mHeaderObject;

    int mHeaderRessourceId;
    int mItemRessourceId;
    public GenericHeaderAdapter(H headerObject,ArrayList<T> objects, HeaderAdapterInterface fragment, int headerRessource, int itemRessource) {
        this.mHeaderObject=headerObject;
        this.mObjects=objects;
        this.mHeaderRessourceId=headerRessource;
        this.mItemRessourceId=itemRessource;
        this.mSelectedObjects =new ArrayList<>();
        this.mListener = fragment;

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
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mObjects.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private T getItem(int position) {
        return mObjects.get(position-1);
    }


    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mDescription;
        T object;
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
                mSelectedObjects.remove(object);
            }else{
                v.setSelected(true);
                mSelectedObjects.add(object);
                v.setBackgroundColor(v.getResources().getColor(R.color.selection_item));
            }
            mListener.onItemsSelected();
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mDescription;

        public VHHeader(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mDescription = (TextView)itemView.findViewById(R.id.tv_description);
        }
    }
}



