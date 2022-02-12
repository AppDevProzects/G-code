package com.example.gcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN=1;
    EditText userName,password;
    Button signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn = findViewById(R.id.SigninButton);
        mAuth=FirebaseAuth.getInstance();
        googleAuthRequest();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=userName.getText().toString();
                String pass=password.getText().toString();



            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void googleAuthRequest() {
        GoogleSignInOptions gSignOp = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_Id)).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gSignOp);
    }

    public void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }catch (Exception e){

            }

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount user) {
        Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
        String name=user.getDisplayName();
        String lastname= user.getFamilyName();
        String email= user.getEmail();
        intent.putExtra("email",email);
        intent.putExtra("name",name);
        intent.putExtra("lastname",lastname);
    }
//
//    public void forgetPassword(View view) {
//        Intent intent = new Intent(LoginActivity.this,)
//    }
}