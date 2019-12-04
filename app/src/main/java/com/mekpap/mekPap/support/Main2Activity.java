package com.mekpap.mekPap.support;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mekpap.mekPap.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Main2Activity extends AppCompatActivity {

    ListView lvDiscussionTopics;
    ArrayList<String> listOfDiscussion = new ArrayList<>();
    ArrayAdapter arrayAdpt;

    String UserName;
String userid = FirebaseAuth.getInstance().getUid();
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("support").child(userid);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        lvDiscussionTopics = findViewById(R.id.lvDiscussionTopics);
        arrayAdpt = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listOfDiscussion);
        lvDiscussionTopics.setAdapter(arrayAdpt);


        getUserName();


        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                arrayAdpt.clear();
                arrayAdpt.addAll(set);
                arrayAdpt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        lvDiscussionTopics.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(getApplicationContext(), DiscussionActivity.class);
            i.putExtra("selected_topic", ((TextView)view).getText().toString());
            i.putExtra("user_name", UserName);
            startActivity(i);
        });
    }

    private void getUserName(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText userName = new EditText(this);

        builder.setView(userName);
        builder.setPositiveButton("OK", (dialog, which) -> UserName = userName.getText().toString());
        builder.setNegativeButton("Cancel", (dialog, which) -> getUserName());
        builder.show();
    }
}
