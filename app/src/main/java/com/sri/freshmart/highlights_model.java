package com.sri.freshmart;

public class highlights_model {
    String tag,ans;

    public highlights_model(String tag, String ans) {
        this.tag = tag;
        this.ans = ans;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
