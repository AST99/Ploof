package com.astdev.ploof;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class MapsFragment extends AppCompatActivity {

    static SupportMapFragment supportMapFragment;
    static FusedLocationProviderClient client;
    private static LatLng latLng;
    static String latitude;
    static String longitude;
    public List<UsersModel> usersModelList;
    UsersModel user;
    private ExtendedFloatingActionButton saveLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_popup);

        this.saveLocation = findViewById(R.id.extended_fab);
        saveLocation.setOnClickListener(view -> this.finish());

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.GoogleMap);
        client = LocationServices.getFusedLocationProviderClient(MapsFragment.this);

        //Vérifie si l'application a accès à la localistion du téléphone. Sinon demande l'accès
        if (ActivityCompat.checkSelfPermission(MapsFragment.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted, call methode
            getCurrentLocation();
        }else {
            //when permission denied
            //Request permission
            ActivityCompat.requestPermissions(MapsFragment.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }

    @SuppressLint("MissingPermission")
    public static void getCurrentLocation() {
        try {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location!=null){
                    supportMapFragment.getMapAsync(googleMap -> {
                        latLng=new LatLng(location.getLatitude(),location.getLongitude());
                        //option
                        MarkerOptions options=new MarkerOptions().position(latLng).title("C'est ici !");
                        //zoom
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                        //ajouter l'icon de localisation
                        googleMap.addMarker(options);
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}