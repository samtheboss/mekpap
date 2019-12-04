package com.mekpap.mekPap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mekpap.mekPap.navigation.menu;


import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.makeText;

public class register extends AppCompatActivity {
    TextInputEditText user, email, pass, cpass, phone;
    private FirebaseAuth mAuth;
    LinearLayout layout;
    DatabaseReference currentuser;
    FirebaseDatabase firebaseDatabase;
    Button signup, customer_signup, settings;
    RadioButton mal, fmal;
    String gender;
    private de.hdodenhof.circleimageview.CircleImageView mprofilepic;
    String userName, UsrEmail, PhoneNbr, genderUdr;
    private Uri resultUri;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
 DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //firebase authentication
        mAuth = FirebaseAuth.getInstance();
//        String userid = mAuth.getCurrentUser().getUid();
        //  currentuser = FirebaseDatabase.getInstance().getReference().child("user_inform").child("drivers").child(userid);
        //  getUserInformation()
       // documentReference = db.document("Users/");

        layout = findViewById(R.id.parentlayou);
        user = findViewById(R.id.user);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.confirm_pass);
        phone = findViewById(R.id.phone_number);
        signup = findViewById(R.id.register);
        // mal = findViewById(R.id.male);
        //   fmal = findViewById(R.id.femalw);
        //   customer_signup = findViewById(R.id.customer);
        mprofilepic = findViewById(R.id.profile_image);

        mprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && requestCode == Activity.RESULT_OK) {
            final Uri imageuri = data.getData();
            resultUri = imageuri;
            mprofilepic.setImageURI(resultUri);

        }
    }

    public void signup_user(View view) {
        final ProgressDialog dialog = ProgressDialog.show(register.this, "please wait", "loading...", true);

        //layout.setBackgroundResource(R.color.colorAccent);

        final String userEmail = email.getText().toString().trim();
        String passrd = pass.getText().toString().trim();
        String confrimpass = cpass.getText().toString().trim();
        final String phone_nuber = phone.getText().toString().trim();
        final String usernameid = user.getText().toString().trim();


        if (TextUtils.isEmpty(userEmail)) {
            dialog.dismiss();
            makeText(register.this, "please enter the email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(phone_nuber)) {
            dialog.dismiss();
            makeText(register.this, "please enter the phone number", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(usernameid)) {
            dialog.dismiss();
            makeText(register.this, "please enter the User_name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(passrd)) {
            dialog.dismiss();
            makeText(register.this, "please enter the password", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(confrimpass)) {
            dialog.dismiss();
            makeText(register.this, "please enter the confirm password", Toast.LENGTH_LONG).show();
            return;
        }
        if (passrd.length() < 6) {
            dialog.dismiss();
            makeText(register.this, "password to short", Toast.LENGTH_LONG).show();
        }
//                    DatabaseReference currentuser = FirebaseDatabase.getInstance().getReference().child("user_inform").child("drivers").child(userid);
        if (passrd.equals(confrimpass)) {
            mAuth.createUserWithEmailAndPassword(userEmail, passrd)
                    .addOnCompleteListener(this, task -> {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            reqisterModel information = new reqisterModel(
                                    usernameid, userEmail, phone_nuber
                            );
                            String userid = mAuth.getCurrentUser().getUid();
                            currentuser = FirebaseDatabase.getInstance().getReference().child("user_inform").child("customers").child(userid);
                            Map<String,Object> userInfor =new HashMap<>();
                            userInfor.put("Email" ,userEmail);
                            userInfor.put("phone_Number" ,phone_nuber);
                            userInfor.put("userName" ,usernameid);
                            userInfor.put("userid" ,userid);
                            documentReference = db.document("users/"+userid);
                            documentReference.set(userInfor).addOnSuccessListener(aVoid -> makeText(register.this, "user profile created", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    makeText(register.this, "error creating this account", Toast.LENGTH_SHORT).show();
                                }
                            });




                            currentuser.setValue(information).addOnCompleteListener(task1 -> {
                                startActivity(new Intent(getApplicationContext(), menu.class));
                                makeText(register.this, "registration successful", Toast.LENGTH_SHORT).show();
                            });

                        } else {
                            makeText(register.this, "Customer registration failed", Toast.LENGTH_SHORT).show();
                        }


                    });

        }
    }


    public void saveUserTofireStore() {
        String userEmail = email.getText().toString().trim();
        String phone_nuber = phone.getText().toString().trim();
        String username = user.getText().toString().trim();
        String userloginid = mAuth.getCurrentUser().getUid();
        Map<String,Object> userInfor =new HashMap<>();
        userInfor.put("Email" ,userEmail);
        userInfor.put("phone_Number" ,phone_nuber);
        userInfor.put("userName" ,username);
        userInfor.put("userid" ,userloginid);

        documentReference.set(userInfor).addOnSuccessListener(aVoid -> makeText(register.this, "user profile created", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeText(register.this, "error creating this account", Toast.LENGTH_SHORT).show();
            }
        });


    }
void checkInputs(){
    final String userEmail = email.getText().toString().trim();
    String passrd = pass.getText().toString().trim();
    String confrimpass = cpass.getText().toString().trim();
    final String phone_nuber = phone.getText().toString().trim();
    final String userid = user.getText().toString().trim();
    final String userloginid = mAuth.getCurrentUser().getUid();


     /*   if (mal.isChecked()) {
            gender = "Male";

        }
        if (fmal.isChecked()) {
            gender = "Female";
        }*/

    if (TextUtils.isEmpty(userEmail)) {
        makeText(register.this, "please enter the email", Toast.LENGTH_LONG).show();
        email.setError("email required");
        return;
    }
    if (TextUtils.isEmpty(phone_nuber)) {
        phone.setError("phone required");
        makeText(register.this, "please enter the phone number", Toast.LENGTH_LONG).show();
        return;
    }
    if (TextUtils.isEmpty(userid)) {
        user.setError("user name required");
        makeText(register.this, "please enter the User_name", Toast.LENGTH_LONG).show();
        return;
    }
    if (TextUtils.isEmpty(passrd)) {
        pass.setError("password error");
        makeText(register.this, "please enter the password", Toast.LENGTH_LONG).show();
        return;
    }
    if (TextUtils.isEmpty(confrimpass)) {
        cpass.setError("confirm password required");
        makeText(register.this, "please enter the confirm password", Toast.LENGTH_LONG).show();
        return;
    }
    if (passrd.length() < 6) {
        pass.setError("password length to short");
        makeText(register.this, "password to short", Toast.LENGTH_LONG).show();
    }
    if (!passrd.equals(confrimpass)) {
        cpass.setError("password don't match");
    }
}
    public void signup_driver(View view) {
        String userEmail = email.getText().toString().trim();
        String passrd = pass.getText().toString().trim();
        String confrimpass = cpass.getText().toString().trim();
        if (passrd.equals(confrimpass)){
            checkInputs();
            mAuth.createUserWithEmailAndPassword(userEmail,passrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    makeText(register.this, "welcome ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(register.this,menu.class);

                    startActivity(intent);
                    saveUserTofireStore();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    makeText(register.this, "error creating this account ", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else{
            makeText(register.this, "check your email and try again ", Toast.LENGTH_SHORT).show();

        }
     /*   if (passrd.equals(confrimpass)) {
            mAuth.createUserWithEmailAndPassword(userEmail, passrd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                reqisterModel information = new reqisterModel(
                                        userid, userEmail, phone_nuber
                                );
                                if (resultUri != null) {
                                    final StorageReference filererence = FirebaseStorage.getInstance().getReference().child("profile_image").child(userloginid);

                                    Bitmap bitmap = null;
                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, boas);

                                    byte[] data = boas.toByteArray();

                                    UploadTask uploadTask = filererence.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            finish();
                                        }
                                    });
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            filererence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Map newImage = new HashMap();
                                                    newImage.put("profileImage", uri.toString());
                                                    currentuser.setValue(newImage);
                                                }
                                            });

                                        }
                                    });

                                } else {
                                    finish();
                                }

                                currentuser = FirebaseDatabase.getInstance().getReference().child("user_inform").child("customers").child(userloginid);
                                currentuser.setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(getApplicationContext(), DriverMapActivity.class));
                                        makeText(register.this, "registration successful", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                makeText(register.this, "Driver registration failed", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

        }*/
    }
}
