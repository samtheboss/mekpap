package com.mekpap.mekPap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button sign_up, sign_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign_in = findViewById(R.id.usr_login);
        sign_up = findViewById(R.id.user_singup);

    }

    public void sign_In(View view) {
        Intent intent = new Intent(MainActivity.this,login.class);
        startActivity(intent);

    }

    public void user_login(View view) {
        Intent intent = new Intent(MainActivity.this,register.class);
        startActivity(intent);
    }
}
