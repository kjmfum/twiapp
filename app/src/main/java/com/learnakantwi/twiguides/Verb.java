package com.learnakantwi.twiguides;

public class Verb {

    private String englishVerb;
   private String twiVerbPositive;
   private String twiVerbNegative;
   private String twiVerb;

    public Verb(String englishVerb, String twiVerbPositive, String twiVerbNegative) {
        this.englishVerb = englishVerb;
        this.twiVerbPositive = twiVerbPositive;
        this.twiVerbNegative = twiVerbNegative;
    }

    public Verb(String englishVerb, String twiVerb) {
        this.englishVerb = englishVerb;
        this.twiVerb = twiVerb;
    }

    public String getTwiVerb() {
        return twiVerb;
    }

    public void setTwiVerb(String twiVerb) {
        this.twiVerb = twiVerb;
    }

    public String getEnglishVerb() {
        return englishVerb;
    }

    public void setEnglishVerb(String englishVerb) {
        this.englishVerb = englishVerb;
    }

    public String getTwiVerbPositive() {
        return twiVerbPositive;
    }

    public void setTwiVerbPositive(String twiVerbPositive) {
        this.twiVerbPositive = twiVerbPositive;
    }

    public String getTwiVerbNegative() {
        return twiVerbNegative;
    }

    public void setTwiVerbNegative(String twiVerbNegative) {
        this.twiVerbNegative = twiVerbNegative;
    }
}
