package com.example.gcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intialise();
        register.setOnClickListener(this);
        contGoogle.setOnClickListener(this);
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
                fAuth.createUserWithEmailAndPassword(emailStr,passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            makeToast("User Created!");
                            updateUI(user);
                        }
                        else{
                            makeToast("Error "+task.getException());
                        }
                    }
                });
            case R.id.SignupGoogle:

        }
    }

    private void updateUI(FirebaseUser user) {
//        Intent intent = new Intent(register_Activity.this,ProfileActivity.class);
//        intent.putExtra("")
    }

    private void makeToast(String s) {
        Toast.makeText(register_Activity.this, ""+s, Toast.LENGTH_SHORT).show();
    }
}