package com.astdev.ploof;


import android.accounts.Account;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
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

public class Bar extends AppCompatActivity implements IAxisValueFormatter {

    final List<String> xAxisLabel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        BarChart barChart = findViewById(R.id.barChart);
        ArrayList<BarEntry> visitor = new ArrayList<>();
        ArrayList<String> XAxisLabels=new ArrayList <> ();


        visitor.add(new BarEntry(2010,10));
        visitor.add(new BarEntry(2011,35));
        visitor.add(new BarEntry(2012,80));
        visitor.add(new BarEntry(2013,70));
        visitor.add(new BarEntry(2014,20));
        visitor.add(new BarEntry(2015,30));
        visitor.add(new BarEntry(2016,50));


        xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");
        xAxisLabel.add("Sat");
        xAxisLabel.add("Sun");



        BarDataSet barDataSet = new BarDataSet(visitor,"Visitor");
        //barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData  = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);

        //barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxis = barChart.getXAxis();

       /* xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "YOUR_TEXT"; // here you can map your values or pass it as empty string
            }

            @Override
            public int getDecimalDigits() {
                return 0; //show only integer
            }
        });*/







        barChart.getDescription().setText("Bar chart Exemple");
        barChart.animateY(1000);


        /*GraphView graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);*/

    }



    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value >= 0) {
            if (value <= xAxisLabel.size() - 1) {
                return xAxisLabel.get((int) value);
            }return "";
        }return "";
    }
}