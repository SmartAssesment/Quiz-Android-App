package com.example.quiz.models;

public class UserModel {
     String uid,uname,usurveyId,utesthistId;
    int utestcount,uexppoint,ulevel;

    public UserModel() {
        // For Firebase
    }

    public UserModel(String uid, String uname, String usurveyId, String utesthistId, int utestcount, int uexppoint, int ulevel) {
        this.uid = uid;
        this.uname = uname;
        this.usurveyId = usurveyId;
        this.utesthistId = utesthistId;
        this.utestcount = utestcount;
        this.uexppoint = uexppoint;
        this.ulevel = ulevel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUsurveyId() {
        return usurveyId;
    }

    public void setUsurveyId(String usurveyId) {
        this.usurveyId = usurveyId;
    }

    public String getUtesthistId() {
        return utesthistId;
    }

    public void setUtesthistId(String utesthistId) {
        this.utesthistId = utesthistId;
    }

    public int getUtestcount() {
        return utestcount;
    }

    public void setUtestcount(int utestcount) {
        this.utestcount = utestcount;
    }

    public int getUexppoint() {
        return uexppoint;
    }

    public void setUexppoint(int uexppoint) {
        this.uexppoint = uexppoint;
    }

    public int getUlevel() {
        return ulevel;
    }

    public void setUlevel(int ulevel) {
        this.ulevel = ulevel;
    }
}
