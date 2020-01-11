package com.learnakantwi.twiguides;

public class SubPHomeMainButton implements Comparable<SubPHomeMainButton> {

    private String nameofActivity;
    private int imageOfActivity;

    SubPHomeMainButton(String nameofActivity, int imageOfActivity) {
        this.nameofActivity = nameofActivity;
        this.imageOfActivity = imageOfActivity;
    }

    SubPHomeMainButton(String nameofActivity){
        this.nameofActivity = nameofActivity;
    }


    public String getNameofActivity() {
        return nameofActivity;
    }

    public void setNameofActivity(String nameofActivity) {
        this.nameofActivity = nameofActivity;
    }

    public int getImageOfActivity() {
        return imageOfActivity;
    }

    public void setImageOfActivity(int imageOfActivity) {
        this.imageOfActivity = imageOfActivity;
    }

    @Override
    public int compareTo(SubPHomeMainButton o) {
        int compareInt = this.nameofActivity.compareTo(o.nameofActivity);
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

