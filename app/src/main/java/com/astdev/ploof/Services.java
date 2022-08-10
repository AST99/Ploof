package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de toolbar_menu à l'ActionBar
        getMenuInflater().inflate(R.menu.home_toolbar, menu);

        return true;
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_Compte) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.action_deconnexion) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), ConnexionPage.class));
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}