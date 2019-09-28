package com.learnakantwi.twiguides;

public class Alphabets {
    private String upper;
    private String lower;
    private String both;

    public Alphabets(String both, String upper, String lower) {
        this.upper = upper;
        this.lower = lower;
        this.both = both;
    }

    public String getUpper() {
        return upper;
    }

    public void setUpper(String upper) {
        this.upper = upper;
    }

    public String getLower() {
        return lower;
    }

    public void setLower(String lower) {
        this.lower = lower;
    }

    public String getBoth() {
        return both;
    }

    public void setBoth(String both) {
        this.both = both;
    }
}
