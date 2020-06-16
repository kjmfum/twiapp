package com.learnakantwi.twiguides;

public class Proverbs {
     private String twiProverb;
    private String proverbLiteral;
     private String proverbMeaning;
     private String proverbQuiz;
    private String pQuizA;
    private String pQuizB;
    private String pQuizC;

    public Proverbs (String proverbQuiz,String a, String b, String c, String twiProverb,String proverbLiteral, String proverbMeaning){
        this.twiProverb = twiProverb;
        this.proverbQuiz = proverbQuiz;
        this.proverbMeaning = proverbMeaning;
        this.proverbLiteral=proverbLiteral;
        this.pQuizA=a;
        this.pQuizB=b;
        this.pQuizC=c;
    }

    public String getProverbQuiz() {
        return proverbQuiz;
    }

    public void setProverbQuiz(String proverbQuiz) {
        this.proverbQuiz = proverbQuiz;
    }

    public String getpQuizA() {
        return pQuizA;
    }

    public void setpQuizA(String pQuizA) {
        this.pQuizA = pQuizA;
    }

    public String getpQuizB() {
        return pQuizB;
    }

    public void setpQuizB(String pQuizB) {
        this.pQuizB = pQuizB;
    }

    public String getpQuizC() {
        return pQuizC;
    }


    public Proverbs(String twiProverb, String proverbLiteral, String proverbMeaning) {
        this.twiProverb = twiProverb;
        this.proverbLiteral = proverbLiteral;
        this.proverbMeaning = proverbMeaning;
    }


    public Proverbs(String twiProverb, String proverbLiteral) {
        this.twiProverb = twiProverb;
        this.proverbLiteral = proverbLiteral;
    }

    public void setpQuizC(String pQuizC) {
        this.pQuizC = pQuizC;
    }

    public String getTwiProverb() {
        return "Twi: "+ twiProverb;
    }

    public void setTwiProverb(String twiProverb) {
        this.twiProverb = twiProverb;
    }

    public String getProverbLiteral() {
        return "Literal: " + proverbLiteral;
    }

    public void setProverbLiteral(String proverbLiteral) {
        this.proverbLiteral = "Literal: " + proverbLiteral;
    }

    public String getProverbMeaning() {
        return "Meaning: " + proverbMeaning;
    }

    public void setProverbMeaning(String proverbMeaning) {
        this.proverbMeaning = proverbMeaning;
    }
}
