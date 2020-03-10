package com.learnakantwi.twiguides;

public class HomeButton implements Comparable<HomeButton> {

    private String nameofActivity;
    private int imageOfActivity;

    public HomeButton(String nameofActivity, int imageOfActivity) {
        this.nameofActivity = nameofActivity;
        this.imageOfActivity = imageOfActivity;
    }

    public HomeButton(String nameofActivity){
        this.nameofActivity = nameofActivity;
    }

    public String getNameofActivity() {
        return nameofActivity;
    }

    public void setNameofActivity(String nameofActivity) {
        this.nameofActivity = nameofActivity;
    }

    int getImageOfActivity() {
        return imageOfActivity;
    }

    public void setImageOfActivity(int imageOfActivity) {
        this.imageOfActivity = imageOfActivity;
    }

    @Override
    public int compareTo(HomeButton o) {
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

