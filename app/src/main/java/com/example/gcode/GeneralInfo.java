package com.example.gcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class GeneralInfo extends AppCompatActivity {

    Intent intent;
    FirebaseFirestore db;
    String uID;
    User user = new User();
    private EditText codeforceID,chefID,institute,place;
    private String name,email;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_info);
        initialise();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pos=setUser();
                if (!pos) return;
                db.collection("Users").document(user.getUserID()).set(user);
                startActivity(new Intent(GeneralInfo.this,ProfileActivity.class));
            }
        });

    }


    public void initialise(){
        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        codeforceID = findViewById(R.id.forceUsername);
        chefID = findViewById(R.id.chefUsername);
        institute = findViewById(R.id.InstituteName);
        place = findViewById(R.id.place);
        email = intent.getStringExtra("email");
        name = intent.getStringExtra("name");
        register = findViewById(R.id.registerButton);
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public boolean setUser(){
        String ccid=chefID.getText().toString().trim();
        String cfid=codeforceID.getText().toString().trim();
        String inststr=institute.getText().toString().trim();
        String placestr = place.getText().toString().trim();
        if (cfid.isEmpty()){
            makeToast("Codeforce Username is required");
            return false;
        }
        if (ccid.isEmpty()){
            makeToast("Codechef Username is required");
            return false;
        }
//        if (inststr.isEmpty()){
//            makeToast("Codeforce Username is required");
//            return false;
//        }
//        if (placestr.isEmpty()){
//            makeToast("Codeforce Username is required");
//            return false;
//        }

        user.setChefID(ccid);
        user.setCodeforceID(cfid);
        user.setEmail(email);
        user.setInstitute(inststr);
        user.setName(name);
        user.setPlace(placestr);
        user.setUserID(uID);
        return true;
    }

    private void makeToast(String s) {
        Toast.makeText(GeneralInfo.this, "s", Toast.LENGTH_SHORT).show();
    }
}