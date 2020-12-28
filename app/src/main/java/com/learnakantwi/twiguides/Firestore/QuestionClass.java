package com.learnakantwi.twiguides.Firestore;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class QuestionClass {

    String answer;
    String question;
    String token;
    boolean complete;
    String email;
    @ServerTimestamp
    Date timestamp;

    public QuestionClass(){

    }

    public QuestionClass(String answer, String question, String token, boolean complete, String email, Date timestamp) {
        this.answer = answer;
        this.question = question;
        this.token = token;
        this.complete = complete;
        this.email = email;
        this.timestamp = timestamp;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
