package com.astdev.ploof;


import android.accounts.Account;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.common.internal.IAccountAccessor;


import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Bar extends AppCompatActivity {

    final List<String> xAxisLabel = new ArrayList<>();
    BarChart barChart;
    ArrayList<String> xEntry=new ArrayList <> ();
    ArrayList<BarEntry> yEntry=new ArrayList <> ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        //this.barChart = findViewById(R.id.barChart);

        //graph();
    }



    public void graph(){

        String[] test = {"test1","test2"};

        BarChart barChart = (BarChart) findViewById(R.id.barChart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        XAxis xl = barChart.getXAxis();
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);
        xl.setValueFormatter(new IndexAxisValueFormatter(test));
        YAxis leftAxis = barChart.getAxisLeft();

        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); //this replaces setStartAtZero(true
        barChart.getAxisRight().setEnabled(false);
//data
        float groupSpace = 0.04f;
        float barSpace = 0.02f; //x2 dataset
        float barWidth = 0.46f; //x2 dataset
//(0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"
        int startYear = 1980;
        int endYear = 1985;
        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<BarEntry> yVals2 = new ArrayList<BarEntry>();
        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, 0.4f));
        }
        for (int i = startYear; i < endYear; i++) {
            yVals2.add(new BarEntry(i, 0.7f));
        }
        BarDataSet set1, set2;
        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet)barChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
//create 2 datasets with different types
            set1 = new BarDataSet(yVals1, "Company A");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "Company B");
            set2.setColor(Color.rgb(164, 228, 251));
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            BarData data = new BarData(dataSets);
            barChart.setData(data);
        }
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinValue(startYear);
        barChart.groupBars(startYear, groupSpace, barSpace);
        barChart.invalidate();

    }
}