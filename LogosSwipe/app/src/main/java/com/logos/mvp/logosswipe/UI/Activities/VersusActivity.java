package com.logos.mvp.logosswipe.UI.activities;

import java.util.Locale;

import android.app.ActionBar;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.fragments.DebateFragment;
import com.logos.mvp.logosswipe.UI.fragments.CompareSolutionFragment;
import com.logos.mvp.logosswipe.UI.fragments.SolutionsChoiceFragment;
import com.logos.mvp.logosswipe.UI.fragments.ValuesChoiceFragment;
import com.logos.mvp.logosswipe.UI.fragments.ValuesRankFragment;

import greendao.Versus;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class VersusActivity extends ActionBarActivity implements DebateFragment.OnFragmentInteractionListener,CompareSolutionFragment.OnFragmentInteractionListener, MaterialTabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    MaterialTabHost tabHost;

    long mCurrentVersusId=-1;

    public Versus getmCurrentVersus() {
        return mCurrentVersus;
    }

    Versus mCurrentVersus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versus);
        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionbar);

        if (null != actionbar) {

            actionbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(VersusActivity.this);
                }
            });

            // Inflate a menu to be displayed in the toolbar
            //     actionbar.inflateMenu(R.menu.settings);
        }
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);

        Long problemId = -1L;
        long[] valueIds = new long[0];
        long[] solutionsIds = new long[0];
        if (getIntent().getExtras() != null) {
            problemId = getIntent().getExtras().getLong(ValuesChoiceFragment.ARG_PROBLEM_ID);
            valueIds = getIntent().getExtras().getLongArray(SolutionsChoiceFragment.ARG_VALUE_IDS);
            solutionsIds = getIntent().getExtras().getLongArray(ValuesRankFragment.ARG_SOLUTION_IDS);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),problemId,valueIds,solutionsIds);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
                if(position==1 ){
                    Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
                    // based on the current position you can then cast the page to the correct
                    // class and call the method:
                    if ( page != null) {
                        ((DebateFragment)page).requestComments();
                    }
                }
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_versus, menu);
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mViewPager.setCurrentItem(materialTab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onNextVersusReceived(Versus versus) {
        mCurrentVersus=versus;

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Long problemId = -1L;
        long[] valueIds = new long[0];
        long[] solutionsIds = new long[0];

        public SectionsPagerAdapter(FragmentManager fm, long problemId, long[] valueIds, long[] solutionIds) {
            super(fm);
            this.problemId=problemId;
            this.valueIds=valueIds;
            this.solutionsIds=solutionIds;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position){
                case 0:
                    return CompareSolutionFragment.newInstance(problemId,valueIds,solutionsIds);
                case 1:
                    return  DebateFragment.newInstance("", "");

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }

    public long getmCurrentVersusId() {
        return mCurrentVersusId;
    }

    public void setmCurrentVersusId(long mCurrentVersusId) {
        this.mCurrentVersusId = mCurrentVersusId;
    }



}
