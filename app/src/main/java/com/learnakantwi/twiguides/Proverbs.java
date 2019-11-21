package com.learnakantwi.twiguides;

public class Proverbs {
     private String twiProverb;
    private String proverbLiteral;
     private String proverbMeaning;

    public Proverbs(String twiProverb, String proverbLiteral, String proverbMeaning) {
        this.twiProverb = twiProverb;
        this.proverbLiteral = proverbLiteral;
        this.proverbMeaning = proverbMeaning;
    }


    public Proverbs(String twiProverb, String proverbLiteral) {
        this.twiProverb = twiProverb;
        this.proverbLiteral = proverbLiteral;
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
