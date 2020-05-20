package com.example.minorproject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

        Plant plant = plants.get(position);

        if (plant.getPlantName().toLowerCase().contains("month"))
        {
            holder.ll_plant.setVisibility(View.GONE);
            holder.ll_month.setVisibility(View.VISIBLE);
            holder.tv_month.setText(plant.getPlantName());
        }else{
            holder.ll_plant.setVisibility(View.VISIBLE);
            holder.ll_month.setVisibility(View.GONE);
            holder.tv_plant_name.setText(plant.getPlantName());
            holder.tv_req_soil.setText("Soil: "+plant.getSoilReq());
            holder.tv_req_mintemp.setText("Min Temp: "+plant.getMinTemp()+" Celsius");
            holder.tv_req_maxtemp.setText("Max Temp: "+plant.getMaxTemp()+" Celsius");
            holder.tv_req_energy.setText("Energy per square meter: "+plant.getEnergy());
            holder.tv_req_weather.setText("Suitable weather: "+plant.getWeather());
            holder.tv_req_month.setText("Grows majorly in: "+plant.getMonths());
            Glide.with(context).load(plant.getImageUrl()).into(holder.iv_plant);

        }


    }

   

    @Override
    public int getItemCount() {
        //todo
        return plants.size();
        //return 7;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_plant_name, tv_req_soil, tv_req_mintemp, tv_req_maxtemp, tv_req_energy, tv_req_weather, tv_req_month, tv_month;
        ImageView iv_plant;
        LinearLayout ll_month, ll_plant;
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
            tv_month = itemView.findViewById(R.id.tv_month);
            ll_month = itemView.findViewById(R.id.ll_month);
            ll_plant = itemView.findViewById(R.id.ll_plant);

        }
    }

}


