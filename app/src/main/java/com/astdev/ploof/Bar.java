package com.astdev.ploof;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.astdev.jpandas.src.main.java.com.c_bata.DataFrame;
import com.astdev.jpandas.src.main.java.com.c_bata.Series;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Bar extends AppCompatActivity {

    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "App Downloads";
    private static final String[] DAYS = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
    private BarChart barChart;
    private static Random sRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        this.barChart = findViewById(R.id.barChart);

        graphMensuel();

        // Checking jpandas
        Series<Double> series_a = new Series<>();
        series_a.add(3.0);
        series_a.add(5.0);
        Series<Double> series_b = new Series<>();
        series_b.add(9.0);
        series_b.add(20.0);

        DataFrame<String, Series> df = new DataFrame<>();
        df.put("feature_a", series_a);
        df.put("feature_b", series_b);

        // Log.d(df.mean());
        Log.d("JPANDAS", String.valueOf(df.mean().get("feature_a")));
        Log.d("JPANDAS", String.valueOf(df.mean().get("feature_b")));
        // Log.d(df.std());
        Log.d("JPANDAS", String.valueOf(df.std().get("feature_a")));
        Log.d("JPANDAS", String.valueOf(df.std().get("feature_b")));

    }

    public void graphMensuel(){

        float[] yData = {10,35,80,70,20,30,
                         50,64,94,70/*,8,20*/};
        final String[] mois  = {"Janv","Févr", "mars", "Avr", "Mai", "Juin",
                                "Juil", "Août","Sept", "Oct"/*, "Déc", "Déc"*/};
        ArrayList<String> xEntry=new ArrayList <>();
        ArrayList<BarEntry> yEntry=new ArrayList <>();

        for (int i=0;i<yData.length;i++)
            yEntry.add(new BarEntry(i, yData[i]));

        Collections.addAll(xEntry, mois);
        BarDataSet set1 = new BarDataSet(yEntry, "");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);

        barChart.getXAxis().setLabelRotationAngle(-90);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return mois[(int) value];
            }
        });
        barChart.setData(data);
        barChart.animateY(900);
    }
}