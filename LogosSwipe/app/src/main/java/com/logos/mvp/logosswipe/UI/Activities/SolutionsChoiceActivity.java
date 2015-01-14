package com.logos.mvp.logosswipe.UI.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.fragments.SolutionsChoiceFragment;

public class SolutionsChoiceActivity extends ActionBarActivity implements SolutionsChoiceFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solutions_choice);
        if (savedInstanceState == null) {
            ;
            getFragmentManager().beginTransaction()
                    //.add(R.id.container, new PlaceholderFragment())
                    .add(R.id.solutions_choice_container, SolutionsChoiceFragment.newInstance("", ""))
                    .commit();
        }
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
