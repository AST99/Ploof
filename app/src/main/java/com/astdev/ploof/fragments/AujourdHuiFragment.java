package com.astdev.ploof.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astdev.ploof.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;


public class AujourdHuiFragment extends Fragment {

    BarChart barChart;

    public AujourdHuiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aujourd_hui, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.barChart);
        setData();

    }



    /**************************************Affiche les données du pie chart*****************************/
    @SuppressLint("SetTextI18n")
    private void setData()
    {

        ArrayList<BarEntry> visitor = new ArrayList<>();
        visitor.add(new BarEntry(2010,10));
        visitor.add(new BarEntry(2011,35));
        visitor.add(new BarEntry(2012,80));
        visitor.add(new BarEntry(2013,70));
        visitor.add(new BarEntry(2014,20));
        visitor.add(new BarEntry(2015,30));
        visitor.add(new BarEntry(2016,50));

        BarDataSet barDataSet = new BarDataSet(visitor,"Visitor");
        //barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData  = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar chart Exemple");
        barChart.animateY(2000);
    }

}