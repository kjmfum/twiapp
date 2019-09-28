package com.learnakantwi.twiguides;

public class Pronouns {

    String englishPronoun;
    String twiPronoun;
    String singPlural;
    String subObject;

    public Pronouns(String englishPronoun, String twiPronoun, String singPlural, String subObject) {
        this.englishPronoun = englishPronoun;
        this.twiPronoun = twiPronoun;
        this.singPlural = singPlural;
        this.subObject = subObject;
    }


    public String getEnglishPronoun() {
        return englishPronoun;
    }

    public void setEnglishPronoun(String englishPronoun) {
        this.englishPronoun = englishPronoun;
    }

    public String getTwiPronoun() {
        return twiPronoun;
    }

    public void setTwiPronoun(String twiPronoun) {
        this.twiPronoun = twiPronoun;
    }

    public String getSingPlural() {
        return singPlural;
    }

    public void setSingPlural(String singPlural) {
        this.singPlural = singPlural;
    }

    public String getSubObject() {
        return subObject;
    }

    public void setSubObject(String subObject) {
        this.subObject = subObject;
    }
}
