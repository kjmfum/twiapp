package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

   String email = "kjmfum@yahoo.co.uk" ;
   String password = "Check12";
   TextView tvsignUp;
   EditText emailText;
   EditText passwordText;
   Button signInButton;
   TextView tvForgotPassword;
   private FirebaseAuth mAuth;

   public void SignInButtons() {
       email = emailText.getText().toString().trim();
       password = passwordText.getText().toString().trim();

       //Toast.makeText(this, email + " " + password, Toast.LENGTH_SHORT).show();

       /*if (email.length() > 0 && password.length() > 0) {*/

           if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
               emailText.setError("No email entered");
               passwordText.setError("No password entered");
           }
           else if (TextUtils.isEmpty(email)){
               emailText.setError("No email entered");
           }
           else if (TextUtils.isEmpty(password)){
               passwordText.setError("No password entered");
           }

           else{
           mAuth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               Toast.makeText(SignInActivity.this, "Authentication Success",
                                       Toast.LENGTH_SHORT).show();
                               Intent subscriptionHome = new Intent(getApplicationContext(), SubPHomeMainActivity.class );
                               startActivity(subscriptionHome);
                               // Sign in success, update UI with the signed-in user's information
                           /*Log.d(TAG, "signInWithEmail:success");
                           FirebaseUser user = mAuth.getCurrentUser();
                           updateUI(user);*/
                           } else {
                               // If sign in fails, display a message to the user.
                               //Log.w(TAG, "signInWithEmail:failure", task.getException());
                               Toast.makeText(SignInActivity.this, "Authentication failed",
                                       Toast.LENGTH_SHORT).show();
                               //updateUI(null);
                           }

                           // ...
                       }
                   });


           //mAuth.signInWithEmailAndPassword(email,password);


       }
   }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        emailText = findViewById(R.id.etEmail);
        passwordText = findViewById(R.id.etPassword);
        signInButton = findViewById(R.id.btSignIn);
        tvsignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(getApplicationContext(), ForgotPassword.class  );
                startActivity(signUp);
            }
        });

        tvsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(getApplicationContext(), SignUpActivity.class  );
                startActivity(signUp);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInButtons();
            }
        });


        mAuth = FirebaseAuth.getInstance();

        /*mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "I'm in" + " "+ user, Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            Toast.makeText(SignInActivity.this, "Too bad", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            *//*Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null); *//*
                        }

                        // ...
                    }
                });*/

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if (currentUser !=null){
            Toast.makeText(this, currentUser.getEmail() + " " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
    }

}
