package com.mekpap.mekPap.history;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mekpap.mekPap.R;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class historyActivity extends AppCompatActivity {
    private RecyclerView mHistoryRecyclerView;
    private String  userid;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mHistoryRecyclerView = findViewById(R.id.historyRecyclerView);
        mHistoryRecyclerView.setNestedScrollingEnabled(false);
        mHistoryRecyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.progress);
        mHistoryLayoutManager = new LinearLayoutManager(historyActivity.this);
        mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);

        mHistoryAdapter = new HistoryAdapter(getDataHistory(), historyActivity.this);

        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        //HistoryObject object = new HistoryObject("1234");

       //customerOrDriver = getIntent().getExtras().getString("customerOrDriver");
        userid = FirebaseAuth.getInstance().getUid();

        getUserHistoryIds();


        mHistoryAdapter.notifyDataSetChanged();

    }

    private void getUserHistoryIds() {
        DatabaseReference userHistoryDatabase = FirebaseDatabase.getInstance().getReference().child("history").child(userid);

        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot history : dataSnapshot.getChildren()) {
                        FetchRideInformation(history.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }


    private void FetchRideInformation(String requestId) {
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("history").child(userid).child(requestId);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String rideId = dataSnapshot.getKey();
                    Long timestamp = 0L;
                    String distance = "";
                    Double ridePrice = 0.0;

                    if (dataSnapshot.child("Time").getValue() != null) {
                        timestamp = Long.valueOf(dataSnapshot.child("Time").getValue().toString());
                    }

                        progressBar.setVisibility(View.GONE);


                    HistoryObject obj = new HistoryObject(rideId, getDate(timestamp));
                    resultHistory.add(obj);
                    mHistoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String getDate(Long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("MM-dd-yyyy hh:mm", cal).toString();
        return date;
    }


    private ArrayList resultHistory = new ArrayList();

    private List<HistoryObject> getDataHistory() {
        return resultHistory;
    }
}
