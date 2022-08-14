package com.astdev.ploof;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Home extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private CardView cvConso, cvFuite, cardViewServices, cvAlerte;
    private ImageView imgProfil;
    private TextView txtView;



    String dateTime24h, dateTime12h;
    Calendar calendar;
    SimpleDateFormat heureFormat24h, heureFormat12h;

    public static CheckBox atHome, outsideHome;

    private Dialog dialog; //l'utilisateur choisie le lieux de la fuite (à domicile ou  dans la rue

    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.txtView = findViewById(R.id.textView);
        this.cvConso = findViewById(R.id.cardViewConso);
        this.cvFuite = findViewById(R.id.cardViewFuite);
        this.cardViewServices = findViewById(R.id.cardViewServices);
        this.cvAlerte = findViewById(R.id.cardViewAlerte);


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.setCanceledOnTouchOutside(true);

        this.atHome = dialog.findViewById(R.id.checkBoxDomicile);
        atHome.setOnCheckedChangeListener(this);
        this.outsideHome = dialog.findViewById(R.id.checkBoxHorsDomicile);
        outsideHome.setOnCheckedChangeListener(this);

        calendar = Calendar.getInstance();
        heureFormat24h = new SimpleDateFormat("HH:mm");
        heureFormat12h = new SimpleDateFormat("KK:mm aaa");
        dateTime24h = heureFormat24h.format(calendar.getTime());
        dateTime12h = heureFormat12h.format(calendar.getTime());

        Toast.makeText(getApplicationContext(), String.valueOf(dateTime12h), Toast.LENGTH_SHORT).show();

        cvConso.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),SuiviConso.class)));

        cvFuite.setOnClickListener(view -> {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cardViewServices.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Services.class)));

        cvAlerte.setOnClickListener(view -> Toast.makeText(this, "Mes alertes", Toast.LENGTH_SHORT).show());
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.checkBoxDomicile:
                if (isChecked) {

                    try {
                        //imgView.setImageDrawable(getDrawable(R.drawable.maison));
                        outsideHome.setChecked(false);
                        //lieu.setAtHome("yes");
                        startActivity(new Intent(getApplicationContext(), SignalerFuite.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }break;

            case R.id.checkBoxHorsDomicile:
                //lieu.setAtHome("no");
                if (isChecked) {
                    try {
                        //imgView.setImageDrawable(getDrawable(R.drawable.rue));
                        atHome.setChecked(false);
                        startActivity(new Intent(getApplicationContext(), SignalerFuite.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }break;
            default:break;
        }
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