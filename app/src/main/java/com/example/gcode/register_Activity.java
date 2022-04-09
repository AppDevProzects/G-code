package com.example.gcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register_Activity extends AppCompatActivity implements View.OnClickListener {

    Button register,contGoogle;
    FirebaseAuth fAuth;
    EditText name,email,pass;
    SharedPreferences sp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intialise();
        register.setOnClickListener(this);
        contGoogle.setOnClickListener(this);
        currentPosition(0);
    }

    private void intialise() {
        register=findViewById(R.id.registerButton);
        contGoogle = findViewById(R.id.SignupGoogle);
        fAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.nameRegister);
        email = findViewById(R.id.emailRegister);
        pass = findViewById(R.id.passwordRegister);
    }

    public void openLogin(View view) {
        startActivity(new Intent(register_Activity.this,LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                String nameStr=name.getText().toString().trim();
                String emailStr=email.getText().toString().trim();
                String passStr=pass.getText().toString().trim();
                if (nameStr.isEmpty()){
                    makeToast("Name is Required! ");
                    break;
                }
                if (emailStr.isEmpty()){
                    makeToast("Email is Required! ");
                    break;
                }
                if (passStr.isEmpty()){
                    makeToast("Password is Required! ");
                    break;
                }
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(emailStr,passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            makeToast("User Created!");
                            updateUI(user);
                        }
                        else{
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            makeToast("Error!! "+task.getException().getMessage());
                        }
                    }
                });
                break;
            case R.id.SignupGoogle:

        }
    }

    private void updateUIgoogle(FirebaseUser user) {
        Intent intent = new Intent(register_Activity.this,GeneralInfo.class);
        String name=user.getDisplayName();
        String email= user.getEmail();
        intent.putExtra("email",email);
        intent.putExtra("name",name);
        startActivity(intent);
        finish();
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(register_Activity.this,GeneralInfo.class);
        String namestr=name.getText().toString().trim();
        String email= user.getEmail();
        intent.putExtra("email",email);
        intent.putExtra("name",namestr);
        currentPosition(1);
        startActivity(intent);
        finish();
    }


    private void makeToast(String s) {
        Toast.makeText(register_Activity.this, ""+s, Toast.LENGTH_SHORT).show();
    }

    void currentPosition(int val) {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Last_activity",val);
        editor.apply();
    }
}