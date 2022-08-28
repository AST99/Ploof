package com.astdev.ploof;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class ConsoFragment extends Fragment {

    BarChart barChart;

    private CardView signaleFuite;
    private TextView txtView, txtViewClose, txtViewClose2;
    private Button btnDomicile, btnRue;
    public static boolean atHome, outsideHome;
    private Dialog d_lieuFuite; //l'utilisateur choisie le lieux de la fuite (à domicile ou  dans la rue)
    private Dialog sFuite;
    private ExtendedFloatingActionButton exFab;

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

        ((MainFragment) requireActivity()).setActionBarTitle("Ma consommation");

        d_lieuFuite = new Dialog(getActivity());
        d_lieuFuite.setContentView(R.layout.fuitepopup);
        d_lieuFuite.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        d_lieuFuite.getWindow().setGravity(Gravity.CENTER);
        d_lieuFuite.setCanceledOnTouchOutside(false);

        sFuite = new Dialog(getActivity());
        sFuite.setContentView(R.layout.signaler_fuite);
        sFuite.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        sFuite.getWindow().setGravity(Gravity.CENTER);
        sFuite.setCanceledOnTouchOutside(false);

        this.barChart = view.findViewById(R.id.barChart);
        this.exFab = sFuite.findViewById(R.id.exFab);
        this.txtView = sFuite.findViewById(R.id.txtViewTitle);

        this.signaleFuite = view.findViewById(R.id.cvFuite);
        signaleFuite.setOnClickListener(view1 -> {
            d_lieuFuite.show();
            signaleFuite();
        });

        graph();
    }

    public void signaleFuite(){
        this.txtViewClose = d_lieuFuite.findViewById(R.id.close);
        this.txtViewClose2 = sFuite.findViewById(R.id.close2);
        txtViewClose.setOnClickListener(view -> d_lieuFuite.dismiss());
        txtViewClose2.setOnClickListener(view -> {
            sFuite.dismiss();
            d_lieuFuite.show();
        });

        this.btnDomicile = d_lieuFuite.findViewById(R.id.btnDomicile);
        btnDomicile.setOnClickListener(view -> {
            atHome = true;
            outsideHome = false;
            exFab.setText("Contacter un plombier");
            txtView.setText("Signaler une fuite d'eau à domicile");
            //lieu.setAtHome("yes");
            sFuite.show();
            d_lieuFuite.dismiss();
        });

        this.btnRue = d_lieuFuite.findViewById(R.id.btnRue);
        btnRue.setOnClickListener(view -> {
            outsideHome = true;
            atHome = false;
            exFab.setText("Signaler à l'ONEA");
            txtView.setText("Signaler une fuite d'eau dans la rue");
            sFuite.show();
            d_lieuFuite.dismiss();
        });
    }

    public void graph(){
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