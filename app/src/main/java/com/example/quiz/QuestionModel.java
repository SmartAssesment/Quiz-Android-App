package com.example.quiz;

import java.util.List;

public class QuestionModel {
    private String correct;
    private String level;
    private List<String> options;
    private String qID;
    private String ques;
    private String solution;
    private int typeId;

    public QuestionModel() {
    }

    public QuestionModel(String correct, String level, List<String> options, String qID, String ques, String solution, int typeId) {
        this.correct = correct;
        this.level = level;
        this.options = options;
        this.qID = qID;
        this.ques = ques;
        this.solution = solution;
        this.typeId = typeId;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getqID() {
        return qID;
    }

    public void setqID(String qID) {
        this.qID = qID;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}