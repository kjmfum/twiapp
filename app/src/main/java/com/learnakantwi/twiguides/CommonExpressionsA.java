package com.learnakantwi.twiguides;

public class CommonExpressionsA implements Comparable<CommonExpressionsA> {

    String englishCommonExpressionsA;
    String twiCommonExpressionsA;


    public CommonExpressionsA(String englishCommonExpressionsA, String twiCommonExpressionsA) {
        this.englishCommonExpressionsA = englishCommonExpressionsA;
        this.twiCommonExpressionsA = twiCommonExpressionsA;
    }

    public String getEnglishCommonExpressionsA() {
        return englishCommonExpressionsA;
    }

    public void setEnglishCommonExpressionsA(String englishCommonExpressionsA) {
        this.englishCommonExpressionsA = englishCommonExpressionsA;
    }

    public String getTwiCommonExpressionsA() {
        return twiCommonExpressionsA;
    }

    public void setTwiCommonExpressionsA(String twiCommonExpressionsA) {
        this.twiCommonExpressionsA = twiCommonExpressionsA;
    }

    @Override
    public int compareTo(CommonExpressionsA o) {
        int compareInt = this.englishCommonExpressionsA.compareTo(o.englishCommonExpressionsA);
        if (compareInt<0){
            return -1;
        }
        else if (compareInt>0){
            return 1;
        }
        else return 0;
    }
}
