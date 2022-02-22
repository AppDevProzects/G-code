package com.example.gcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button button,profileButton,register;
    FirebaseUser fuser;
    TextView appLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        button = findViewById(R.id.button);
//        profileButton = findViewById(R.id.profileButton);
//        register = findViewById(R.id.register);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        profileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,register_Activity.class));
//            }
//        });
        appLine = findViewById(R.id.appLine);
        appLine.animate().alpha(1.0f).setDuration(2000);
        appLine.animate().alpha(0.0f).setDuration(2000);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser!=null) startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        else startActivity(new Intent(MainActivity.this,register_Activity.class));

    }
}