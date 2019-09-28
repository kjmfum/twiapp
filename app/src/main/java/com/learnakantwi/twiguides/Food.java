package com.learnakantwi.twiguides;

public class Food implements Comparable<Food> {

    String englishFood;
    String twiFood;


    public Food(String englishFood, String twiFood) {
        this.englishFood = englishFood;
        this.twiFood = twiFood;
    }

    public String getEnglishFood() {
        return englishFood;
    }

    public void setEnglishFood(String englishFood) {
        this.englishFood = englishFood;
    }

    public String getTwiFood() {
        return twiFood;
    }

    public void setTwiFood(String twiFood) {
        this.twiFood = twiFood;
    }

    @Override
    public int compareTo(Food o) {
        int compareInt = this.englishFood.compareTo(o.englishFood);
        if (compareInt<0){
            return -1;
        }
        else if (compareInt>0){
            return 1;
        }
        else return 0;
    }
}
