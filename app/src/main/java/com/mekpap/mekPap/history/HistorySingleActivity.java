package com.mekpap.mekPap.history;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mekpap.mekPap.R;


import java.util.Calendar;
import java.util.Locale;

public class HistorySingleActivity extends AppCompatActivity {
    TextView problems, carType, requestDate,carmodel;

    DatabaseReference historyDatabase;
Button ratingbt;
    private String RequestId, userId;
    RatingBar rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_single);
        problems = findViewById(R.id.problem);
        carType = findViewById(R.id.carType);
        requestDate = findViewById(R.id.date);
        rate = findViewById(R.id.ratingbar);
        ratingbt = findViewById(R.id.ratebtn);
        carmodel = findViewById(R.id.carmodel);


        RequestId = getIntent().getExtras().getString("requestId");

        userId = FirebaseAuth.getInstance().getUid();
        historyDatabase = FirebaseDatabase.getInstance().getReference().child("history").child(userId).child(RequestId);
        getRequeatInformation();

        rate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            DatabaseReference mDriverRatingDb = FirebaseDatabase.getInstance().getReference().child("history").child(userId).child(RequestId);
            mDriverRatingDb.child("Rating").setValue(rating);
        });

    }

    private void getRequeatInformation() {
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child:dataSnapshot.getChildren()){
                        if (child.getKey().equals("CarType")){
                            carType.setText(child.getValue().toString());
                        }
                        if (child.getKey().equals("carModel")){
                            carmodel.setText(child.getValue().toString());
                        }
                        if (child.getKey().equals("Time")){
                            requestDate.setText(getDate(Long.valueOf(child.getValue().toString())));
                        }
                        if (child.getKey().equals("Rating")){
                            rate.setRating(Float.valueOf(child.getValue().toString()));
                            float rating = Float.valueOf(child.getValue().toString());
                            if(rating>0){
                                rate.setVisibility(View.VISIBLE);
                                ratingbt.setVisibility(View.GONE);
                            }

                        }
                        if (child.getKey().equals("car problem")){
                            problems.setText(child.getValue().toString());

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private String getDate(Long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("MM-dd-yyyy hh:mm", cal).toString();
        return date;
    }

    public void payServices(View view) {

    }

    public void loadRateBar(View view) {
        rate.setVisibility(View.VISIBLE);
    }
}
