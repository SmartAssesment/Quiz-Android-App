package com.example.quiz.models;

import java.util.List;

public class TestHistoryModel {
    private int qtypeid;
    private String qId;
    private boolean response;

    public TestHistoryModel() {
    }

    public TestHistoryModel(int qtypeid, String qId, boolean response) {
        this.qtypeid = qtypeid;
        this.qId = qId;
        this.response = response;
    }


    public int getQtypeid() {
        return qtypeid;
    }

    public void setQtypeid(int qtypeid) {
        this.qtypeid = qtypeid;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
