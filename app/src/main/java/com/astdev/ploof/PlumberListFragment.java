package com.astdev.ploof;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class PlumberListFragment extends Fragment {

    private ArrayList<PlumberModel> plumberList;

    public PlumberListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plumber_list, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainFragment) requireActivity()).setActionBarTitle("Contacter un plombier");

        showList();
        RecyclerView recylerView = view.findViewById(R.id.recyclerView);
        recylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recylerView.setHasFixedSize(true);

        PlumberAdapter adapter = new PlumberAdapter(getContext(),plumberList);
        recylerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showList() {

        plumberList = new ArrayList<>();

        int[] imgId = {R.drawable.ex1, R.drawable.ex2, R.drawable.service_plumber, R.drawable.vidange,
                R.drawable.signaler_fuite, R.drawable.fuite_detecte,
                R.drawable.ex1, R.drawable.ex2, R.drawable.service_plumber,};

        String[] plumberName = {"Ouedroago Amidou", "Kaboré Cedric", "Koné Mohamed", "Sawadogo Abdoul",
                "Ouattara Ali","Jean Christoph","Diallo Amina", "Coulibaly Fanta","Gondo Junior"};

        String[] phoneNum = {"89822505", "05362221", "58327422", "05134798", "07217745","05837850",
                "89822505", "05362221", "58327422"};

        for (int i=0;i<imgId.length;i++){
            PlumberModel plumber =new PlumberModel(plumberName[i],phoneNum[i], imgId[i]);
            plumberList.add(plumber);
        }

    }
}