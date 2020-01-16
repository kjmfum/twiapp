package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

   FirebaseAuth mAuth;
   String email;
   String password;
   EditText emailText;
   EditText passwordText;
   Button signInButton;

   public void SignInButtons(){
       email = emailText.getText().toString();
       password = passwordText.getText().toString();

       Toast.makeText(this, email + " " + password, Toast.LENGTH_SHORT).show();

       //mAuth.signInWithEmailAndPassword(email,password);

       mAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           // Sign in success, update UI with the signed-in user's information
                           //Log.d(TAG, "createUserWithEmail:success");
                           FirebaseUser user = mAuth.getCurrentUser();
                           Toast.makeText(SignInActivity.this, "I'm in", Toast.LENGTH_SHORT).show();
                           //updateUI(user);
                       } else {
                           Toast.makeText(SignInActivity.this,"Too bad",Toast.LENGTH_SHORT).show();
                           // If sign in fails, display a message to the user.
                            /*Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);*/
                       }

                       // ...
                   }
               });

   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        emailText = findViewById(R.id.emailID);
        passwordText = findViewById(R.id.passwordID);
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInButtons();
            }
        });


        mAuth = FirebaseAuth.getInstance();

       /* mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "I'm in", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            Toast.makeText(SignInActivity.this, "Too bad", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            *//*Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);*//*
                        }

                        // ...
                    }
                });*/

    }



}
