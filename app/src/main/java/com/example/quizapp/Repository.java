package com.example.quizapp;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//Repository in MVVM architecture is an intermediary layer between the view model and data sources such as
//database for network service .It abstracts the details of data retieval and provides a clean API for
//the viewmodel to interact with.It is mainly used to manage multiple resources of data . The repository
//can gather data from different data sources, different rest API's, cache , local databases storage and it provides
//this data to the rest of the app.

//Room Database operations such as CRUD should not be executed on the main UI Thread because they can potentially bblock
//the UI causing the app to become unresponsive.We need to offload these databse operations to background threads . Ny this
//you create the Ui thread free to handle user interactions and ensure that the app remains responsive. we need to handler
//and executor service in order to execute the methods in the background otherwise AR apllication not responding or maybe crashes
public class Repository {
    private final RouteDAO routeDAO;
    private final ExecutorService executor;
    private final Handler mainThreadHandler;

    public Repository(Application application) {
        Route_DB routeDB = Route_DB.getInstance(application);
        this.routeDAO = routeDB.getRouteDAO();

        // Use a fixed thread pool with multiple threads if needed
        // this.executor = Executors.newFixedThreadPool(4); // Example: 4 threads
        this.executor = Executors.newSingleThreadExecutor(); // Sequential execution

        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    //This repository encapsulates data access and retieval logic . It receives a contact Dao objectin its constructor which provides
    //access to the local database assuming using room database.every method created in the DAO should be mentioned in the repository.
    public void addRoute(Route route){
        //Thread pool executor is used to offload these DB operations to background threads.Keep the main UI thread free
        //to handle user interactions and ensure that app remains responsive as single threaded executer is created
        //meaning that databse operations will be executed sequentially on the background threads.This can help avoid concourrency.
        //runnable objects are used to encapsulate code that is to be run asynchronsly on a separate thread.
        //By separating a runnable to an executor service , It delegates the execution of that task to the thread.
        //manged by the thread
        executor.execute(() -> {
            try {
                routeDAO.insert(route);
            } catch (Exception e) {
                // Handle exceptions or errors
                e.printStackTrace();
            }
        });
    }

    public void deleteRoute(Route route){
        //runnable objects are used to encapsulate code that is to be run asynchronsly on a separate thread.
        //By separating a runnable to an executor service , It delegates the execution of that task to the thread.
        //manged by the thread
        executor.execute(() -> {
            try {
                routeDAO.delete(route);
            } catch (Exception e) {
                // Handle exceptions or errors
                e.printStackTrace();
            }
        });
    }
    public LiveData<List<Route>> getallRoutes(){
        //Live Data is often used in both repository and the DAO to faciltate the observation of database changes and provide
        //real time updates to the UI.
        return(routeDAO.getAllRoutes());
    }

    // Method to read data from Excel and insert into Room database
    /*public void addRoutesFromExcel(InputStream filePath) {
        executor.execute(() -> {
            try {
                List<Route> entities = ExcelReader.readExcelFile(filePath).first;
                for (Route entity : entities) {
                    // Convert MyEntity to Route or adjust as per your entities
                    Route route = new Route(entity.getName(), entity.getLine() , entity.getPrev()
                            , entity.getDist_prev() , entity.getNext() , entity.getDist_next());
                    routeDAO.insert(route);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }*/

    // Optional: Clean up resources when no longer needed
    public void cleanup() {
        executor.shutdown();
    }
}
