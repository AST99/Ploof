package com.astdev.ploof;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MainFragment extends AppCompatActivity{

    private CardView cvConso, cvFuite, cardViewServices, cvAlerte;
    private ImageView imgProfil;
    private TextView txtView, txtViewClose;
    private Button btnDomicile, btnRue;



    String dateTime24h, dateTime12h;
    Calendar calendar;
    SimpleDateFormat heureFormat24h, heureFormat12h;

    public static boolean atHome, outsideHome;

    private Dialog dialog; //l'utilisateur choisie le lieux de la fuite (à domicile ou  dans la rue

    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*this.txtView = findViewById(R.id.textView);
        this.cvConso = findViewById(R.id.cardViewConso);
        this.cvFuite = findViewById(R.id.cardViewFuite);
        this.cardViewServices = findViewById(R.id.cardViewServices);
        this.cvAlerte = findViewById(R.id.cardViewAlerte);*/

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.fuitepopup);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);

        this.txtViewClose = dialog.findViewById(R.id.close);
        txtViewClose.setOnClickListener(view -> dialog.dismiss());

        this.btnDomicile = dialog.findViewById(R.id.btnDomicile);
        btnDomicile.setOnClickListener(view -> {
            atHome = true;
            outsideHome = false;
            //lieu.setAtHome("yes");
            startActivity(new Intent(getApplicationContext(), SignalerFuite.class));
        });

        this.btnRue = dialog.findViewById(R.id.btnRue);
        btnRue.setOnClickListener(view -> {
            outsideHome = true;
            atHome = false;
            startActivity(new Intent(getApplicationContext(), SignalerFuite.class));
        });

        /*calendar = Calendar.getInstance();
        heureFormat24h = new SimpleDateFormat("HH:mm");
        heureFormat12h = new SimpleDateFormat("KK:mm aaa");
        dateTime24h = heureFormat24h.format(calendar.getTime());
        dateTime12h = heureFormat12h.format(calendar.getTime());

        Toast.makeText(getApplicationContext(), String.valueOf(dateTime12h), Toast.LENGTH_SHORT).show();*/

        //Gestion des évènements du menu de navigation du bas de page
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(MainFragment.this,R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


    public void setActionBarTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
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
        if (item.getItemId() == R.id.action_deconnexion) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), ConnexionPage.class));
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}