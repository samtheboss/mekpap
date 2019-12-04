package com.mekpap.mekPap.navigation;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class checkonlineStatus extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser =firebaseAuth.getCurrentUser();

        if (currentUser !=null){
            Intent intent = new Intent(checkonlineStatus.this,menu.class);
            startActivity(intent);
        }
    }
}
