package com.learnakantwi.twiguides;

import android.widget.TextView;

public class Notice implements Comparable <Notice> {

    String english;
    String twi;
    TextView textView;

    int imageID;
    int drawableID;

    public Notice(String english, String twi) {
        this.english = english;
        this.twi = twi;
    }

    public Notice(String english, String twi, int imageID) {
        this.english = english;
        this.twi = twi;
        this.imageID= imageID;
    }

    public Notice(String englishAnimals, String twiAnimals, int imageID, int drawableID) {
        this.english = englishAnimals;
        this.twi = twiAnimals;
        this.imageID= imageID;
        this.drawableID= drawableID;
    }



    public String getEnglishAnimals() {
        return english;
    }

    public void setEnglishAnimals(String englishAnimals) {
        this.english= englishAnimals;
    }

    public String getTwiAnimals() {
        return twi;
    }


    public void setTwiAnimals(String twiAnimals) {
        this.twi = twiAnimals;
    }

    @Override
    public int compareTo(Notice o) {
        int compareInt = this.english.compareTo(o.english);
        if (compareInt<0){
            return -1;
        }
        else if (compareInt>0){
            return 1;
        }
        else {
            return 0;
        }
    }
}
