package com.astdev.ploof;


import android.accounts.Account;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.common.internal.IAccountAccessor;


import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;
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

        graph();
    }



    public void graph(){
        float[] yDataL = {10, 60, 70, 80};
        String[] xDataL = {"Week 1", "Week 2" , "Week 3" , "Week 4"};

        ArrayList<Entry> yEntrys = new ArrayList<>();
        final ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yDataL.length; i++){
            yEntrys.add(new Entry(i, yDataL[i]));
        }

        for(int i = 1; i < xDataL.length; i++){
            xEntrys.add(xDataL[i]);
        }


        Log.d("asa", "data Y 1" + yEntrys);
        Log.d("asa", "data X 1" + xEntrys);

        //create the data set
        LineDataSet pieDataSet = new LineDataSet(yEntrys, "assa");
        LineChart lineChart= findViewById(R.id.barChart);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                Log.d("asa", "data X" + xEntrys);
                return "Day"+xDataL[(int) value-1];
                //return xEntrys.get((int) value % xEntrys.size());
            }
        });

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);


        lineChart.getAxisLeft().setAxisMaxValue(100f);
        lineChart.getAxisLeft().setAxisMinValue(10f);

        LineData pieData = new LineData(pieDataSet);
        lineChart.setData(pieData);
        lineChart.invalidate();
    }
}