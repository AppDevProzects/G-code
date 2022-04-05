package com.example.gcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    Intent intent;
    TextView userName,email,InstituteName;
    String uID;
    //User user = new User();
    FirebaseFirestore db;
    String apiresp= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialise();
        apivolly();
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
                        if (user!=null)
                        updateUI(user);
                    }
                });
    }


    void updateUI(User user){
        userName.setText(user.getName());
        email.setText(user.getEmail());
        InstituteName.setText(user.getInstitute());
    }

    public void apivolly(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://g-code-server.herokuapp.com/codechef/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            apiresp = response.getString("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("responce",apiresp);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API Error",error.toString());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
    }
}