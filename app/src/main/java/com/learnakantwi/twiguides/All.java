package com.learnakantwi.twiguides;

public class All implements Comparable<All> {

    private String englishmain;
    private String twiMain;
    private String twi1;
    private String twi2;
    private String english1;
    private String english2;

    public All(String englishmain, String twiMain, String english1, String english2, String twi1, String twi2) {
        this.englishmain = englishmain;
        this.twiMain = twiMain;
        this.twi1 = twi1;
        this.twi2 = twi2;
        this.english1 = english1;
        this.english2 = english2;

        /*   for (int i = 0; i < 4;i++ ){
            chosenSizeRand= random.nextInt(chosenSize);


            //boolean compare = allArrayList.get(i).getTwiMain().equals(twi1);
            if (i== answerLocation){
                answers.add(twi1);
            }
            else if (i>0){
                for (int j=0;j<i;j++){
                    if (answers.get(i).equals(answers.get(j))){
                        chosenSizeRand= random.nextInt(chosenSize);
                }
                else {
                answers.add(allArrayList.get(chosenSizeRand).getTwiMain());
            }*/
    }

    public String getEnglishmain() {
        return englishmain;
    }

    public void setEnglishmain(String englishmain) {
        this.englishmain = englishmain;
    }

    public String getTwiMain() {
        return twiMain;
    }

    public void setTwiMain(String twiMain) {
        this.twiMain = twiMain;
    }

    public String getTwi1() {
        return twi1;
    }

    public void setTwi1(String twi1) {
        this.twi1 = twi1;
    }

    public String getTwi2() {
        return twi2;
    }

    public void setTwi2(String twi2) {
        this.twi2 = twi2;
    }

    public String getEnglish1() {
        return english1;
    }

    public void setEnglish1(String english1) {
        this.english1 = english1;
    }

    public String getEnglish2() {
        return english2;
    }

    public void setEnglish2(String english2) {
        this.english2 = english2;
    }



    @Override
    public int compareTo(All o) {
        int compareInt = this.englishmain.compareTo(o.englishmain);
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
