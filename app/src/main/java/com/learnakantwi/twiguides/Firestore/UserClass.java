package com.learnakantwi.twiguides.Firestore;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

    @IgnoreExtraProperties
    public class UserClass{
        String userID;
        String name;
        String email;
        String token;
        @ServerTimestamp
        Date timestamp;
        ///
        String FCMToken;

        public UserClass() {
        }
/*
        public String getFCMToken() {
            return FCMToken;
        }

        public void setFCMToken(String FCMToken) {
            this.FCMToken = FCMToken;
        }*/

        public UserClass(String userID, String name, String email, String token, Date timestamp) {
            this.userID = userID;
            this.name = name;
            this.email = email;
            this.token = token;
            this.timestamp = timestamp;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }
    }

