package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.astdev.ploof.databinding.ActivityPlumberProfilBinding;

public class PlumberProfil extends AppCompatActivity {

    ActivityPlumberProfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlumberProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent!=null){
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            int img = intent.getIntExtra("image",R.drawable.ex1);

            binding.nomPlumber.setText(name);
            binding.phone.setText(phone);
            binding.profileImage.setImageResource(img);

        }
    }
}