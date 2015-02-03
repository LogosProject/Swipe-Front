package com.logos.mvp.logosswipe.UI.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.fragments.SolutionsChoiceFragment;
import com.logos.mvp.logosswipe.UI.fragments.ValuesChoiceFragment;
import com.logos.mvp.logosswipe.UI.fragments.ValuesRankFragment;

public class ValuesRankActivity extends ActionBarActivity implements ValuesRankFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_values_rank);
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Long problemId = -1L;
            long[] valueIds = new long[0];
            long[] solutionsIds = new long[0];
            if (getIntent().getExtras() != null) {
                problemId = getIntent().getExtras().getLong(ValuesChoiceFragment.ARG_PROBLEM_ID);
                valueIds = getIntent().getExtras().getLongArray(SolutionsChoiceFragment.ARG_VALUE_IDS);
                solutionsIds = getIntent().getExtras().getLongArray(ValuesRankFragment.ARG_SOLUTION_IDS);
            }
            ValuesRankFragment fragment = ValuesRankFragment.newInstance(problemId,valueIds,solutionsIds);
            fragmentTransaction.add(R.id.values_rank_container, fragment,ValuesRankFragment.TAG);
            fragmentTransaction.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_values_rank, menu);
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
