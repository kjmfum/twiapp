package com.learnakantwi.twiguides;

import android.graphics.Color;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Animals implements Comparable <Animals> {

    String englishAnimals;
    String twiAnimals;
    TextView textView;

    int imageID;
    int drawableID;

    public Animals(String englishAnimals, String twiAnimals) {
        this.englishAnimals = englishAnimals;
        this.twiAnimals = twiAnimals;
    }

    public Animals(String englishAnimals, String twiAnimals, int imageID) {
        this.englishAnimals = englishAnimals;
        this.twiAnimals = twiAnimals;
        this.imageID= imageID;
    }

    public Animals(String englishAnimals, String twiAnimals, int imageID, int drawableID) {
        this.englishAnimals = englishAnimals;
        this.twiAnimals = twiAnimals;
        this.imageID= imageID;
        this.drawableID= drawableID;
    }



    public String getEnglishAnimals() {
        return englishAnimals;
    }

    public void setEnglishAnimals(String englishAnimals) {
        this.englishAnimals = englishAnimals;
    }

    public String getTwiAnimals() {
        return twiAnimals;
    }


    public void setTwiAnimals(String twiAnimals) {
        this.twiAnimals = twiAnimals;
    }

    @Override
    public int compareTo(Animals o) {
        int compareInt = this.englishAnimals.compareTo(o.englishAnimals);
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
