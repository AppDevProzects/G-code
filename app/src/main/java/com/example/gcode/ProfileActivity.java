package com.example.gcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    Intent intent;
    TextView userName,email,InstituteName;
    String uID;
    //User user = new User();
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialise();
    }

    public void initialise(){
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        email = findViewById(R.id.email);
        userName = findViewById(R.id.UsernameProfile);
        InstituteName = findViewById(R.id.InstituteName);
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(uID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        updateUI(user);
                    }
                });
    }


    void updateUI(User user){
        userName.setText(user.getName());
        email.setText(user.getEmail());
        InstituteName.setText(user.getInstitute());
    }
}