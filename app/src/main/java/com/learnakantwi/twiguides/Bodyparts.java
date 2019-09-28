package com.learnakantwi.twiguides;

public class Bodyparts implements Comparable<Bodyparts> {

    String englishBodyparts;
    String twiBodyparts;

    public Bodyparts(String englishBodyparts, String twiBodyparts) {
        this.englishBodyparts = englishBodyparts;
        this.twiBodyparts = twiBodyparts;
    }

    public String getEnglishBodyparts() {
        return englishBodyparts;
    }

    public void setEnglishBodyparts(String englishBodyparts) {
        this.englishBodyparts = englishBodyparts;
    }

    public String getTwiBodyparts() {
        return twiBodyparts;
    }

    public void setTwiBodyparts(String twiBodyparts) {
        this.twiBodyparts = twiBodyparts;
    }

    @Override
    public int compareTo(Bodyparts o) {
        int compareInt = this.englishBodyparts.compareTo(o.englishBodyparts);
        if (compareInt<0){
            return -1;
        }
        else if (compareInt>0){
            return 1;
        }
        else return 0;
    }
}
