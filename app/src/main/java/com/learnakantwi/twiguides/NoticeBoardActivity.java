package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NoticeBoardActivity extends AppCompatActivity implements RVNoticeAdapter.onClickRecycle {

    RecyclerView noticeRecyclerView;
    RVNoticeAdapter rvNoticeAdapter;
    ArrayList<Notice> noticeList;
    TextView notice1;
    TextView tvAnswer;
    EditText etQuestion;

    Button btSendQuestion;

   FirebaseFirestore db = FirebaseFirestore.getInstance();
   CollectionReference usersCollection = db.collection("users");
    CollectionReference questionsCollection = db.collection("questions");
    DocumentReference Notice1ref = db.document("notice/notice1");
   FirebaseAuth mAuth;
   FirebaseUser User;
   String currentUser;
   String userEmail;
   String userDisplayName;


   public void SendChat(View v){
       if (User != null) {

           Map<String, Object> question = new HashMap<>();
           if (etQuestion != null){
              String questionText = etQuestion.getText().toString();
               question.put("question", questionText );
               question.put("answer", "Answer will appear here \n Check back within 2 days" );
               etQuestion.setText("");

           }

           etQuestion.setVisibility(View.VISIBLE);
           btSendQuestion.setVisibility(View.VISIBLE);
           questionsCollection.document(User.getEmail()).set(question).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   tvAnswer.setVisibility(View.VISIBLE);
                   Toast.makeText(NoticeBoardActivity.this, "Your Question has been submitted \n Check back later for an answer", Toast.LENGTH_LONG).show();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(NoticeBoardActivity.this, "Could not Send Question \n Check Your Internet Connection", Toast.LENGTH_SHORT).show();
               }
           });
       }
   }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        mAuth = FirebaseAuth.getInstance();
        etQuestion = findViewById(R.id.etQuestion);
        btSendQuestion = findViewById(R.id.btSendQuestion);
        tvAnswer = findViewById(R.id.tvAnswers);
        tvAnswer.setVisibility(View.VISIBLE);


        notice1 = findViewById(R.id.tvNotice1);
        User = mAuth.getCurrentUser();

        if (User != null){
           etQuestion.setVisibility(View.VISIBLE);
           btSendQuestion.setVisibility(View.VISIBLE);
            usersCollection.document(User.getEmail()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                String name = documentSnapshot.getString("name");
//                                notice1.setText(name);
                                Toast.makeText(NoticeBoardActivity.this, "Welcome " +name, Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NoticeBoardActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else{
            etQuestion.setVisibility(View.GONE);
            btSendQuestion.setVisibility(View.GONE);
            tvAnswer.setVisibility(View.GONE);
            notice1.setText("Please sign In to Access Content");
        }

/*        Notice1ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String Notice1 = documentSnapshot.getString("note1");
                            notice1.setText(Notice1);
                        }
                    }
                });*/


        noticeList = new ArrayList<>();

        noticeList.add(new Notice("hello", "hi"));
        noticeList.add(new Notice("hello", "hi"));




        rvNoticeAdapter = new RVNoticeAdapter(this, noticeList, this);
    /*    noticeRecyclerView = findViewById(R.id.noticeRecyclerView);
        noticeRecyclerView.setAdapter(rvNoticeAdapter);

        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(this));*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (User != null){

            if (!User.isEmailVerified()){
                Toast.makeText(this, "Please Verify your Email to Enjoy future Content", Toast.LENGTH_SHORT).show();
            }
            questionsCollection.document(Objects.requireNonNull(User.getEmail())).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null){
                        return;
                    }
                    if (value.exists()){
                        String question = value.getString("question");
                        String answer = value.getString("answer");
                        tvAnswer.setVisibility(View.VISIBLE);
                        tvAnswer.setText("My Question: "+ question + "\n \nAnswer: "+answer);
                    }

                }
            });


            Notice1ref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null){
                        return;
                    }
                    if (value.exists()){
                        String Notice1 = value.getString("note1");
                        notice1.setText(Notice1);
                    }
                }
            });
        }
    }

    @Override
    public void onMyItemClick(int position, View view) {
        String b = noticeList.get(position).english;
        Toast.makeText(this, b, Toast.LENGTH_SHORT).show();
    }
}