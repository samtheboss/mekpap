package com.mekpap.mekPap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mekpap.mekPap.navigation.splashScreen;
import com.mekpap.mekPap.support.UserDetails;

public class login extends AppCompatActivity {
    TextInputEditText usr, pass;
    Button loginbt, signup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthlisterner;
    String userid = FirebaseAuth.getInstance().getUid();
    TextView forgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usr = findViewById(R.id.userid);
        pass = findViewById(R.id.password);
        loginbt = findViewById(R.id.login);
        signup = findViewById(R.id.signUp);
        forgetPass = findViewById(R.id.resetpass);

        forgetPass.setOnClickListener(v -> {
            alertDialog();
        });

        //firebase initialization
        mAuth = FirebaseAuth.getInstance();

    }

    public void login_user(View view) {
        final ProgressDialog dialog = ProgressDialog.show(login.this, "please wait", "loading...", true);

        final String email = usr.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            dialog.dismiss();
            usr.setError("email required");
            Toast.makeText(login.this, "enter your email password", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            pass.setError("password required");
            dialog.dismiss();
            Toast.makeText(login.this, "enter your email password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(login.this, "welcome", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login.this, splashScreen.class);
                            UserDetails.username = userid;
                            UserDetails.password = password;
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(login.this, "sign up error", Toast.LENGTH_SHORT).show();


                        }

                        // ...
                    });
        }


    }

    public void alertDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = getLayoutInflater().inflate(R.layout.resetpassword, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        final EditText userInputDialogEditText;
        userInputDialogEditText = findViewById(R.id.userInputDialog);
        String userEmail = userInputDialogEditText.getText().toString();
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Reset ", (dialogBox, id) -> {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("", "Email sent.");
                                }
                            });
                    Toast.makeText(this, "check your email", Toast.LENGTH_LONG).show();
                })

                .setNegativeButton("Cancel",
                        (dialogBox, id) -> {
                            dialogBox.cancel();
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(login.this, splashScreen.class);
            startActivity(intent);
        }

    }


    public void driver_Login(View view) {
        final ProgressDialog dialog = ProgressDialog.show(login.this, "please wait", "loading...", true);

        final String email = usr.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            usr.setError("password required");
        }
        if (TextUtils.isEmpty(password)) {
            pass.setError("password required");
            pass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    dialog.dismiss();
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(login.this, DriverMapActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(login.this, "welcome ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(login.this, "sign up error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }

                    // ...
                });


    }

    public void signUp(View view) {
        Intent intent = new Intent(login.this, register.class);
        startActivity(intent);
    }
}
