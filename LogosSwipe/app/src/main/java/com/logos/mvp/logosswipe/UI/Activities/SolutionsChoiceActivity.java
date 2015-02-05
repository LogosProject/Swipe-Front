package com.logos.mvp.logosswipe.UI.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.fragments.SolutionsChoiceFragment;
import com.logos.mvp.logosswipe.UI.fragments.ValuesChoiceFragment;

public class SolutionsChoiceActivity extends ActionBarActivity implements SolutionsChoiceFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solutions_choice);
        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionbar);
        if (null != actionbar) {
            actionbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(SolutionsChoiceActivity.this);
                }
            });

            // Inflate a menu to be displayed in the toolbar
            //     actionbar.inflateMenu(R.menu.settings);
        }
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Long problemId = -1L;
            long[] array = new long[0];
            if (getIntent().getExtras() != null) {
                problemId = getIntent().getExtras().getLong(ValuesChoiceFragment.ARG_PROBLEM_ID);
                array = getIntent().getExtras().getLongArray(SolutionsChoiceFragment.ARG_VALUE_IDS);
            }
            SolutionsChoiceFragment fragment = SolutionsChoiceFragment.newInstance(problemId,array);
            fragmentTransaction.add(R.id.solutions_choice_container, fragment, SolutionsChoiceFragment.TAG);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        SolutionsChoiceFragment fragment =(SolutionsChoiceFragment) getSupportFragmentManager().findFragmentByTag(SolutionsChoiceFragment.TAG);
        if(fragment != null && fragment.isItemSelected()){
            fragment.resetSelection();
            return;
        }
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solutions_choice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {
    }
}
