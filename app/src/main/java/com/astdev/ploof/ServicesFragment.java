package com.astdev.ploof;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.astdev.ploof.databinding.FragmentServicesBinding;

public class ServicesFragment extends Fragment {

    FragmentServicesBinding binding;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((MainFragment) requireActivity()).setActionBarTitle("Services");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentServicesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainFragment) requireActivity()).setActionBarTitle("Services");

        binding.fabService.setOnClickListener(v->startActivity(new Intent(getActivity(),MainFragment.class)));
        binding.btnConterPlombier.setOnClickListener(view1 -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment myFragment = new PlumberListFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,
                    myFragment).addToBackStack(null).commit();
        });

        binding.btnConterVidangeur.setOnClickListener(view1 -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment myFragment = new PlumberListFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,
                    myFragment).addToBackStack(null).commit();
        });
    }

}