package com.astdev.ploof;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SuiviConso extends AppCompatActivity {

    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suivi_conso);

        TabLayout tabLayoutConso = findViewById(R.id.tabLayoutConso);
        //selectedConsoTab();
        this.viewPager = findViewById(R.id.viewpager);

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(SuiviConso.this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayoutConso, viewPager, (tab, position) -> {
            if (position==0) tab.setText(R.string.aujourd_hui);
            if (position==1) tab.setText(R.string.hier);
            if (position==2) tab.setText(R.string.cette_semaine);
        }).attach();

        tabLayoutConso.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.setCurrentItem(0);

        //Gestion des évènements du menu de navigation du bas de page
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_conso);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_accueil){
                startActivity(new Intent(getApplicationContext(),Home.class));
                overridePendingTransition(0,0);
                return true;
            }

            if (item.getItemId() == R.id.action_services){
                startActivity(new Intent(getApplicationContext(), Services.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

    }


/************************Gère le menu du toolbar (Menu en haut de la page***************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de toolbar_menu à l'ActionBar
        getMenuInflater().inflate(R.menu.home_toolbar, menu);
        return true;
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_accueil) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.action_services) {
            Toast.makeText(getApplicationContext(), "Services", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}