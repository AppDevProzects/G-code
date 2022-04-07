package com.example.gcode;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

public class analysis extends Fragment {

    TextView userName,email,InstituteName;
    String uID;
    FirebaseFirestore db;
    View view;

    public analysis ()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_analysis, container, false);
        initialise();
        apivolly(null,null);

        return view ;
    }

    public void initialise(){
        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        email = view.findViewById(R.id.email);
        userName = view.findViewById(R.id.UsernameProfile);
        InstituteName = view.findViewById(R.id.InstituteName);
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(uID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user!=null) updateUI(user);
                        else updateUI(FirebaseAuth.getInstance().getCurrentUser());
                    }
                });
    }


    void updateUI(User user){
        userName.setText(user.getName());
        email.setText(user.getEmail());
        InstituteName.setText(user.getInstitute());
    }

    void updateUI(FirebaseUser user){
        userName.setText(user.getDisplayName());
        email.setText(user.getEmail());
    }

    public void apivolly(String url, EditText editText){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        //String url = "https://g-code-server.herokuapp.com/codechef/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //apiUpadate(response);
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
        startActivity(new Intent(getContext(),LoginActivity.class));
    }


}