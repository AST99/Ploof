package com.astdev.ploof;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private CardView cvConso, cvFuite, cvPlombier, cvAlerte;
    private ImageView imgProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.cvConso = findViewById(R.id.cardViewConso);
        this.cvFuite = findViewById(R.id.cardViewFuite);
        this.cvPlombier = findViewById(R.id.cardViewPlombier);
        this.cvAlerte = findViewById(R.id.cardViewAlerte);
        this.imgProfil = findViewById(R.id.imgViewProfil);

        cvConso.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),SuiviConso.class)));

        cvFuite.setOnClickListener(view -> Toast.makeText(this, "Signaler une fuite d'eau", Toast.LENGTH_SHORT).show());

        cvPlombier.setOnClickListener(view -> Toast.makeText(this, "Contacter un plombier", Toast.LENGTH_SHORT).show());

        cvAlerte.setOnClickListener(view -> Toast.makeText(this, "Mes alertes", Toast.LENGTH_SHORT).show());

        imgProfil.setOnClickListener(view -> Toast.makeText(this, "User Profil", Toast.LENGTH_SHORT).show());

       ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

}