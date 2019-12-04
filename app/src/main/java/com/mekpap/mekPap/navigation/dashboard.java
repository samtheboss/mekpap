package com.mekpap.mekPap.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mekpap.mekPap.R;
import com.mekpap.mekPap.customer.customer_map;
import com.mekpap.mekPap.history.historyActivity;
import com.mekpap.mekPap.login;
import com.mekpap.mekPap.profile;



public class dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void bookAppointment(View view) {
        Intent intent = new Intent(dashboard.this, customer_map.class);
        startActivity(intent);
    }

    public void Loadprofile(View view) {
        Intent intent = new Intent(dashboard.this, profile.class);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(dashboard.this, login.class);
        startActivity(intent);
        finish();
    }

    public void transaction(View view) {
    }

    public void orderHistory(View view) {
        Intent intent = new Intent(dashboard.this, historyActivity.class);
        intent.putExtra("customerOrDriver", "Customer");
        startActivity(intent);


    }

    public void Support(View view) {
    }
}
