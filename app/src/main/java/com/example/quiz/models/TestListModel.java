package com.example.quiz.models;

public class TestListModel {
    private int qtypeid;
    private String qId;
    private int responseindex;
    private String qlevel;
    boolean lastresponse;

    public TestListModel() {
    }

    public TestListModel(int qtypeid, String qId, int responseindex, String qlevel, boolean lastresponse) {
        this.qtypeid = qtypeid;
        this.qId = qId;
        this.responseindex = responseindex;
        this.qlevel = qlevel;
        this.lastresponse = lastresponse;
    }

    public boolean isLastresponse() {
        return lastresponse;
    }

    public void setLastresponse(boolean lastresponse) {
        this.lastresponse = lastresponse;
    }

    public String getQlevel() {
        return qlevel;
    }

    public void setQlevel(String qlevel) {
        this.qlevel = qlevel;
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


    public int getResponseindex() {
        return responseindex;
    }

    public void setResponseindex(int responseindex) {
        this.responseindex = responseindex;
    }
}
