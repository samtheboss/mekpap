package com.mekpap.mekPap.navigation;

import android.location.Location;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

//import org.imperiumlabs.geofirestore.GeoFirestore;

public class geoFire {

    /*
     put this in gradle
     implementation 'com.koalap:geofirestore-android:1.1.0'
     implementation 'com.github.imperiumlabs:GeoFirestore-Android:v1.5.0'
   */
    Location mLastLocation;
    String userId = FirebaseAuth.getInstance().getUid();

    public void addgeofire() {
        CollectionReference ref = FirebaseFirestore.getInstance().collection("mechanic_requests");
        // GeoFire geoFire = new GeoFire(ref);
       // GeoFirestore geoFirestore = new GeoFirestore(ref);
       // geoFirestore.setLocation(userId, new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
    }
}
