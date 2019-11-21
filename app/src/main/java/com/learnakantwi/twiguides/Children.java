package com.learnakantwi.twiguides;

public class Children implements Comparable<Children> {

    String englishChildren;
    String twiChildren;

    public Children(String englishChildren, String twiChildren) {
        this.englishChildren = englishChildren;
        this.twiChildren = twiChildren;
    }

    public Children(String twiChildren) {
        this.twiChildren = twiChildren;
    }

    public String getEnglishChildren() {
        return englishChildren;
    }

    public void setEnglishChildren(String englishChildren) {
        this.englishChildren = englishChildren;
    }

    public String getTwiChildren() {
        return twiChildren;
    }

    public void setTwiChildren(String twiChildren) {
        this.twiChildren = twiChildren;
    }

    @Override
    public int compareTo(Children o) {
        return 0;
    }
}
