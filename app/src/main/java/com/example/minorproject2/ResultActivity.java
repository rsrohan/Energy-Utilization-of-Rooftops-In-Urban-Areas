package com.example.minorproject2;

import android.content.ReceiverCallNotAllowedException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        recyclerView = findViewById(R.id.recyclerView);
        String result = getIntent().getStringExtra("result");
        result = result.substring(2);
/*jan;13-14-23-25-28-29::feb;13-14-23-24-25-27-28-29::mar;24-25-27::apr;24-27::may;24-27::jun;24-27::jul;24-27::aug;24-27::sep;24-27::oct;24-25-27::nov;13-14-23-24-25-27-28-29::dec;13-14-23-25-28-29*/
        Log.d("TAG", "onCreate: "+result);
        String s = result;
        ArrayList<String> month = new ArrayList<String>();
        ArrayList<String> plants = new ArrayList<String>();
        String string[] = s.split("::");
        for (String y : string) {
            try {
                String[] temp = y.split(";");
                month.add("Month: "+temp[0]);
                plants.add(temp[1]);
            } catch (Exception e) {
            }
        }
        Log.d("TAG", "onCreate: " + month.get(0) + "-" + plants.size());
        ArrayList<String> plantIndex = new ArrayList<>();
        for (int i = 0; i < plants.size(); i++) {
            String indexes[] = plants.get(i).split("-");
            //plantIndex.add(null);
            plantIndex.add(month.get(i));
            plantIndex.addAll(Arrays.asList(indexes));
        }
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            ArrayList<Plant> plantArrayList = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i=0;i<plantIndex.size();i++)
            {
                if (!plantIndex.get(i).toLowerCase().contains("month"))
                {
                    int in = Integer.parseInt(plantIndex.get(i));
                    JSONObject plantObject = new JSONObject(jsonArray.get(in).toString());
                    Plant plant = new Plant();
                    plant.setPlantName(plantObject.getString("plants"));
                    plant.setEnergy(plantObject.getString("energy/ sq. m in Joules"));
                    plant.setImageUrl(plantObject.getString("image"));
                    plant.setMaxTemp(plantObject.getString("max temp"));
                    plant.setMinTemp(plantObject.getString("min temp"));
                    plant.setMonths(plantObject.getString("months"));
                    plant.setSoilReq(plantObject.getString("soil"));
                    plant.setWeather(plantObject.getString("weather"));
                    plantArrayList.add(plant);
                }else{
                    Plant plant = new Plant();
                    plant.setPlantName(plantIndex.get(i));
                    plantArrayList.add(plant);
                }
            }

            RecyclerAdapterForPlants recyclerAdapterForPlants = new RecyclerAdapterForPlants(getApplicationContext(), plantArrayList);
            recyclerView.setAdapter(recyclerAdapterForPlants);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
