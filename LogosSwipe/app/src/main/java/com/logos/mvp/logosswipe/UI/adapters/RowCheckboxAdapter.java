package com.logos.mvp.logosswipe.UI.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;

import java.util.List;

public class RowCheckboxAdapter extends ArrayAdapter<ClipData.Item> {

    private final List<ClipData.Item> list; // TODO replace with class from Model
    private final Activity context;

    public RowCheckboxAdapter(Activity context, List<ClipData.Item> list) {
        super(context, R.layout.listview_item_row_with_checkbox, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

}
