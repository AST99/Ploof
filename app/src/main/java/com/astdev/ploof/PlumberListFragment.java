package com.astdev.ploof;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astdev.ploof.databinding.FragmentNotifsBinding;
import com.astdev.ploof.databinding.FragmentPlumberListBinding;

import java.util.ArrayList;


public class PlumberListFragment extends Fragment {

    private ArrayList<PlumberModel> plumberList;
    FragmentPlumberListBinding binding;

    public PlumberListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlumberListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainFragment) requireActivity()).setActionBarTitle("Contacter un plombier");

        showList();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);

        PlumberAdapter adapter = new PlumberAdapter(getContext(),plumberList);
        binding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        binding.fabPlumber.setOnClickListener(v-> startActivity(new Intent(getActivity(), MainFragment.class)));
    }

    private void showList() {

        plumberList = new ArrayList<>();

        int[] imgId = {R.drawable.ex1, R.drawable.ex2, R.drawable.service_plumber, R.drawable.vidange};

        String[] plumberName = {"Ouedroago Amidou", "Kaboré Alice", "Koné Mohamed", "Sawadogo Abdoul"};

        String[] phoneNum = {"05654412", "61271187", "67014996", "63148056"};

        for (int i=0;i<imgId.length;i++){
            PlumberModel plumber =new PlumberModel(plumberName[i],phoneNum[i], imgId[i]);
            plumberList.add(plumber);
        }

    }
}