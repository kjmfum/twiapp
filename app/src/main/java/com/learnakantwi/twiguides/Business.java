package com.learnakantwi.twiguides;

public class Business implements Comparable<Business> {

    String englishBusiness;
    String twiBusiness;


    public Business(String englishBusiness, String twiBusiness) {
        this.englishBusiness = englishBusiness;
        this.twiBusiness = twiBusiness;
    }

    public String getEnglishBusiness() {
        return englishBusiness;
    }

    public void setEnglishBusiness(String englishFood) {
        this.englishBusiness = englishFood;
    }

    public String getTwiBusiness() {
        return twiBusiness;
    }

    public void setTwiBusiness(String twiFood) {
        this.twiBusiness = twiFood;
    }

    @Override
    public int compareTo(Business o) {
        int compareInt = this.englishBusiness.compareTo(o.englishBusiness);
        if (compareInt<0){
            return -1;
        }
        else if (compareInt>0){
            return 1;
        }
        else return 0;
    }
}
