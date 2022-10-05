package com.astdev.ploof;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.astdev.ploof.databinding.FragmentFuiteDetectedBinding;
import java.util.ArrayList;

public class FuiteDetectedFragment extends Fragment {

    FragmentFuiteDetectedBinding binding;
    private ArrayList<ListFuiteModel> fuitList;

    public FuiteDetectedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFuiteDetectedBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainFragment) requireActivity()).setActionBarTitle("Fuites détectées");

        showList();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);

        ListFuiteAdapter adapter = new ListFuiteAdapter(getContext(),fuitList);
        binding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        binding.fabFuiteD.setOnClickListener(v-> startActivity(new Intent(requireActivity(), MainFragment.class)));
    }


    private void showList() {

        fuitList = new ArrayList<>();

        String[] date = {"Fuite détectée le 27 Sep. 2022", "Fuite détectée le 10 Sep. 2022"
                ,"Fuite détectée le 03 Août. 2022","Fuite détectée le 15 Août. 2022"
                ,"Fuite détectée le 07 Juin 2022","Fuite détectée le 15 Mai 2022"};

        String[] statut = {"Non réparer", "Réparer", "Non réparer", "Réparer", "Réparer","Réparer"};

        for (int i=0;i<statut.length;i++){
            ListFuiteModel listFuite=new ListFuiteModel(date[i],statut[i]);
            fuitList.add(listFuite);
        }

    }

}