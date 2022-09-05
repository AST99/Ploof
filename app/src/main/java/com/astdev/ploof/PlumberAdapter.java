package com.astdev.ploof;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PlumberAdapter extends RecyclerView.Adapter<PlumberAdapter.PlumberViewHolder>{

    Context context;
    ArrayList<PlumberModel> plumberArrayList;

    public PlumberAdapter(Context context, ArrayList<PlumberModel> plumberArrayList) {
        this.context = context;
        this.plumberArrayList = plumberArrayList;
    }

    @NonNull
    @Override
    public PlumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

        return new PlumberViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlumberViewHolder holder, int position) {

        PlumberModel plumberModel = plumberArrayList.get(position);
        holder.txtViewName.setText(plumberModel.nomPrenoms);
        holder.imgView.setImageResource(plumberModel.imgId);
        holder.txtViewPhone.setText(plumberModel.numeroTel);

    }

    @Override
    public int getItemCount() {
        return plumberArrayList.size();
    }

    public static class PlumberViewHolder extends RecyclerView.ViewHolder{


        ShapeableImageView imgView;
        TextView txtViewName, txtViewPhone;


        public PlumberViewHolder(@NonNull View itemView) {
            super(itemView);


            imgView = itemView.findViewById(R.id.title_image);
            txtViewName = itemView.findViewById(R.id.plumberName);
            txtViewPhone = itemView.findViewById(R.id.plumberPhone);

        }
    }
}
