package com.astdev.ploof;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class MainFragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout menu_Lateral;
    NavigationView nav_view;
    ActionBarDrawerToggle toggle;

    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "UseCompatLoadingForDrawables",
            "NonConstantResourceId", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        menu_Lateral = findViewById(R.id.drawerLayout);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        MaterialToolbar mToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Pour le menu lateral
        nav_view.bringToFront();
        toggle = new ActionBarDrawerToggle(this,menu_Lateral, R.string.open_lateral_menu, R.string.close_lateral_menu);
        menu_Lateral.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        menu_Lateral.closeDrawer(GravityCompat.START);
        Fragment fragment=null;
        switch (item.getItemId()){
            case R.id.servicesFragment: fragment = new ServicesFragment();break;
            case R.id.facturesFragment: fragment = new FacturesFragment();break;
            case R.id.aboutFragment: fragment = new AboutFragment();break;
            case R.id.consoFragment: fragment = new ConsoFragment();break;
        }
        if (fragment == null) throw new AssertionError();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
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
        if (item.getItemId() == R.id.notifsFragment){
            Fragment fragment = new NotifsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
            return true;
        }
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}