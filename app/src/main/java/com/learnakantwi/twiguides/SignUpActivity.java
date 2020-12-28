package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.learnakantwi.twiguides.Firestore.UserClass;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    static UserClass userLocal;

    private static final String TAG = "SignUpActivity";
    private String username;
    String nextScreen ="";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.vocabulary_menu, menu);



        final MenuItem item = menu.findItem(R.id.menusearch);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.searchAll:
                goToAll();
                return true;
            case R.id.videoCourse:
                goToWeb();
                return true;
            case R.id.main:
                goToMain();
                return true;
            case R.id.rate:
                rateMe();
                return true;
            case R.id.share:
                shareApp();
                return true;
            case R.id.dailyTwiAlert:
                tunOnDailyTwi();
                return true;
            default:
                return false;
        }
    }


    public void tunOnDailyTwi() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.learnakantwi.twiguides", Context.MODE_PRIVATE);
        String dailyTwiPreference = sharedPreferences.getString("DailyTwiPreference", "Yes");
        sharedPreferences.edit().putString("DailyTwiPreference", "Yes").apply();
        Toast.makeText(this, "Daily Twi Alerts Turned On", Toast.LENGTH_SHORT).show();
    }
    public void shareApp(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        //sharingIntent.setAction("http://play.google.com/store/apps/details?id=" + getPackageName());
        sharingIntent.setType("text/plain");
        String shareBody = "http://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Please install this Nice Android Twi App");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void goToMain(){
        Intent intent;
        if (MainActivity.Subscribed != 1){
            intent = new Intent(getApplicationContext(), HomeMainActivity.class);
        }
        else{
            intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
        }
        startActivity(intent);
    }

    public void goToWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com/course/learn-akan-twi/?referralCode=6D321CE6AEE1834CCB0F"));
        startActivity(intent);
    }


    public void goToAll() {
        Intent intent = new Intent(getApplicationContext(), SubPAllActivity.class);
        startActivity(intent);
    }

    public void SignUp(){
        progressBar.setVisibility(View.VISIBLE);
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        username = etUsername.getText().toString().trim();

       // Toast.makeText(this, email + ": " + password, Toast.LENGTH_SHORT).show();


        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(username)){
            etEmail.setError("No email entered");
            etPassword.setError("No password entered");
            etUsername.setError("Please enter a username");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (TextUtils.isEmpty(email)){
            etEmail.setError("No email entered");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (TextUtils.isEmpty(password)){
            etPassword.setError("No password entered");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (TextUtils.isEmpty(username)) {
            etUsername.setError("Please enter a username");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (etUsername.length()<4) {
            etUsername.setError("User name should be at least 4 characters");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (etUsername.length()>21) {
            etUsername.setError("User name is too long");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (etPassword.length()<6){
            etPassword.setError("Password should be at least 6 characters");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e.toString().contains("com.google.firebase.auth.FirebaseAuthUserCollisionException")){
                                Toast.makeText(SignUpActivity.this, "This e-mail address is already in use by another account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUpActivity.this, "You are Signed Up", Toast.LENGTH_SHORT).show();
                                        currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SignUpActivity.this, "An email has been sent to "+ currentUser.getEmail() +" \n Please click the link in the email to verify account", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, "Failed \n"+ e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                progressBar.setVisibility(View.VISIBLE);
                                currentUser = mAuth.getCurrentUser();
                                //Toast.makeText(SignUpActivity.this, "Success" + " " + currentUser, Toast.LENGTH_SHORT).show();
                                //writeNewUser(currentUser.getUid(), username,currentUser.getEmail());

                                writeNewFirestore(currentUser.getUid(), username, currentUser.getEmail());

                               // Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);


                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                intent.putExtra("registeredEmail", currentUser.getEmail());
                                intent.putExtra("nextScreen", nextScreen);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(SignUpActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    });
        }
    }

 /*   private void writeNewUser(String userID, String name, String email){

        RealTimeDatabaseUsers users = new RealTimeDatabaseUsers(userID, name, email);
        databaseReference.child("Users").setValue(users);
    }*/

    private void writeNewFirestore(String userID, String name, String email){


        //RealTimeDatabaseUsers users = new RealTimeDatabaseUsers(userID, name, email);
        //databaseReference.child("Users").setValue(users);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i("getInstanceId failed", task.getException().toString());
                           // Toast.makeText(SubPHomeMainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.i("getInstanceId Good", token);
                       // Toast.makeText(SubPHomeMainActivity.this, token, Toast.LENGTH_SHORT).show();

                        sendRegistrationToServer(userID, name, email, token);
                    }
                });

    }


    public void sendRegistrationToServer(String userID, String name, String email, String token){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
       /* Map<String, String> user = new HashMap<>();

        user.put("", token);*/

        // Create a new user with a first and last name
        /////
   /*    Map<String, Object> user = new HashMap<>();
        user.put("userID", userID);
        user.put("name", name);
        user.put("email", email);
        user.put("token", token);
*/
        ////
        userLocal = new UserClass(userID, name, email, token, null);
       /* user.email = email;
        user.userID = userID;
        user.name = name;
        user.token = token;
*/

        db.collection("users").document(email)
                .set(userLocal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Debugged", "No Error adding document");
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debugged", "Error adding document", e);
                    }
                });

// Add a new document with a generated ID
      /*  db.collection("users").document(email)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Debugged", "No Error adding document");
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debugged", "Error adding document", e);
                    }
                });

        db.collection("users").document(email)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Debugged", "No Error adding document");
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debugged", "Error adding document", e);
                    }
                });
*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();

        if (intent.getStringExtra("nextScreen") != null){
            nextScreen = intent.getStringExtra("nextScreen");
        }


        mAuth = FirebaseAuth.getInstance();

       /* databaseReference = FirebaseDatabase.getInstance().getReference("Users/");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(SignUpActivity.this, "Added", Toast.LENGTH_SHORT).show();
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
        });*/


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
                finish();
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
