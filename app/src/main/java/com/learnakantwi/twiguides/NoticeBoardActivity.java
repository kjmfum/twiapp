package com.learnakantwi.twiguides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.learnakantwi.twiguides.Firestore.QuestionClass;
import com.learnakantwi.twiguides.Firestore.UserClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NoticeBoardActivity extends AppCompatActivity implements RVNoticeAdapter.onClickRecycle {

    RecyclerView noticeRecyclerView;
    RVNoticeAdapter rvNoticeAdapter;
    ArrayList<Notice> noticeList;
    TextView notice1;
    TextView notice2;
    TextView tvNotice3;
    TextView tvNotice4;
    TextView tvNotice5;
    TextView tvAnswer;
    EditText etQuestion;

    EditText etQuestionEmail;
    EditText etAnswerToSend;
    Button btGetQuestion;
    Button btSendAnswer;
    Button btSendQuestion;

    FirebaseAuth mAuth;
    FirebaseUser User;
   FirebaseFirestore db = FirebaseFirestore.getInstance();
   CollectionReference usersCollection = db.collection("users");
    CollectionReference questionsCollection = db.collection("questions");
    DocumentReference Notice1ref = db.document("notice/notice1");
    DocumentReference Notice2ref = db.document("notice/notice2");

   String currentUser;
   String userEmail;
   String userDisplayName;
   String token ="";


   public void SendChat(View v){
       String questionText = etQuestion.getText().toString();
       if (User != null) {
           Map<String, Object> question = new HashMap<>();
           QuestionClass questionClass = new QuestionClass();

           if (etQuestion != null && questionText.length()>4){

            /*  questionClass.setQuestion(questionText);
              questionClass.setAnswer("Answer will appear here \n Check back within 2 days");
              questionClass.setComplete(false);
              questionClass.setEmail(User.getEmail());
              questionClass.setToken(token);*/
               //questionClass.setTimestamp(null);

               question.put("question", questionText );
               question.put("answer", "Answer will appear here \n Check back within 2 days" );
               question.put("email", User.getEmail());
               question.put("complete", false);

               question.put("token", token);
               etQuestion.setText("");
           }
           else if(etQuestion != null && questionText.length()>1){
               Toast.makeText(this, "Question too short", Toast.LENGTH_SHORT).show();
           }else{
               Toast.makeText(this, "Please ask a question", Toast.LENGTH_SHORT).show();
           }
           etQuestion.setVisibility(View.VISIBLE);
           btSendQuestion.setVisibility(View.VISIBLE);
           questionsCollection.document(User.getEmail()).set(question, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

       etQuestionEmail= findViewById(R.id.etQuestionEmail);
       etAnswerToSend = findViewById(R.id.etAnswerToSend);
        btGetQuestion = findViewById(R.id.btGetQuestions);
       btSendAnswer = findViewById(R.id.btSendAnswer);

        mAuth = FirebaseAuth.getInstance();
        etQuestion = findViewById(R.id.etQuestion);
        btSendQuestion = findViewById(R.id.btSendQuestion);
        tvAnswer = findViewById(R.id.tvAnswers);
        tvAnswer.setVisibility(View.VISIBLE);


        notice1 = findViewById(R.id.tvNotice1);
        notice2 = findViewById(R.id.tvNotice2);
        User = mAuth.getCurrentUser();

        tvNotice3 = findViewById(R.id.tvNotice3);
        tvNotice4 = findViewById(R.id.tvNotice4);
        tvNotice5 = findViewById(R.id.tvNotice5);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            // Toast.makeText(SubPHomeMainActivity.this, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                            //Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                       token = task.getResult();
                    }
                });

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

        etQuestion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    Toast.makeText(NoticeBoardActivity.this, "Got it", Toast.LENGTH_SHORT).show();
                    SendChat(textView);
                    return true;
                }
                return false;
            }
        });
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

    public void getQuestions(View v){
        getQuestions();
    }

    public void getQuestions(){
        db.collection("questions").whereEqualTo("complete",false).get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    String data="";
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            QuestionClass allQuestions = documentSnapshot.toObject(QuestionClass.class);

                            String questions = allQuestions.getQuestion();
                            // String token = allQuestions.getToken();
                            String email = allQuestions.getEmail();
                            // String email = myUsers.getEmail();
                            data += "Question: "+ questions + "\n Email: "+ email + "\n\n";
                        }
                        tvNotice5.setVisibility(View.VISIBLE);
                        tvNotice5.setText(data);
                        tvNotice5.setTextSize(20);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NoticeBoardActivity.this, "Failed \n"+e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void sendAnswer(View v){
       sendAnswer();
    }

    public  void sendAnswer(){

       if (etAnswerToSend.getText().length()>1 && etQuestionEmail !=null){
           String answer = etAnswerToSend.getText().toString();
           String email = etQuestionEmail.getText().toString().trim();

           Map<String, Object> answerMap = new HashMap<>();
           answerMap.put("answer", answer);
           answerMap.put("complete", true);

           db.collection("questions").document(email).update(answerMap).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   Toast.makeText(NoticeBoardActivity.this, "Answer Sent", Toast.LENGTH_SHORT).show();
               etAnswerToSend.setText("");
               etQuestionEmail.setText("");
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(NoticeBoardActivity.this, "This Error "+ e, Toast.LENGTH_SHORT).show();
               }
           });


       }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (User != null){

            Toast.makeText(this, User.getEmail(), Toast.LENGTH_SHORT).show();

            if (!User.isEmailVerified()){
                Toast.makeText(this, "Please Verify your Email to Enjoy future Content", Toast.LENGTH_SHORT).show();
            }
            questionsCollection.document(Objects.requireNonNull(User.getEmail())).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null){
                        tvAnswer.setVisibility(View.GONE);
                        return;
                    }
                    if (value.exists()){
                        String question = value.getString("question");
                        String answer = value.getString("answer");
                        String email = value.getString("email");


                        if (question.length()>1){
                            tvAnswer.setVisibility(View.VISIBLE);
                            tvAnswer.setText("My Question: "+ question + "\n \nAnswer: "+answer);
                            tvAnswer.setTextSize(30);
                        }
                        else{
                            tvAnswer.setVisibility(View.GONE);
                        }

                    }

                }
            });
           // db.collection("/users/"+ User.getEmail() +"/questions/questions")

          /*  db.collection("users").document(User.getEmail() +"/questions/questions").addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null){
                        tvAnswer.setVisibility(View.GONE);
                        return;
                    }
                    if (value.exists()){
                        String question = value.getString("question");
                        String answer = value.getString("answer");


                        if (question.length()>1){
                            tvAnswer.setVisibility(View.VISIBLE);
                            tvAnswer.setText("My Question: "+ question + "\n \nAnswer: "+answer);
                            tvAnswer.setTextSize(30);
                        }
                        else{
                            tvAnswer.setVisibility(View.GONE);
                        }

                    }

                }
            });*/

            if (User.getEmail().contains("kjmfum@yahoo.co.uk")){
                etQuestionEmail.setVisibility(View.VISIBLE);
                etAnswerToSend.setVisibility(View.VISIBLE);
                btGetQuestion.setVisibility(View.VISIBLE);
                btSendAnswer.setVisibility(View.VISIBLE);

                btGetQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getQuestions();
                    }
                });


             /*   db.collection("users").whereEqualTo("Verified",true).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    String data="4 :";
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            UserClass myUsers = documentSnapshot.toObject(UserClass.class);

                            String name = myUsers.getName();
                            String email = myUsers.getEmail();
                            data += "Name: "+ name + "\n Email: "+ email+ "\n\n";
                        }
                        tvNotice4.setVisibility(View.VISIBLE);
                        tvNotice4.setText(data);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeBoardActivity.this, "Failed \n"+e, Toast.LENGTH_SHORT).show();
                    }
                });*/

                ///
                getQuestions();
              /*  db.collection("questions").whereEqualTo("complete",false).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    String data="";
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            QuestionClass allQuestions = documentSnapshot.toObject(QuestionClass.class);

                            String questions = allQuestions.getQuestion();
                           // String token = allQuestions.getToken();
                            String email = allQuestions.getEmail();
                            // String email = myUsers.getEmail();
                            data += "Question: "+ questions + "\n Email: "+ email + "\n\n";
                        }
                        tvNotice5.setVisibility(View.VISIBLE);
                        tvNotice5.setText(data);
                        tvNotice5.setTextSize(20);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeBoardActivity.this, "Failed \n"+e, Toast.LENGTH_SHORT).show();
                    }
                });*/
            }


            Notice1ref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null){
                        return;
                    }
                    if (value.exists()){
                        String Notice1 = value.getString("note1");

                        notice1.setText(Notice1);


                        String Notice2 = value.getString("note2");
                        if (Notice2!=null && Notice2.length()>1){
                            notice2.setVisibility(View.VISIBLE);
                            notice2.setText(Notice2);
                            notice2.setTextSize(20);
                        }
                        else {
                            notice2.setVisibility(View.GONE);
                        }


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