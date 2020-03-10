package com.learnakantwi.twiguides;

public class subConversation implements Comparable<subConversation> {

    private String twiConversation;
    private String englishConversation;

    public subConversation(String twiConversation, String englishConversation) {
        this.twiConversation = twiConversation;
        this.englishConversation = englishConversation;
    }

    public subConversation(){
    }

    public String getTwiConversation() {
        return twiConversation;
    }

    public void setTwiConversation(String twiConversation) {
        this.twiConversation = twiConversation;
    }

    public String getEnglishConversation() {
        return englishConversation;
    }

    public void setEnglishConversation(String englishConversation) {
        this.englishConversation = englishConversation;
    }

    @Override
    public int compareTo(subConversation o) {
        int compareInt = this.twiConversation.compareTo(o.twiConversation);
        if (compareInt<0){
            return -1;
        }
        else if (compareInt>0){
            return 1;
        }
        else {
            return 0;
        }
    }

}

