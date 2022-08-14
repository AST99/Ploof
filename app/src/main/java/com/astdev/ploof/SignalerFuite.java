package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SignalerFuite extends AppCompatActivity {

    private ImageView imgView;
    private TextView txtViewLieuF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signaler_fuite);

        this.imgView = findViewById(R.id.imgViewF);
        this.txtViewLieuF = findViewById(R.id.lieuFuite);

        if (Home.atHome.isChecked()){
            imgView.setImageDrawable(getDrawable(R.drawable.maison));
            txtViewLieuF.setText("A domicile");
        }
        if (Home.outsideHome.isChecked()){
            imgView.setImageDrawable(getDrawable(R.drawable.rue));
            txtViewLieuF.setText("Dans la rue");
        }
    }
}