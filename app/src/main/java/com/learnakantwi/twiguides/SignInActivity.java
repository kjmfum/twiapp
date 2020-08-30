package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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
    String registeredEmail;


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

                               if( !mAuth.getCurrentUser().isEmailVerified()){
                                   Toast.makeText(SignInActivity.this, "Email not Verified",
                                           Toast.LENGTH_SHORT).show();
                                   mAuth.getCurrentUser().sendEmailVerification();

                               }
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
        Intent intent = new Intent(getApplicationContext(), SubPHomeMainActivity.class);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Intent intent = getIntent();

        registeredEmail = intent.getStringExtra("registeredEmail");

        emailText = findViewById(R.id.etEmail);
        passwordText = findViewById(R.id.etPassword);
        signInButton = findViewById(R.id.btSignIn);
        tvsignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        if (registeredEmail !=null){
            emailText.setText(registeredEmail);
            tvsignUp.setText("Please click on the verification link sent to your email\n and enter your password to sign in");
        }


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
