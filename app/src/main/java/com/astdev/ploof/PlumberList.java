package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.astdev.ploof.databinding.ActivityMainFragmentBinding;
import com.astdev.ploof.databinding.ActivityPlumberListBinding;

import java.util.ArrayList;

public class PlumberList extends AppCompatActivity {

    ActivityPlumberListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlumberListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imgId = {R.drawable.ex1, R.drawable.ex2, R.drawable.service_plumber, R.drawable.vidange,
                R.drawable.signaler_fuite58, R.drawable.fuite_detecte};

        String[] plumberName = {"Ouedroago Amidou", "Kaboré Cedric", "Koné Mohamed", "Sawadogo Abdoul",
                "Ouattara Ali","Jean Christoph"};

        String[] phoneNum = {"89822505", "05362221", "58327422", "05134798", "07217745","05837850"};

        ArrayList<PlombierModel> plumberArrayList = new ArrayList<>();

        for (int i=0;i<imgId.length;i++){
            PlombierModel plumber =new PlombierModel(plumberName[i],phoneNum[i], imgId[i]);
            plumberArrayList.add(plumber);
        }

        PlombierListAdapter adapter = new PlombierListAdapter(PlumberList.this, plumberArrayList);

        binding.listView.setAdapter(adapter);
        binding.listView.setClickable(true);
        binding.listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(PlumberList.this, PlumberProfil.class);
            intent.putExtra("name",plumberName[i]);
            intent.putExtra("phone",phoneNum[i]);
            intent.putExtra("image",imgId[i]);
            startActivity(intent);

        });
    }
}