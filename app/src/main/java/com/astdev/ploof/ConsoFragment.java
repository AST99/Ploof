package com.astdev.ploof;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ConsoFragment extends Fragment{

    BarChart barChart;

    private CardView signaleFuite, fuiteDetected;
    private TextView txtView, txtViewClose, txtViewClose2;
    private Button btnDomicile, btnRue, btnPrendrePhoto;
    public static boolean atHome, outsideHome;
    private Dialog d_lieuFuite; //l'utilisateur choisie le lieux de la fuite (à domicile ou  dans la rue)
    private Dialog sFuite;
    private Button exFab;


    ImageView photoPrise;
    Uri afficheImage;

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
        this.btnPrendrePhoto = sFuite.findViewById(R.id.btnPrendrePhoto);
        this.photoPrise = sFuite.findViewById(R.id.addPhotoFuite);

        btnPrendrePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED ||
                            getContext().checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
                        String[]tabPermission={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                        requestPermissions(tabPermission,100);
                    }
                    else openCam();
                }
                else openCam();
            }
        });

        this.fuiteDetected = view.findViewById(R.id.cvFuiteDetected);
        fuiteDetected.setOnClickListener(view1 -> startActivity(new Intent(getContext(),Bar.class)));

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

        BarDataSet barDataSet = new BarDataSet(visitor,"");
        //barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);
        BarData barData  = new BarData(barDataSet);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.setFitBars(true);
        barChart.setData(barData);

        barChart.animateY(900);
    }


    /*************************************Prendre une photo****************************************/
    private void openCam() {
        ContentValues cv=new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Titre Image");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Description Image");
        afficheImage= requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,afficheImage);
        startActivityForResult(cameraIntent,101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                openCam();
            else
                Toast.makeText(getActivity(), "Permission manquante", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==101)
            photoPrise.setImageURI(afficheImage);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // UploadImage method
    /*private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(PrendrePhoto.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Enregistrement de la photo...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(photo != null) {
            StorageReference childRef = storageRef.child("Images/"+ System.currentTimeMillis()+".jpeg");
            //uploading the image
            UploadTask uploadTask = childRef.putBytes(imgCompressed);
            uploadTask.addOnSuccessListener(taskSnapshot ->{
                Toast.makeText(PrendrePhoto.this, "La photo a bien été enregistrée!", Toast.LENGTH_SHORT).show();
                //Obtenir l'url de l'image
                childRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    urlImage = uri.toString();
                    url.setImage(urlImage);
                });
                progressDialog.dismiss();
            });
            uploadTask.addOnFailureListener(e ->
                    Toast.makeText(PrendrePhoto.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show());
        }
        else
            Toast.makeText(PrendrePhoto.this, "Select an image", Toast.LENGTH_SHORT).show();
    }*/

}