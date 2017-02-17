package com.example;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by GH on 2/5/2017.
 */
public class OfflineCapabality extends Application {
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
