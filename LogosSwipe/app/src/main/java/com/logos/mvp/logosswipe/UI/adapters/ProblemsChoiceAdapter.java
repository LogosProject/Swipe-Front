package com.logos.mvp.logosswipe.UI.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;

import java.util.ArrayList;
import java.util.List;

import greendao.Problem;

/**
 * Created by Sylvain on 31/01/15.
 */
public class ProblemsChoiceAdapter extends ArrayAdapter<Problem> {
    Context mContext;
    int mLayoutResourceId;

    public ProblemsChoiceAdapter(Context context, int resource, List<Problem> objects) {
        super(context, resource, objects);
        this.mContext=context;
        this.mLayoutResourceId=resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProblemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new ProblemHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (ProblemHolder)row.getTag();
        }
        holder.txtTitle.setText(getItem(position).getName());

        return row;
    }
    static class ProblemHolder
    {
        TextView txtTitle;
    }
}
