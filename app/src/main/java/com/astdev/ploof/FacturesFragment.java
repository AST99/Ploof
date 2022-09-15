package com.astdev.ploof;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astdev.ploof.databinding.FragmentFacturesBinding;
import com.astdev.ploof.databinding.FragmentProfileBinding;

public class FacturesFragment extends Fragment {

    FragmentFacturesBinding binding;

    public FacturesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFacturesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainFragment) requireActivity()).setActionBarTitle("Mes Factures");

        binding.fabFacture.setOnClickListener(v->startActivity(new Intent(getActivity(),MainFragment.class)));
    }
}