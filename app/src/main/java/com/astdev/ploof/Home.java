package com.astdev.ploof;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private CardView cvConso, cvFuite, cvPlombier, cvAlerte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        this.cvConso = findViewById(R.id.cardViewConso);
        this.cvFuite = findViewById(R.id.cardViewFuite);
        this.cvPlombier = findViewById(R.id.cardViewPlombier);
        this.cvAlerte = findViewById(R.id.cardViewAlerte);

        cvConso.setOnClickListener(view -> Toast.makeText(this, "Ma conso", Toast.LENGTH_SHORT).show());

        cvFuite.setOnClickListener(view -> Toast.makeText(this, "Signaler une fuite d'eau", Toast.LENGTH_SHORT).show());

        cvPlombier.setOnClickListener(view -> Toast.makeText(this, "Contacter un plombier", Toast.LENGTH_SHORT).show());

        cvAlerte.setOnClickListener(view -> Toast.makeText(this, "Mes alertes", Toast.LENGTH_SHORT).show());
       /* ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();*/
    }
}