package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Services extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        //Gestion des évènements du menu de navigation du bas de page
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_services);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_accueil){
                startActivity(new Intent(getApplicationContext(),Home.class));
                overridePendingTransition(0,0);
                return true;
            }
            if (item.getItemId() == R.id.action_conso){
                startActivity(new Intent(getApplicationContext(), SuiviConso.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });
    }
}