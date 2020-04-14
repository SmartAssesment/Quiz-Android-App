package com.example.quiz.models;

public class UserModel {
    String uexppoint,uid,ulevel,uname,usurveyId,utesthistId;

    public UserModel() {
        // For Firebase
    }

    public UserModel(String uexppoint, String uid, String ulevel, String uname, String usurveyId, String utesthistId) {
        this.uexppoint = uexppoint;
        this.uid = uid;
        this.ulevel = ulevel;
        this.uname = uname;
        this.usurveyId = usurveyId;
        this.utesthistId = utesthistId;
    }

    public String getUexppoint() {
        return uexppoint;
    }

    public void setUexppoint(String uexppoint) {
        this.uexppoint = uexppoint;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUlevel() {
        return ulevel;
    }

    public void setUlevel(String ulevel) {
        this.ulevel = ulevel;
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
}
