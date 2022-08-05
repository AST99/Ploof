package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SuiviConso extends AppCompatActivity {

    private TabLayout tabLayoutConso;

    String dateTime24h, dateTime12h;
    Calendar calendar;
    SimpleDateFormat heureFormat24h, heureFormat12h;

    // Create the object of TextView and PieChart class
    TextView tvR, tvPython;
    View legendColor1, legendColor2;
    PieChart pieChart;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suivi_conso);

        this.tabLayoutConso = findViewById(R.id.tabLayoutConso);
        selectedConsoTab();

        calendar = Calendar.getInstance();
        heureFormat24h = new SimpleDateFormat("HH:mm");
        heureFormat12h = new SimpleDateFormat("KK:mm");
        dateTime24h = heureFormat24h.format(calendar.getTime());
        dateTime12h = heureFormat12h.format(calendar.getTime());

        tvR = findViewById(R.id.tvR);
        tvPython = findViewById(R.id.tvPython);
        pieChart = findViewById(R.id.piechart);
        legendColor1 = findViewById(R.id.view1);
        legendColor2 = findViewById(R.id.view2);


        setData();

        Toast.makeText(getApplicationContext(), String.valueOf(dateTime24h), Toast.LENGTH_SHORT).show();

    }


    @SuppressLint("SetTextI18n")
    private void setData()
    {

        // Set the percentage of language used
        tvR.setText(Integer.toString(80));
        tvPython.setText(Integer.toString(10));


        // Set the data and color to the pie chart
        pieChart.addPieSlice(new PieModel("R", Integer.parseInt(tvR.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Python", Integer.parseInt(tvPython.getText().toString()),
                        Color.parseColor("#66BB6A")));


        // To animate the pie chart
        pieChart.startAnimation();
    }

    private void selectedConsoTab(){

        tabLayoutConso.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Toast.makeText(getApplicationContext(), "Conso d'aujourd'hui", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Conso d'hier", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(SuiviConso.this, "Conso de cette semaine", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}