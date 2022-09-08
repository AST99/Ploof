package com.astdev.ploof;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConsoFragment extends Fragment{

    BarChart barChart;

    private TextView txtView;
    private Button btnPrendrePhoto;
    public static boolean atHome, outsideHome;
    private Dialog choixLieuFuite; //l'utilisateur choisie le lieux de la fuite (à domicile ou  dans la rue)
    private Dialog sFuite;
    private Button exFab;

    private ImageView photoPrise;
    private Uri afficheImage;
    private byte[] imgCompressed;  //L'image après l'avoir compresser

    //Pour la localisation
    FusedLocationProviderClient client;
    public List<UsersModel> usersModelList;
    private UsersModel user, lieu;
    private FirebaseDatabase database;
    private DatabaseReference myRefPosition;
    private String latitude, longitude;


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

        lieu = new UsersModel();

        choixLieuFuite = new Dialog(getActivity());
        choixLieuFuite.setContentView(R.layout.fuitepopup);
        choixLieuFuite.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        choixLieuFuite.getWindow().setGravity(Gravity.CENTER);
        choixLieuFuite.setCanceledOnTouchOutside(false);

        sFuite = new Dialog(getActivity());
        sFuite.setContentView(R.layout.signaler_fuite);
        sFuite.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sFuite.getWindow().setGravity(Gravity.CENTER);
        sFuite.setCanceledOnTouchOutside(false);

        this.barChart = view.findViewById(R.id.barChart);
        this.exFab = sFuite.findViewById(R.id.exFab);
        this.txtView = sFuite.findViewById(R.id.txtViewTitle);
        this.btnPrendrePhoto = sFuite.findViewById(R.id.btnPrendrePhoto);
        this.photoPrise = sFuite.findViewById(R.id.addPhotoFuite);


        //Pour la localisation
        client = LocationServices.getFusedLocationProviderClient(requireActivity());
        usersModelList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRefPosition = database.getReference("Users");
        client = LocationServices.getFusedLocationProviderClient(requireActivity());
        // check condition
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            getCurrentLocation();
        }
        else {
            // When permission is not granted
            // Call method
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
        }

        TextInputEditText desc = sFuite.findViewById(R.id.descFuite);


        exFab.setOnClickListener(view12 ->{
            String strDesc = Objects.requireNonNull(desc.getText()).toString().trim();
            lieu.setDescription(strDesc);
            sendOnDatabase();

            sFuite.dismiss();
            choixLieuFuite.dismiss();
            if (atHome){
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new PlumberListFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,
                        myFragment).addToBackStack(null).commit();
            }
        });

        btnPrendrePhoto.setOnClickListener(view1 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                        requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    String[] tabPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    requestPermissions(tabPermission, 100);
                } else openCam();
            } else openCam();
        });

        CardView fuiteDetected = view.findViewById(R.id.cvFuiteDetected);
        fuiteDetected.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), Bar.class));
            Toast.makeText(getActivity(), "Affiche les fuites détectées", Toast.LENGTH_SHORT).show();
        });
        CardView signaleFuite = view.findViewById(R.id.cvFuite);
        signaleFuite.setOnClickListener(view1 -> {
            choixLieuFuite.show();
            signaleFuite();
        });
        graph();
    }

    @SuppressLint("SetTextI18n")
    public void signaleFuite(){
        TextView txtViewClose = choixLieuFuite.findViewById(R.id.close);
        TextView txtViewClose2 = sFuite.findViewById(R.id.close2);
        txtViewClose.setOnClickListener(view -> choixLieuFuite.dismiss());
        txtViewClose2.setOnClickListener(view -> {
            sFuite.dismiss();
            choixLieuFuite.show();
        });

        Button btnDomicile = choixLieuFuite.findViewById(R.id.btnDomicile);
        btnDomicile.setOnClickListener(view -> {
            atHome = true;
            outsideHome = false;
            exFab.setText("Contacter un plombier");
            txtView.setText("Signaler une fuite d'eau à domicile");
            lieu.setAtHome("yes");
            sFuite.show();
            choixLieuFuite.dismiss();
        });

        Button btnRue = choixLieuFuite.findViewById(R.id.btnRue);
        btnRue.setOnClickListener(view -> {
            outsideHome = true;
            atHome = false;
            lieu.setAtHome("no");
            exFab.setText("Signaler à l'ONEA");
            txtView.setText("Signaler une fuite d'eau dans la rue");
            sFuite.show();
            choixLieuFuite.dismiss();
        });
    }

    /***************************************Localisation********************************************/
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        try {
            // Initialize Location manager
            LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            // Check condition
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                // When location service is enabled
                // Get last location
                client.getLastLocation().addOnCompleteListener(task -> {
                    // Initialize location
                    Location location = task.getResult();
                    // Check condition
                    if (location != null) {
                        // When location result is not
                        // null set latitude
                        latitude= String.valueOf(location.getLatitude());
                        // set longitude
                        longitude= String.valueOf(location.getLongitude());
                    } else {
                        // When location result is null
                        // initialize location request
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                                // Initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // Initialize
                                // location
                                Location location1 = locationResult.getLastLocation();
                                // Set latitude
                                latitude = String.valueOf(Objects.requireNonNull(location1).getLatitude());
                                longitude=String.valueOf(location1.getLongitude());
                            }
                        };
                        // Request location updates
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                });
            } else {
                // When location service is not enabled
                // open location setting
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void graph(){

        float[] yData = {10,35,80,70,20,30,50};
        final String[] jours  = {"Lun","Mar","Mer","Jeu","Ven","Sam","Dim"};
        ArrayList<String> xEntry=new ArrayList <> ();
        ArrayList<BarEntry> yEntry=new ArrayList <> ();

        for (int i=0;i<yData.length;i++){
            yEntry.add(new BarEntry(i, yData[i]));
        }

        Collections.addAll(xEntry, jours);

        BarDataSet set1 = new BarDataSet(yEntry, "");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.setFitBars(true);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return jours[(int) value];
            }
        });
        barChart.setData(data);
        barChart.animateY(900);
    }

    /***************************Envoie les info sur la base de données*****************************/
    public void sendOnDatabase(){
        try {
            try {
                user = new UsersModel(latitude, longitude, lieu.getAtHome(), lieu.getDescription());
                usersModelList.add(user);

                myRefPosition = database.getReference("Users");
                myRefPosition.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).
                        getUid()).setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sFuite.dismiss();
                        choixLieuFuite.cancel();
                        Toast.makeText(requireActivity(), "La fuite a bien été signalée!" +
                                "\nUn technicien sera envoyé sur place!", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(requireActivity(), "Vérifiez votre connexion internet !",
                                Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if ((requestCode==100) && (grantResults.length>0 && grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED)){
           openCam();
           getCurrentLocation();
        }else
            Toast.makeText(getActivity(), "Permission manquante", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==101){
            photoPrise.setImageURI(afficheImage);
            btnPrendrePhoto.setVisibility(View.GONE);
        }
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