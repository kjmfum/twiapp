package com.learnakantwi.twiguides;

public class Animals implements Comparable <Animals> {

    String englishAnimals;
    String twiAnimals;

    public Animals(String englishAnimals, String twiAnimals) {
        this.englishAnimals = englishAnimals;
        this.twiAnimals = twiAnimals;
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
