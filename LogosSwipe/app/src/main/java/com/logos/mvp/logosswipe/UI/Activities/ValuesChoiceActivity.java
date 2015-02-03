package com.logos.mvp.logosswipe.UI.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.fragments.ValuesChoiceFragment;

public class ValuesChoiceActivity extends ActionBarActivity implements ValuesChoiceFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_values_choice);
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Long problemId = -1L;
            if (getIntent().getExtras() != null) {
                problemId = getIntent().getExtras().getLong(ValuesChoiceFragment.ARG_PROBLEM_ID);
            }
            ValuesChoiceFragment fragment = ValuesChoiceFragment.newInstance(problemId);
            fragmentTransaction.add(R.id.values_choice_container, fragment, ValuesChoiceFragment.TAG);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        ValuesChoiceFragment fragment =(ValuesChoiceFragment) getFragmentManager().findFragmentByTag(ValuesChoiceFragment.TAG);
        if(fragment != null && fragment.isItemSelected()){
            fragment.resetSelection();
            return;
        }
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_values_choice, menu);
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
