package com.mekpap.mekPap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mekpap.mekPap.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class profile extends AppCompatActivity {
    DatabaseReference currentuser;
    Button edit_dp;
    TextInputEditText user, email, phone;
    ImageView profilepic;
    String userName, UsrEmail, PhoneNbr, profileImage;
    private FirebaseAuth mAuth;
    private Uri resultUri;
    private DatabaseReference mDatabaseRef;
    private StorageTask<UploadTask.TaskSnapshot> mUploadTask;
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = findViewById(R.id.user);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        mAuth = FirebaseAuth.getInstance();
        profilepic = findViewById(R.id.profile_image);
        final String userid = mAuth.getCurrentUser().getUid();
        edit_dp = findViewById(R.id.edit_profile);
        currentuser = FirebaseDatabase.getInstance().getReference().child("user_inform").child("customers").child(userid);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        //  getUserInformation();
        documentReference = db.document("users/" + userid);
        getUserData();

        edit_dp.setOnClickListener(view -> {


            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(profile.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();

            }

        });

        profilepic.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            profilepic.setImageURI(resultUri);
            Glide.with(this).load(resultUri).into(profilepic);


        }
    }

    private void uploadFile() {
        final ProgressDialog dialog = ProgressDialog.show(profile.this, "please wait", "uploading...", true);

        if (resultUri != null) {

            final String userid = mAuth.getCurrentUser().getUid();
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference().child("profile_file").child(userid);

            mUploadTask = fileReference.putFile(resultUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            Map<String, Object> newImage = new HashMap<>();
                            newImage.put("profileImageUrl", uri.toString());
                            documentReference.update(newImage).addOnSuccessListener(
                                    o -> Toast.makeText(profile.this, "profile Picture updated successfully", Toast.LENGTH_SHORT).show());
                            dialog.dismiss();

                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(profile.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {

                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    void getUserData() {
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String UsrEmail = Objects.requireNonNull(documentSnapshot.get("Email")).toString();
                String phonenuber = Objects.requireNonNull(documentSnapshot.get("phone_Number")).toString();
                String username = Objects.requireNonNull(documentSnapshot.get("userName")).toString();
              //  String profilepicture = Objects.requireNonNull(documentSnapshot.get("profileImageUrl")).toString();

                  //  Glide.with(getApplication()).load(profilepicture).into(profilepic);

                user.setText(username);
                phone.setText(phonenuber);
                email.setText(UsrEmail);
            } else {
                Toast.makeText(profile.this, "update your Profile Data", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {

        });
    }

    private void getUserInformation() {
        currentuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("User_name") != null) {
                        userName = map.get("User_name").toString();
                        user.setText(userName);
                    }
                    if (map.get("email") != null) {
                        UsrEmail = map.get("email").toString();
                        email.setText(UsrEmail);
                    }

                    if (map.get("phone") != null) {
                        PhoneNbr = map.get("phone").toString();
                        phone.setText(PhoneNbr);
                    }
                    if (map.get("profileImageUrl") != null) {
                        profileImage = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(profileImage).into(profilepic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
