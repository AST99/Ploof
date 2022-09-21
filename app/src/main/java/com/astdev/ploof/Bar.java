package com.astdev.ploof;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


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