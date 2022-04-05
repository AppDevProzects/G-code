package com.example.gcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Check Issue";
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN=1;
    EditText userName,password;
    Button signIn,signinGoogle;
    String user,pass;
    User newuser;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialise();
        signIn.setOnClickListener(this);
        signinGoogle.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser account = mAuth.getCurrentUser();
        if (account!=null) updateUI(account);
    }

    private void googleAuthRequest() {
        GoogleSignInOptions gSignOp = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_Id)).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gSignOp);
    }

//    public void signIn(View view) {
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            updateUI(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                user=userName.getText().toString();
                pass=password.getText().toString();
                if (user.isEmpty()){
                    makeToast("Username can not be empty!");
                    break;
                }
                if (pass.isEmpty()){
                    makeToast("Password can not be empty!");
                    break;
                }
                mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            makeToast("Login Successful !");
                            updateUI(mAuth.getCurrentUser());
                        }
                        else{
                            makeToast("Error! "+task.getException().getMessage());
                        }
                    }
                });
                break;
            case R.id.ContinueWithGoogle:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

        }
    }

    public void initialise(){
        signIn=findViewById(R.id.registerButton);
        mAuth=FirebaseAuth.getInstance();
        userName=findViewById(R.id.emailRegister);
        password=findViewById(R.id.passwordRegister);
        googleAuthRequest();
        signinGoogle=findViewById(R.id.ContinueWithGoogle);
        fstore = FirebaseFirestore.getInstance();
    }

    public void makeToast(String s){
        Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
    }


    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
        String name=user.getDisplayName();
        String email= user.getEmail();
        intent.putExtra("email",email);
        intent.putExtra("name",name);
        startActivity(intent);
    }

    public void openRegister(View view) {
        startActivity(new Intent(LoginActivity.this,register_Activity.class));
    }


//
//    public void forgetPassword(View view) {
//        Intent intent = new Intent(LoginActivity.this,)
//    }
}