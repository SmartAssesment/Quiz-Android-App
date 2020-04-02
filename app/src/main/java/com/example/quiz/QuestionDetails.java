package com.example.quiz;

public class QuestionDetails {
    private int typeId;
    private String qID;

    public QuestionDetails() {
        //For Firebase database
    }

    public QuestionDetails(int typeId, String qID) {
        this.typeId = typeId;
        this.qID = qID;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getqID() {
        return qID;
    }

    public void setqID(String qID) {
        this.qID = qID;
    }
}
