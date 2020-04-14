package com.example.quiz.models;

import java.util.List;

public class SurveyModel {

    private String uid;
    private List<String> surveyresponces;
    public SurveyModel() {
        //For Firebase
    }

    public SurveyModel(String uid, List<String> surveyresponces) {
        this.uid = uid;
        this.surveyresponces = surveyresponces;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getSurveyresponces() {
        return surveyresponces;
    }

    public void setSurveyresponces(List<String> surveyresponces) {
        this.surveyresponces = surveyresponces;
    }
}
