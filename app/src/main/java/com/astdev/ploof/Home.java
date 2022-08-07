package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Home extends AppCompatActivity {

    private CardView cvConso, cvFuite, cardViewServices, cvAlerte;
    private ImageView imgProfil;
    private TextView txtView;

    String dateTime24h, dateTime12h;
    Calendar calendar;
    SimpleDateFormat heureFormat24h, heureFormat12h;

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.txtView = findViewById(R.id.textView);
        this.cvConso = findViewById(R.id.cardViewConso);
        this.cvFuite = findViewById(R.id.cardViewFuite);
        this.cardViewServices = findViewById(R.id.cardViewServices);
        this.cvAlerte = findViewById(R.id.cardViewAlerte);

        calendar = Calendar.getInstance();
        heureFormat24h = new SimpleDateFormat("HH:mm");
        heureFormat12h = new SimpleDateFormat("KK:mm aaa");
        dateTime24h = heureFormat24h.format(calendar.getTime());
        dateTime12h = heureFormat12h.format(calendar.getTime());

        Toast.makeText(getApplicationContext(), String.valueOf(dateTime12h), Toast.LENGTH_SHORT).show();

        cvConso.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),SuiviConso.class)));

        cvFuite.setOnClickListener(view -> Toast.makeText(this, "Signaler une fuite d'eau", Toast.LENGTH_SHORT).show());

        cardViewServices.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Services.class)));

        cvAlerte.setOnClickListener(view -> Toast.makeText(this, "Mes alertes", Toast.LENGTH_SHORT).show());
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
        if (item.getItemId() == R.id.action_home) {
            Intent activityHome = new Intent(getApplicationContext(), Home.class);
            startActivity(activityHome);
            this.finish();
            return true;
        }

        //Rédirige vers le site https://passivecomponents.me/
        /*if (item.getItemId() == R.id.savoir_plus){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://passivecomponents.me/")));
        }*/
        return super.onOptionsItemSelected(item);
    }

}