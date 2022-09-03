package com.astdev.ploof;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PlombierListAdapter extends ArrayAdapter<PlombierModel> {

    public PlombierListAdapter(Context context, ArrayList<PlombierModel> plombierArrayList){
        super(context, R.layout.list_item, plombierArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        PlombierModel plombier = getItem(position);

        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        ImageView imgView = convertView.findViewById(R.id.profile_image);
        TextView plumberName = convertView.findViewById(R.id.plumberName);

        imgView.setImageResource(plombier.imgId);
        plumberName.setText(plombier.nomPrenoms);

        return convertView;


    }
}
