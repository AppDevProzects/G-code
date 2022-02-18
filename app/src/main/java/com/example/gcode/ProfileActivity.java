package com.example.gcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    Intent intent;
    TextView userFullName,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userFullName = findViewById(R.id.UsernameProfile);
        Email = findViewById(R.id.email);
        intent = getIntent();
        updateUI();
    }


    void updateUI(){
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        userFullName.setText(name);
        Email.setText(email);

    }
}