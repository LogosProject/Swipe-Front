package com.logos.mvp.logosswipe.UI.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.logos.mvp.logosswipe.App;
import com.logos.mvp.logosswipe.R;
import com.logos.mvp.logosswipe.UI.fragments.SolutionsChoiceFragment;
import com.logos.mvp.logosswipe.UI.fragments.ValuesChoiceFragment;
import com.logos.mvp.logosswipe.UI.views.MyMarkerView;
import com.logos.mvp.logosswipe.network.RequestQueueSingleton;
import com.logos.mvp.logosswipe.utils.JSONConverter;
import com.logos.mvp.logosswipe.utils.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.dao.query.QueryBuilder;
import greendao.Problem;
import greendao.ProblemDao;
import greendao.Solution;
import greendao.SolutionDao;
import greendao.SolutionScore;
import greendao.SolutionScoreDao;
import greendao.Value;
import greendao.ValueDao;
import greendao.ValueScore;
import greendao.ValueScoreDao;
import greendao.ValueSolutionScore;
import greendao.ValueSolutionScoreDao;

public class SolutionsPresentationActivity extends ActionBarActivity {

    private static final String TAG = "SolPresentationActivity";
    private RadarChart mChart;
    private long mProblemId;
    private Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solutions_presentation);
        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionbar);
        if (null != actionbar) {
            actionbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //NavUtils.navigateUpFromSameTask(SolutionsChoiceActivity.this);
                }
            });

            // Inflate a menu to be displayed in the toolbar
            //     actionbar.inflateMenu(R.menu.settings);
        }
        mProblemId = -1L;
        if (getIntent().getExtras() != null) {
            mProblemId = getIntent().getExtras().getLong(ValuesChoiceFragment.ARG_PROBLEM_ID);
        }
        mChart = (RadarChart) findViewById(R.id.chart1);

        tf = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");

        mChart.setValueTypeface(tf);

        mChart.setDescription("");

        mChart.setDrawUnitsInChart(true);

        mChart.setWebLineWidth(2f);
        mChart.setWebLineWidthInner(2f);
        mChart.setWebAlpha(100);

        mChart.setDrawYValues(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarkerView(mv);


        int count = setData();
        XLabels xl = mChart.getXLabels();
        xl.setTypeface(tf);
        xl.setTextSize(9f);

        YLabels yl = mChart.getYLabels();
        yl.setTypeface(tf);
        yl.setLabelCount(2);
        yl.setTextSize(9f);
        yl.setDrawUnitsInYLabel(false);

        // mChart.animateXY(1500, 1500);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setTypeface(tf);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solutions_presentation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                if (mChart.isDrawYValuesEnabled())
                    mChart.setDrawYValues(false);
                else
                    mChart.setDrawYValues(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChart.isHighlightEnabled())
                    mChart.setHighlightEnabled(false);
                else
                    mChart.setHighlightEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleRotate: {
                if (mChart.isRotationEnabled())
                    mChart.setRotationEnabled(false);
                else
                    mChart.setRotationEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleFilled: {

                ArrayList<RadarDataSet> sets = (ArrayList<RadarDataSet>) mChart.getData()
                        .getDataSets();

                for (RadarDataSet set : sets) {
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionSave: {
                if (mChart.saveToPath("title" + System.currentTimeMillis(), "")) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
                break;
            }
            case R.id.actionToggleXLabels: {
                if (mChart.isDrawXLabelsEnabled())
                    mChart.setDrawXLabels(false);
                else
                    mChart.setDrawXLabels(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleYLabels: {
                if (mChart.isDrawYLabelsEnabled())
                    mChart.setDrawYLabels(false);
                else
                    mChart.setDrawYLabels(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleSpin: {
                mChart.spin(2000, mChart.getRotationAngle(), mChart.getRotationAngle() + 360);
                break;
            }
        }
        return true;
    }



    public int setData() {
        //insertMockData();

        ValueSolutionScoreDao valueSolutionScoreDao = App.getSession().getValueSolutionScoreDao();
        ValueDao valueDao = App.getSession().getValueDao();
        SolutionScoreDao solutionScoreDao = App.getSession().getSolutionScoreDao();
        SolutionDao solutionDao = App.getSession().getSolutionDao();
        ProblemDao problemDao = App.getSession().getProblemDao();

        // ** Get a problem **//
        Problem problem = problemDao.load(mProblemId);

        // ** Get its solutions ** //
        QueryBuilder<Solution> queryBuilder1 = solutionDao.queryBuilder().where(SolutionDao.Properties.ProblemId.eq(mProblemId));
        List<Solution> solutions=queryBuilder1.list();

        // ** Get its values **//
        QueryBuilder<Value> queryBuilder2 = valueDao.queryBuilder().where(ValueDao.Properties.ProblemId.eq(mProblemId));
        List<Value> values = queryBuilder2.list();

        // *Get ValueSolutionsScores **//
        QueryBuilder<ValueSolutionScore> queryBuilder3 = valueSolutionScoreDao.queryBuilder().where(ValueSolutionScoreDao.Properties.UserId.eq(Requests.USER_ID));
        List<ValueSolutionScore> valueSolutionScore = queryBuilder3.list();

        //** Get SolutionsScore **//
        QueryBuilder<SolutionScore> queryBuilder4 = solutionScoreDao.queryBuilder().where(SolutionScoreDao.Properties.UserId.eq(Requests.USER_ID));
        List<SolutionScore> solutionScores = queryBuilder4.list();

        // Finding XVals
        ArrayList<String> xVals = new ArrayList<String>();
        //HashMap<String,Long> map = new HashMap<String, Long>();

        for(int i = 0; i< valueSolutionScore.size(); i++){
            String name = valueDao.load(valueSolutionScore.get(i).getValueId()).getName();
            if(!xVals.contains(name)){
                xVals.add(name);
                //map.put(name,valueSolutionScore.get(i).getValueId());
            }
        }



        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        for(int i = 0 ; i<solutionScores.size();i++){
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for(int j= 0; j<valueSolutionScore.size();j++){
                if(solutionScores.get(i).getSolutionId()==valueSolutionScore.get(j).getSolutionId()) {
                    String name = valueDao.load(valueSolutionScore.get(j).getValueId()).getName();
                    yVals.add(new Entry((float) (double) valueSolutionScore.get(j).getScore(), xVals.indexOf(name)));
                }
            }
            String solutionName = App.getSession().getSolutionDao().load(solutionScores.get(i).getSolutionId()).getName();
            RadarDataSet set1 = new RadarDataSet(yVals, solutionName);
            set1.setColor(ColorTemplate.VORDIPLOM_COLORS[i % ColorTemplate.VORDIPLOM_COLORS.length]);
            set1.setDrawFilled(true);
            set1.setLineWidth(3f);
            sets.add(set1);
        }



       /* ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }

        for (int i = 0; i < cnt; i++) {
            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }


        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set1.setDrawFilled(true);
        set1.setLineWidth(3f);

        RadarDataSet set2 = new RadarDataSet(yVals2, "Set 2");
        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        set2.setDrawFilled(true);
        set2.setLineWidth(3f);

        sets.add(set1);
        sets.add(set2);*/

        RadarData data = new RadarData(xVals, sets);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
        return sets.size();

    }
}