package com.logos.mvp.logosswipe.UI.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;

import java.util.List;

import greendao.Value;

/**
 * Created by Sylvain on 31/01/15.
 */
public class ValueChoiceAdapter extends ArrayAdapter<Value>{

        Context mContext;
        int mLayoutResourceId;

        public ValueChoiceAdapter(Context context, int resource, List<Value> objects) {
            super(context, resource, objects);
            this.mContext=context;
            this.mLayoutResourceId=resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ValueHolder holder = null;

            if(row == null)
            {
                LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                row = inflater.inflate(mLayoutResourceId, parent, false);

                holder = new ValueHolder();
                holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

                row.setTag(holder);
            }
            else
            {
                holder = (ValueHolder)row.getTag();
            }
            holder.txtTitle.setText(getItem(position).getName());

            return row;
        }
        static class ValueHolder
        {
            TextView txtTitle;
        }
 }


