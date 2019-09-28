package com.learnakantwi.twiguides;

public class Time {

    private String englishTime;
    private String twiTime;

    public Time(String englishTime, String twiTime) {
        this.englishTime = englishTime;
        this.twiTime = twiTime;
    }

    public String getEnglishTime() {
        return englishTime;
    }

    public void setEnglishTime(String englishTime) {
        this.englishTime = englishTime;
    }

    public String getTwiTime() {
        return twiTime;
    }

    public void setTwiTime(String twiTime) {
        this.twiTime = twiTime;
    }

    @Override
    public String toString() {
        return englishTime;
    }
}
