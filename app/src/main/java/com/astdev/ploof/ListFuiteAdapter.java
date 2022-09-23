package com.astdev.ploof;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ListFuiteAdapter extends RecyclerView.Adapter<ListFuiteAdapter.ListFuiteViewHolder>{

    Context context;
    ArrayList<ListFuiteModel> fuiteArrayList;

    public ListFuiteAdapter(Context context, ArrayList<ListFuiteModel> fuiteArrayList) {
        this.context = context;
        this.fuiteArrayList = fuiteArrayList;
    }

    @NonNull
    @Override
    public ListFuiteAdapter.ListFuiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fuite_detected_list_item,parent,false);
        return new ListFuiteAdapter.ListFuiteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFuiteViewHolder holder, int position) {

        ListFuiteModel listFuiteModel = fuiteArrayList.get(position);
        holder.txtViewDate.setText(listFuiteModel.date);
        holder.txtViewStatut.setText(listFuiteModel.statut);

        String strStatut = holder.txtViewStatut.getText().toString().trim();
        holder.btn.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment myFragment = new PlumberListFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,
                    myFragment).addToBackStack(null).commit();
        });

        if (strStatut.equals("Réparer")){
            holder.txtViewStatut.setTextColor(Color.parseColor("#008000"));
            holder.btn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return fuiteArrayList.size();
    }

    public static class ListFuiteViewHolder extends RecyclerView.ViewHolder{

        TextView txtViewDate, txtViewStatut;
        Button btn;

        public ListFuiteViewHolder(@NonNull View itemView) {
            super(itemView);

            txtViewDate = itemView.findViewById(R.id.Date);
            txtViewStatut = itemView.findViewById(R.id.statut);
            btn = itemView.findViewById(R.id.button2);
        }
    }

}
