package com.mekpap.mekPap.notification;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mekpap.mekPap.AppConstants;
import com.mekpap.mekPap.R;

import java.util.Objects;


public class mechanicInformation extends FragmentActivity implements OnMapReadyCallback {
    TextInputEditText user, email, phone;
    ImageView profilepic;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_information);
        user = findViewById(R.id.user);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        profilepic = findViewById(R.id.profile_image);
        String mechanicid = getIntent().getExtras().getString(AppConstants.MEKREQUESTID);
        documentReference = db.document("mechanics/" + mechanicid);
        getMechaniInformation();

    }

    void getMechaniInformation() {
        String mechanicid = getIntent().getExtras().getString("mekId");
user.setText(mechanicid);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
            //    String UsrEmail = Objects.requireNonNull(documentSnapshot.get("Email")).toString();
                String phonenuber = Objects.requireNonNull(documentSnapshot.get("phone")).toString();
                String username = Objects.requireNonNull(documentSnapshot.get("name")).toString();

                String profilepicture = Objects.requireNonNull(documentSnapshot.get("photoUrl")).toString();
                if (profilepicture != null) {
                    Glide.with(getApplication()).load(profilepicture).into(profilepic);
                } else {
                    profilepic.setVisibility(View.GONE);
                }
                user.setText(username);
                phone.setText(phonenuber);
            //    email.setText(UsrEmail);


            } else {

                Toast.makeText(getApplicationContext(), "update your Profile Data", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {

        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
