package com.learnakantwi.twiguides;

public class RealTimeDatabaseUsers {


        public String username;
        public String email;
        public String ID;

        public RealTimeDatabaseUsers(){
        }

        public RealTimeDatabaseUsers (String username, String email){
            this.username = username;
            this.email= email;
        }

  public RealTimeDatabaseUsers(String Identity, String username, String email){
            this.ID = Identity;
            this.username = username;
            this.email = email;
  }

    }
