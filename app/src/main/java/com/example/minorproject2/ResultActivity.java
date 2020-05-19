package com.example.minorproject2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {


    HashMap<String, ArrayList<String>> resultArray;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        recyclerView = findViewById(R.id.recyclerView);
        resultArray = new HashMap<>();
        String result = getIntent().getStringExtra("result");
        //result = result.substring(1);
//        int len = result.length();
//        for (int i = 0; i < result.length(); i++) {
//            if (result.charAt(i) == '(') {
//                int j = i + 1;
//                if (j < len) {
//                    String month = "";
//                    while (result.charAt(j) != ')') {
//                        month = month + result.charAt(j);
//                        j++;
//                    }
//                    ArrayList<String> plantIndex = new ArrayList<>();
//                    String index = "";
//                    if (j < len) {
//                        while (result.charAt(j) != '(') {
//                            j++;
//                            if (j < len) {
//                                if (result.charAt(j) == '_') {
//                                    plantIndex.add(index);
//                                    index = "";
//                                } else {
//                                    index = index + result.charAt(j);
//                                }
//                            }
//
//                        }
//                    }
//
//                    resultArray.put(month, plantIndex);
//                    Log.d("TAG", "onCreate: " + month + "-" + plantIndex.size());
//                    i = j - 1;
//                    Log.d("TAG", "onCreate: " + i);
//
//                }
//
//            }
//        }
        String s = "Tamilnadu;chennai-madurai-salem::Kerala;cochin-tiruvandrum-calicut";
        s = result;
        ArrayList<String> month = new ArrayList<String>();
        ArrayList<String> plants = new ArrayList<String>();
        String string[] = s.split("::");
        for (String y : string) {
            try {
                String[] temp = y.split(";");
                month.add(temp[0]);
                plants.add(temp[1]);
            } catch (Exception e) {
            }
        }
        Log.d("TAG", "onCreate: " + month.size() + "-" + plants.size());


    }
}
