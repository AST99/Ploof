package com.astdev.ploof;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.opencsv.CSVWriter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bar extends AppCompatActivity {

    private static final int MAX_X_VALUE = 7;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "App Downloads";
    private static final String[] DAYS = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
    private BarChart barChart;
    private static Random sRandom = new Random();

    EditText folderName;
    Button createFolder;
    String FolderName;
    private  static final int PERMISSION_REQUEST_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        this.barChart = findViewById(R.id.barChart);

        graphMensuel();

        createCSV();
        createFolder = findViewById(R.id.button);
        /*createFolder.setOnClickListener(view -> {
            FolderName = "PloofData";
            if (ContextCompat.checkSelfPermission(Bar.this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                //createDirectory(FolderName);
                createCSV();
            }else {
                askPermission();
            }
        });*/
    }


    public void createCSV(){
        String csv = (Environment.getExternalStorageDirectory().getPath() + "/MyCsvFile.csv");
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"Country", "Capital"});
            data.add(new String[]{"Civ", "babi"});
            data.add(new String[]{"Burkina", "Ouga"});
            data.add(new String[]{"Germany", "Berlin"});

            writer.writeAll(data); // data is adding to csv

            writer.close();
            //callRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createDirectory(FolderName);
            }else {
                Toast.makeText(Bar.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createDirectory(String folderName) {
        File file = new File(Environment.getExternalStorageDirectory(),folderName);
        if (!file.exists()){
            file.mkdir();
            Toast.makeText(Bar.this,"Successful",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(Bar.this,"Folder Already Exists",Toast.LENGTH_SHORT).show();
        }
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