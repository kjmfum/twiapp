package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText etPassword;
    EditText etEmail;
    EditText etUsername;
    Button btSignUp;
    TextView tvSignIn;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    private  FirebaseAuth mAuth;

    public void SignUp(){
        progressBar.setVisibility(View.VISIBLE);
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        final String username = etUsername.getText().toString().trim();

        Toast.makeText(this, email + ": " + password, Toast.LENGTH_SHORT).show();


        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(username)){
            etEmail.setError("No email entered");
            etPassword.setError("No password entered");
            etPassword.setError("Please enter a username");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (TextUtils.isEmpty(email)){
            etEmail.setError("No email entered");
        }
        else if (TextUtils.isEmpty(password)){
            etPassword.setError("No password entered");
        }
        else if (TextUtils.isEmpty(username)) {
            etPassword.setError("Please enter a username");
        }
        else {
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUpActivity.this, "Success" + " " + user, Toast.LENGTH_SHORT).show();
                                writeNewUser(currentUser.getUid(), username,currentUser.getEmail());
                                progressBar.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(SignUpActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }

    private void writeNewUser(String userID, String name, String email){

        //User user = new User();

        RealTimeDatabaseUsers users = new RealTimeDatabaseUsers(userID, name, email);
        databaseReference.child("Users").setValue(users);
        //databaseReference.updateChildren("Users")



        //mDatabase.child("Baby").setValue(user);

    /*    ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(listener);*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users/");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Toast.makeText(SignUpActivity.this, "Added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RealTimeDatabaseUsers getFrom = dataSnapshot.getValue(RealTimeDatabaseUsers.class);
                String me = getFrom.username + getFrom.email;

               // Toast.makeText(SignUpActivity.this, "From DB: "+ me , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        btSignUp = findViewById(R.id.btSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);
        progressBar = findViewById(R.id.progressBar);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(getApplicationContext(), SignInActivity.class  );
                startActivity(signIn);
            }
        });




    }

    @Override
    public void onStart() {
        super.onStart();


        // Check if user is signed in (non-null) and update UI accordingly.
   currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, currentUser.getEmail()+ " ", Toast.LENGTH_SHORT).show();
            //toast.setText("nothing subscribed");
            //toast.show();
            //Intent homeIntent = new Intent(getApplicationContext(), SignInActivity.class);
            //Intent homeIntent = new Intent(getApplicationContext(), HomeMainActivity.class);
            //Intent homeIntent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
            //startActivity(homeIntent);
            //finish();
            // User is signed in
        }
        //updateUI(currentUser);

    }
}
