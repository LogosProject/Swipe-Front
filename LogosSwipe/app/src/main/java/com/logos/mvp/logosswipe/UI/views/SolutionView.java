package com.logos.mvp.logosswipe.UI.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logos.mvp.logosswipe.R;

/**
 * Created by Sylvain on 05/02/15.
 */
public class SolutionView extends LinearLayout {
    TextView mTitle;
    TextView mContent;

    public SolutionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_solution, this, true);
        mTitle = (TextView) getChildAt(0);
        mContent = (TextView) getChildAt(1);
    }


    public void setContent(String content) {
        this.mContent.setText(content);
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }
}
