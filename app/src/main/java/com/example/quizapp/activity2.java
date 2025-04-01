package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class activity2 extends AppCompatActivity {

    EditText edit1;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    EditText edit2;
    EditText edit3;
    EditText edit4;
    EditText edit5;
    private MyViewModel excelViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        edit2 = findViewById(R.id.edit2);
        edit1 = findViewById(R.id.edit1);
        edit3 = findViewById(R.id.edit3);
        edit4 = findViewById(R.id.edit4);
        edit5 = findViewById(R.id.edit5);

        excelViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Observe the LiveData from ViewModel
        excelViewModel.getExcelData().observe(this, data -> {
            if (data != null) {
                processExcelData(data);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String internalFileName = "copied_file.xlsx";
                int assetFileName = R.raw.metro;
                try {
                    FileUtil.copyAssetFileToInternalStorage(activity2.this, assetFileName, internalFileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                excelViewModel.readExcelFile(internalFileName);
            }
        });
    }

    private void processExcelData(Map<String, Pair<Set<String>, ArrayList<Float>>> data) {
        Graph_M graph = new Graph_M();
        String src = edit1.getText().toString();
        String dest = edit2.getText().toString();

        // Validate and process sourceStations and arr
        // Process the data as needed
        for (Map.Entry<String, Pair<Set<String>, ArrayList<Float>>> entry : data.entrySet()) {
            String sourceVertex = entry.getKey();
            String[] parts = sourceVertex.split("~");
            String cleanVertex = parts[0]; // Extracting the name before '~'
            String line = parts[1]; // Extracting the line information

            if (!graph.containsVertex(cleanVertex)) {
                graph.addVertex(cleanVertex, line);
            }

            if (entry.getValue() != null) {
                Set<String> destinations = entry.getValue().first;
                ArrayList<Float> arr8 = entry.getValue().second;

                if (destinations != null) {
                    int i = 0;
                    for (String destination : destinations) {
                        if (i < arr8.size()) {
                            String[] parts1 = destination.split("~");
                            if (parts1.length == 2) {
                                String v1 = parts1[0];
                                String line1 = parts1[1];
                                if (!graph.containsVertex(v1)) {
                                    graph.addVertex(v1, line1);
                                }
                                float value = arr8.get(i);
                                graph.addEdge(cleanVertex, v1, value);
                                i++;
                            } else {
                                System.out.println("WARNING: arr8 is shorter than destinations for source vertex " + sourceVertex);
                                break;
                            }
                        }
                    }
                } else {
                    System.out.println("WARNING: entry.getValue() is null for source vertex " + sourceVertex);
                }
            }

            if (!graph.containsVertex(src)) {
                edit3.setText("THE INPUT SOURCE IS INVALID: " + src);
            } else if (!graph.containsVertex(dest)) {
                edit3.setText("THE INPUT DESTINATION IS INVALID: " + dest);
            } else {
                float distance = graph.dijkstra(src, dest, false);
                Log.d("activity2", "Minimum Distance: " + distance);

                String minDistPath = graph.Get_Minimum_Distance(src, dest);
                Log.d("activity2", "Minimum Distance Path: " + minDistPath);
                String minTimePath = graph.Get_Minimum_Time(src, dest);
                Log.d("activity2", "Minimum Time Path: " + minTimePath);
                String minLinePath = graph.Get_Minimum_LineChanges(src, dest);
                Log.d("activity2", "Minimum LineChanges: " + minLinePath);
                edit3.setText("DISTANCE : " + distance);
                edit4.setText("NUMBER OF INTERCHANGES : " + minDistPath);
                edit5.setText("START  ==>  " + minTimePath);
            }
        }
    }
}
