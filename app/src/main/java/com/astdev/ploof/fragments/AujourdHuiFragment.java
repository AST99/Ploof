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

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class AujourdHuiFragment extends Fragment {

    // Create the object of TextView and PieChart class
    TextView tvR, tvPython;
    View legendColor1, legendColor2;
    PieChart pieChart;


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

        tvR = view.findViewById(R.id.tvR);
        tvPython = view.findViewById(R.id.tvPython);
        pieChart = view.findViewById(R.id.piechart);
        legendColor1 = view.findViewById(R.id.view1);
        legendColor2 = view.findViewById(R.id.view2);

        setData();

    }



    /**************************************Affiche les données du pie chart*****************************/
    @SuppressLint("SetTextI18n")
    private void setData()
    {
        // Set the percentage of language used
        tvR.setText(Integer.toString(0));
        tvPython.setText(Integer.toString(0));

        // Set the data and color to the pie chart
        pieChart.addPieSlice(new PieModel("R", Integer.parseInt(tvR.getText().toString()),
                Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Python", Integer.parseInt(tvPython.getText().toString()),
                Color.parseColor("#66BB6A")));

        // To animate the pie chart
        pieChart.startAnimation();
    }

}