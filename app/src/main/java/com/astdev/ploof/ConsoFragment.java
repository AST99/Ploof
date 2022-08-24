package com.astdev.ploof;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class ConsoFragment extends Fragment {

    public ConsoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //((MainFragment) requireActivity()).setActionBarTitle("Ma consommation");

        BarChart barChart = view.findViewById(R.id.barChart);
        ArrayList<BarEntry> visitor = new ArrayList<>();
        ArrayList<String> XAxisLabels=new ArrayList <> ();

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
        barDataSet.setValueTextSize(10f);
        BarData barData  = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);


        barChart.getDescription().setText("Bar chart Exemple");
        barChart.animateY(900);

    }
}