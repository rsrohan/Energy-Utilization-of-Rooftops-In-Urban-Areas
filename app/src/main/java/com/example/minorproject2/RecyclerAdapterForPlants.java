package com.example.minorproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterForPlants extends RecyclerView.Adapter<RecyclerAdapterForPlants.MyHolder> {
    Context context;
    ArrayList<Plant> plants;
    public RecyclerAdapterForPlants(Context context, ArrayList<Plant> plantArrayList) {

        this.context = context;
        this.plants=plantArrayList;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {



    }

   

    @Override
    public int getItemCount() {
        //todo
        return plants.size();
        //return 7;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_plant_name, tv_req_soil, tv_req_mintemp, tv_req_maxtemp, tv_req_energy, tv_req_weather, tv_req_month;
        ImageView iv_plant;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            iv_plant = itemView.findViewById(R.id.iv_plant);
            tv_plant_name = itemView.findViewById(R.id.tv_plant_name);
            tv_req_soil = itemView.findViewById(R.id.tv_req_soil);
            tv_req_mintemp = itemView.findViewById(R.id.tv_req_mintemp);
            tv_req_maxtemp = itemView.findViewById(R.id.tv_req_maxtemp);
            tv_req_energy = itemView.findViewById(R.id.tv_req_energy);
            tv_req_weather = itemView.findViewById(R.id.tv_req_weather);
            tv_req_month = itemView.findViewById(R.id.tv_req_month);
        }
    }


}
