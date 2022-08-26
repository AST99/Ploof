package com.astdev.ploof;

import static androidx.navigation.Navigation.findNavController;
import static com.google.android.gms.common.util.CollectionUtils.setOf;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MainFragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CardView cvConso, cvFuite, cardViewServices, cvAlerte;
    private ImageView imgProfil;
    private TextView txtView, txtViewClose;
    private Button btnDomicile, btnRue;

    DrawerLayout menu_Lateral;
    NavigationView nav_view;
    Toolbar toolbar;

    ActionBarDrawerToggle toggle;


    String dateTime24h, dateTime12h;
    Calendar calendar;
    SimpleDateFormat heureFormat24h, heureFormat12h;

    public static boolean atHome, outsideHome;

    private MaterialToolbar mToolbar;
    private Dialog dialog; //l'utilisateur choisie le lieux de la fuite (à domicile ou  dans la rue

    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.fuitepopup);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);

        menu_Lateral = findViewById(R.id.drawerLayout);
        nav_view = findViewById(R.id.nav_view);
        /*androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        this.txtViewClose = dialog.findViewById(R.id.close);
        txtViewClose.setOnClickListener(view -> dialog.dismiss());

        this.mToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        //Gestion des évènements du menu de navigation du bas de page
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ConsoFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.consoFragment);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment=null;
            switch (item.getItemId()){
                case R.id.consoFragment: fragment = new ConsoFragment();break;
                case R.id.servicesFragment: fragment = new ServicesFragment();break;
                case R.id.profileFragment: fragment = new ProfileFragment();break;
            }
            assert fragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
            return true;
        });



        //Pour le menu lateral
        nav_view.bringToFront();
        toggle = new ActionBarDrawerToggle(this,menu_Lateral, R.string.open_lateral_menu,
                R.string.close_lateral_menu);
        menu_Lateral.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment=null;

                switch (item.getItemId()){
                    case R.id.consoFragment: fragment = new ConsoFragment();
                    menu_Lateral.closeDrawer(GravityCompat.START);
                    break;
                    case R.id.deconnexion:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), ConnexionPage.class));break;
                }

                assert fragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
                return true;
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        menu_Lateral.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (menu_Lateral.isDrawerOpen(GravityCompat.START)) menu_Lateral.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
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
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}