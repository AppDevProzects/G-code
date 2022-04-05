package com.example.gcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("application",Context.MODE_PRIVATE);
        int val = sp.getInt("Last_activity",0);

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
        Handler handler = new Handler();
        appLine.animate().alpha(1.0f).setDuration(2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                if (fuser!=null){
                    if (val==0) newactivity(register_Activity.class);
                    else if (val==1) newactivity(GeneralInfo.class);
                    else newactivity(ProfileActivity.class);
                }
                else newactivity(LoginActivity.class);
                finish();
            }
        },3000);

    }

    public void newactivity(Class c){
        startActivity(new Intent(MainActivity.this,c));
    }
}