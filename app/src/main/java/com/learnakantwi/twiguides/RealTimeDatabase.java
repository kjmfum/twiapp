package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;

public class RealTimeDatabase extends AppCompatActivity {

    EditText etName;
    FirebaseStorage firebaseStorage;
    Users users = new Users();
    private DatabaseReference mDatabase;

    private void writeNewUser(String userID, String name, String email){

        //User user = new User();
        Users user = new Users(name, email);
        mDatabase.child("Users").child(userID).setValue(user);
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
        setContentView(R.layout.activity_real_time_database);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        writeNewUser("001","JusticeLoveMe", "kjmfum@yahoo.co.uk");
       // writeNewUser("001","Lovely", "kjmfum5@yahoo.co.uk");
        writeNewUser("002","JusticeLove615", "kjmfum@msn.com");
        //mDatabase.setValue("Champion");

       // mDatabase.getDatabase().getReference("akan-twi-guide/Users/001").setValue("Hi");

        /*ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(listener);*/



    }

    @Override
    protected void onStart() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    public class Users{

        public String username;
        public String email;

        public Users(){
        }

        public Users (String username, String email){
            this.username = username;
            this.email= email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }
}
