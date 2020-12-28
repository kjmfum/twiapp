package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button btResetPassword;
    EditText etEmail;
    FirebaseAuth mAuth;

    String nextScreen ="";


    public void ResetPassword(){

        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email) ){
            etEmail.setError("No email entered");
        }
        else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ForgotPassword.this, "Sent", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                       // intent.putExtra("registeredEmail", currentUser.getEmail());
                        intent.putExtra("nextScreen", nextScreen);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ForgotPassword.this, "Not Sent", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ForgotPassword.this, "Please enter a valid email or create a new Account", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

  /*  @Override
    protected void onDestroy() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        // intent.putExtra("registeredEmail", currentUser.getEmail());
        intent.putExtra("nextScreen", nextScreen);
        startActivity(intent);
        finish();
        super.onDestroy();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Intent intent = getIntent();

        if (intent.getStringExtra("nextScreen") != null){
            nextScreen = intent.getStringExtra("nextScreen");
        }

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        btResetPassword = findViewById(R.id.btResetPassword);

        btResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword();
            }
        });




    }
}
