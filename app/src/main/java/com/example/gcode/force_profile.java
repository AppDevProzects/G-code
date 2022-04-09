package com.example.gcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;


public class force_profile extends Fragment {

    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    String userID;
    FirebaseFirestore fstore;
    View view;
    TextView max_rating,curr_rating,total_problem,status,institute;
    Button user_name;

    public force_profile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_force_profile, container, false);

        initialise();
        fstore.collection("Users").document(userID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user!=null){
                            String userName = user.getCodeforceID();
                            user_name.setText(userName);
                            updateUI(userName);
                        }
                    }
                });


        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://codeforces.com/profile/"+user_name.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        max_rating.animate().translationY(0f).setDuration(200);
        curr_rating.animate().translationY(0f).setDuration(600);
        total_problem.animate().translationY(0f).setDuration(1000);

        return view;
    }

    public void updateUI(String userName) {
        String url = "https://g-code-server.herokuapp.com/codeforces/"+userName;
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            max_rating.setText(response.getString("maxRating"));
                            curr_rating.setText(response.getString("rating"));
                            total_problem.setText(response.getString("problems solved"));
                            status.setText(response.getString("stars"));
                            institute.setText(response.getString("institute"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "Please check the internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void initialise(){
        userID = fuser.getUid();
        fstore = FirebaseFirestore.getInstance();
        user_name = view.findViewById(R.id.force_username);
        max_rating = view.findViewById(R.id.max_rating);
        curr_rating = view.findViewById(R.id.current_rating);
        total_problem = view.findViewById(R.id.problem_solved);
        status = view.findViewById(R.id.starRating);
        institute = view.findViewById(R.id.instituteName);
    }
}