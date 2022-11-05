package com.astdev.ploof;

import static android.app.Activity.RESULT_OK;
import static java.lang.Float.parseFloat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.astdev.ploof.models.UsersModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import me.tankery.lib.circularseekbar.CircularSeekBar;

public class ConsoFragment extends Fragment{

    BarChart barChart;

    String userName="";
    float valueConso;
    final DecimalFormat df=new DecimalFormat();

    private TextView txtView;
    private Button btnPrendrePhoto;
    public static boolean atHome, outsideHome;
    private Dialog choixLieuFuite; //l'utilisateur choisie le lieux de la fuite (à domicile ou  dans la rue)
    private Dialog sFuite, moreUsersInfo, confimeFuiteSignaler;
    private Button exFab;

    private String strAdress, strNbrePersonne, strConsoMoyenne;

    private ImageView photoPrise;

    //Pour la localisation
    FusedLocationProviderClient client;
    String consoValue;
    public List<UsersModel> usersModelList;
    private UsersModel lieu;
    private FirebaseDatabase database;
    private DatabaseReference myRefPosition;

    private UsersModel position;

    public ConsoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conso, container, false);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainFragment) requireActivity()).setActionBarTitle("Accueil");
        position = new UsersModel();

        lieu = new UsersModel();
        List<String> spinnerItem = new ArrayList<>();
        spinnerItem.add("Résumé herbdomadaire");
        spinnerItem.add("Résumé mensuel");
        spinnerItem.add("Résumé annuel");

        choixLieuFuite = new Dialog(getActivity());
        choixLieuFuite.setContentView(R.layout.fuitepopup);
        choixLieuFuite.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
                .LayoutParams.WRAP_CONTENT);
        choixLieuFuite.getWindow().setGravity(Gravity.CENTER);
        choixLieuFuite.setCanceledOnTouchOutside(false);

        sFuite = new Dialog(getActivity());
        sFuite.setContentView(R.layout.signaler_fuite);
        sFuite.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sFuite.getWindow().setGravity(Gravity.CENTER);
        sFuite.setCanceledOnTouchOutside(false);

        confimeFuiteSignaler = new Dialog(getActivity());
        confimeFuiteSignaler.setContentView(R.layout.confimer_fuite_signaler);
        confimeFuiteSignaler.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        confimeFuiteSignaler.getWindow().setGravity(Gravity.CENTER);
        confimeFuiteSignaler.setCanceledOnTouchOutside(false);

        /*Affiche un popup afin que l'utilisateur remplisse les informations supplémentaires
        *tels que son adresse et le nombre de personne vivant dans le menage*/
        moreUsersInfo = new Dialog(getActivity());
        moreUsersInfo.setContentView(R.layout.more_users_info_dialog);
        moreUsersInfo.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
                .LayoutParams.WRAP_CONTENT);
        moreUsersInfo.getWindow().setGravity(Gravity.CENTER);
        moreUsersInfo.setCanceledOnTouchOutside(false);

        TextInputEditText adress = moreUsersInfo.findViewById(R.id.adress);
        TextInputEditText nbrePersonne = moreUsersInfo.findViewById(R.id.nbrePersonne);
        TextInputEditText consoM1 = moreUsersInfo.findViewById(R.id.consoM1);
        TextInputEditText consoM2 = moreUsersInfo.findViewById(R.id.consoM2);
        TextInputEditText consoM3 = moreUsersInfo.findViewById(R.id.consoM3);
        Button btnSendMoreInfo = moreUsersInfo.findViewById(R.id.btnSendMoreInfo);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().
                getCurrentUser()).getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                Log.e("firebase", "Error getting data", task.getException());
            else {
                strAdress=String.valueOf(task.getResult().child("quartier").getValue(String.class));
                strNbrePersonne=String.valueOf(task.getResult().child("nbrePersonne").getValue(String.class));
                strConsoMoyenne=String.valueOf(task.getResult().child("consoMoyenne").getValue(String.class));
                userName=(String.valueOf(task.getResult().child("nomPrenom").getValue(String.class)));

                float cM = 0;//cM=consoMoyenne

                try {
                    cM = parseFloat(strConsoMoyenne);
                    graphHerbdo();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity(),""+cM,Toast.LENGTH_SHORT).show();

                /*Récupère la valeur de la consommation collecter par le débimètre puis stocker sur la
                *base de donnée afin de l'afficher dans le seekBar*/
                consoValue=String.valueOf(task.getResult().child("conso").getValue(String.class));
                TextView txtViewConsoValue=view.findViewById(R.id.txtViewConso);
                txtViewConsoValue.setText(consoValue+" L");
                CircularSeekBar circularSeekBar=view.findViewById(R.id.consoBar);
                circularSeekBar.setEnabled(false);
                try {
                    valueConso = parseFloat(consoValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

//Début Timer******************************************************/
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(0);
                CountDownTimer timer = new CountDownTimer(1000000, 1000) {
                    @Override
                    public void onTick(long l) {
                        valueConso += 0.1;
                        circularSeekBar.setProgress(valueConso);
                        txtViewConsoValue.setText(df.format(valueConso).concat("L"));
                    }
                    @Override
                    public void onFinish() {}
                };
                if (userName.equals("test"))
                    timer.start();
//Fin timer***************************************************************/


                if (valueConso>cM)
                    circularSeekBar.setCircleProgressColor(Color.parseColor("#f00000"));
                circularSeekBar.setProgress(valueConso);

                if (strAdress.equals("")||strNbrePersonne.equals(""))
                    moreUsersInfo.show();
            }
        });

        btnSendMoreInfo.setOnClickListener(view1 -> {
            try {

                //Calcul de la consommation moyenne d'eau par jour et par personne
                double consoM1value = Double.parseDouble(Objects.requireNonNull(consoM1.getText()).toString().trim())*1000;
                double consoM2value = Double.parseDouble(Objects.requireNonNull(consoM2.getText()).toString().trim())*1000;
                double consoM3value = Double.parseDouble(Objects.requireNonNull(consoM3.getText()).toString().trim())*1000;
                double nbrePersonneValue=Double.parseDouble(Objects.requireNonNull(nbrePersonne.getText()).toString().trim());
                double consoMoyenne = ((consoM1value+consoM2value+consoM3value)/30);
                consoMoyenne = consoMoyenne/nbrePersonneValue;
                //fin de calcule de la consommation d'eau moyenne
                String strConsoMoy = String.valueOf(consoMoyenne).trim();

                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("quartier", Objects.requireNonNull(adress.getText()).toString().trim());
                userMap.put("nbrePersonne",Objects.requireNonNull(nbrePersonne.getText()).toString().trim());
                userMap.put("consoMoyenne",Objects.requireNonNull(strConsoMoy));

                myRefPosition = database.getReference("Users");
                myRefPosition.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).
                        getUid()).updateChildren(userMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        moreUsersInfo.dismiss();
                    } else{
                        Toast.makeText(requireActivity(), "Une erreur est survenue! Veuillez réessayer",
                                Toast.LENGTH_SHORT).show();
                        moreUsersInfo.show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        /*Fin*/

        this.barChart = view.findViewById(R.id.barChart);
        this.exFab = sFuite.findViewById(R.id.exFab);
        this.txtView = sFuite.findViewById(R.id.txtViewTitle);
        this.btnPrendrePhoto = sFuite.findViewById(R.id.btnPrendrePhoto);
        this.photoPrise = sFuite.findViewById(R.id.addPhotoFuite);

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout
                .simple_spinner_dropdown_item, spinnerItem);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: graphHerbdo();break;
                    case 1: graphMensuel();break;
                    case 2: graphAnnuel();break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

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
            // When permission is granted Call method
            getCurrentLocation();
        }
        else {
            // When permission is not granted Call method
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
        }

        TextInputEditText desc = sFuite.findViewById(R.id.descFuite);

        exFab.setOnClickListener(view12 ->{
            String strDesc = Objects.requireNonNull(desc.getText()).toString().trim();
            TextView txtViewCFS = confimeFuiteSignaler.findViewById(R.id.txtViewCFS);
            lieu.setDescription(strDesc);
            sendOnDatabase();

            sFuite.dismiss();
            choixLieuFuite.dismiss();
            if (lieu.getAtHome().equals("yes"))
                txtViewCFS.setText("Votre fuite a bien été signaler ! Vous serez contacté par un plombier.");
            else
                txtViewCFS.setText("La fuite a bien été signalée. Un technicien sera envoyé sur place !");
        });

        btnPrendrePhoto.setOnClickListener(view1 -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                        requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    String[] tabPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    requestPermissions(tabPermission, 100);
                } else chooseImage();
            } else chooseImage();
        });

        CardView fuiteDetected = view.findViewById(R.id.cvFuiteDetected);
        fuiteDetected.setOnClickListener(view1 -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment myFragment = new FuiteDetectedFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,
                    myFragment).addToBackStack(null).commit();
        });
        CardView signaleFuite = view.findViewById(R.id.cvFuite);
        signaleFuite.setOnClickListener(view1 -> {
            choixLieuFuite.show();
            signaleFuite();
        });
    }

    @SuppressLint("SetTextI18n")
    public void signaleFuite(){
        TextView txtViewClose = choixLieuFuite.findViewById(R.id.close);
        txtViewClose.setOnClickListener(view -> choixLieuFuite.dismiss());
        TextView txtViewClose2 = sFuite.findViewById(R.id.close2);
        txtViewClose2.setOnClickListener(view -> {
            sFuite.dismiss();
            choixLieuFuite.show();
        });

        Button btnOk = confimeFuiteSignaler.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(view -> confimeFuiteSignaler.dismiss());

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
                // When location service is enabled Get last location
                client.getLastLocation().addOnCompleteListener(task -> {
                    // Initialize location
                    Location location = task.getResult();
                    // Check condition
                    if (location != null) {
                        // When location result is not null set latitude
                        String latitude = String.valueOf(location.getLatitude());
                        String longitude = String.valueOf(location.getLongitude());
                        position.setPosition(""+latitude+","+longitude);

                    } else {
                        // When location result is null initialize location request
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                                // Initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // Initialize location
                                Location location1 = locationResult.getLastLocation();

                                String latitude = String.valueOf(Objects.requireNonNull(location1).getLatitude());
                                String longitude = String.valueOf(location1.getLongitude());
                                position.setPosition(""+latitude+","+longitude);
                            }
                        };
                        // Request location updates
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                });
            } else {
                // When location service is not enabled open location setting
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************Les graphes hebdomadaire, mensuel, annuel******************************/
    public void graphHerbdo(){

        float[] yData=new float[]{};
        try {
            if (userName.equals("test")){
                yData= new float[]{28, 25, 30, 23, 31, 34, 0};
            }
            else {
                yData = new float[]{0, 0, 0, 0, 0, 0, 0};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String[] jours  = {"Lun","Mar","Mer","Jeu","Ven","Sam","Dim"};
        var xEntry= new AtomicReference<ArrayList<String>>(new ArrayList<>());
        ArrayList<BarEntry> yEntry=new ArrayList <> ();

        for (int i=0;i<yData.length;i++)
            yEntry.add(new BarEntry(i, yData[i]));

        Collections.addAll(xEntry.get(), jours);
        BarDataSet set1 = new BarDataSet(yEntry, "");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.getAxisLeft().setStartAtZero(true);
        barChart.getAxisRight().setStartAtZero(true);
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

    public void graphMensuel(){

        float[] yData=new float[]{};
        try {
            if (userName.equals("test")){
                yData = new float[]{0, 0, 0, 0, 0, 0, 60, 75, 80, 20, 0, 0};
            }
            else {
                yData = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String[] mois  = {"Janv","Févr", "Mars", "Avr", "Mai", "Juin", "Juil",
                "Août",	"Sept", "Oct", "Nov", "Déc"};
        var xEntry= new AtomicReference<ArrayList<String>>(new ArrayList<>());
        ArrayList<BarEntry> yEntry=new ArrayList <> ();

        for (int i=0;i<yData.length;i++)
            yEntry.add(new BarEntry(i, yData[i]));

        Collections.addAll(xEntry.get(), mois);
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
                return mois[(int) value];
            }
        });
        barChart.setData(data);
        barChart.animateY(900);
    }

    public void graphAnnuel(){

        float[] yData=new float[]{};
        try {
            if (userName.equals("test")){
                yData = new float[]{235};
            }
            else {
                yData = new float[]{0};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String[] an  = {"2022","","","","","",""};
        var xEntry= new AtomicReference<ArrayList<String>>(new ArrayList<>());
        ArrayList<BarEntry> yEntry=new ArrayList <> ();

        for (int i=0;i<yData.length;i++)
            yEntry.add(new BarEntry(i, yData[i]));

        Collections.addAll(xEntry.get(), an);
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
                return an[(int) value];
            }
        });
        barChart.setData(data);
        barChart.animateY(900);
    }

    /***************************Envoie les info sur la base de données*****************************/
    public void sendOnDatabase(){
        try {
            try {
                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("atHome",lieu.getAtHome());
                userMap.put("description",lieu.getDescription());
                userMap.put("position",position.getPosition());

                myRefPosition = database.getReference("Users");
                myRefPosition.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).
                        getUid()).updateChildren(userMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sFuite.dismiss();
                        choixLieuFuite.cancel();
                        confimeFuiteSignaler.show();
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
    private void chooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((requestCode==100) && (grantResults.length>0 && grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED)){
          //chooseImage();
          getCurrentLocation();
        }else
            Toast.makeText(getActivity(), "Permission manquante", Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 200) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    photoPrise.setImageURI(selectedImageUri);
                    btnPrendrePhoto.setVisibility(View.GONE);
                }
            }
        }
    }
}