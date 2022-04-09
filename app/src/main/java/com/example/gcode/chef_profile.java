package com.example.gcode;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class chef_profile extends Fragment {

    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    String userID,userName;
    FirebaseFirestore fstore;
    View view;
    TextView max_rating,curr_rating,total_problem,status,institute;
    Button user_name;

    public chef_profile () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chef_profile, container, false);
        initialise();
        fstore.collection("Users").document(userID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user!=null){
                            userName = user.getChefID();
                            user_name.setText(userName+"  >>");
                            updateUI(userName);
                        }
                    }
                });

        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.codechef.com/users/"+userName);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return view;
    }

    public void updateUI(String userName){
        String url = "https://g-code-server.herokuapp.com/codechef/"+userName;
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("OK")){
                                JSONObject result = (JSONObject) response.getJSONArray("result").get(0);
                                max_rating.setText(result.getString("maxRating"));
                                curr_rating.setText(result.getString("rating"));
                                total_problem.setText(result.getString("problems solved"));
                                status.setText(result.getString("stars"));
                                institute.setText(result.getString("institute"));
                                max_rating.animate().translationY(0f).setDuration(200);
                                curr_rating.animate().translationY(0f).setDuration(600);
                                total_problem.animate().translationY(0f).setDuration(1000);

                            }else{
                                Toast.makeText(view.getContext(), "Unable to Fetch data", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API Error",error.toString());
                    }
                });

        queue.add(jsonObjectRequest);
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