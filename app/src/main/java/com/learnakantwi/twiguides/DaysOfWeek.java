package com.learnakantwi.twiguides;

public class DaysOfWeek {
    private  String nameEnglish;
    private String nameTwi;

    public DaysOfWeek(String nameEnglish, String nameTwi) {
        this.nameEnglish = nameEnglish;
        this.nameTwi = nameTwi;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getNameTwi() {
        return nameTwi;
    }

    public void setNameTwi(String nameTwi) {
        this.nameTwi = nameTwi;
    }
}

