package com.example.quizapp;

import android.app.Application;
import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyViewModel extends AndroidViewModel {

    private final MutableLiveData<Map<String, Pair<Set<String>, ArrayList<Float>>>>  excelData = new MutableLiveData<>();
    private final ExecutorService executorService;

    public MyViewModel(@NonNull Application application) {
        super(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Map<String, Pair<Set<String>, ArrayList<Float>>>> getExcelData() {
        return excelData;
    }

    public void readExcelFile(final String internalFileName) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ExcelReader excelReader = new ExcelReader();
                    Context context = getApplication().getApplicationContext();
                    Map<String, Pair<Set<String>, ArrayList<Float>>>  data = excelReader.readExcelFile(context, internalFileName);
                    excelData.postValue(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the error accordingly
                }
            }
        });
    }
}
