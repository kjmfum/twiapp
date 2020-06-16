package com.learnakantwi.twiguides;

public class Lessons {

    private String twi;
    private String transliteration;

    public Lessons(String twi, String transliteration) {
        this.twi = twi;
        this.transliteration = transliteration;
    }

    public String getTwi() {
        return twi;
    }

    public void setTwi(String twi) {
        this.twi = twi;
    }

    String getTransliteration() {
        return "Literal Translation: "+ transliteration;
    }


    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }
}
