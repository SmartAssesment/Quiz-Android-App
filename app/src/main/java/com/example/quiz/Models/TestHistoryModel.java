package com.example.quiz.models;

import java.util.List;
import java.util.Map;

public class TestHistoryModel {
    private Map timestamp;
    private int score,correct_count,total_count,skip_count;
    private List<TestListModel> responses;

    public TestHistoryModel() {
    }

    public TestHistoryModel(Map timestamp, int score, int correct_count, int total_count, int skip_count, List<TestListModel> responses) {
        this.timestamp = timestamp;
        this.score = score;
        this.correct_count = correct_count;
        this.total_count = total_count;
        this.skip_count = skip_count;
        this.responses = responses;
    }

    public Map getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map timestamp) {
        this.timestamp = timestamp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCorrect_count() {
        return correct_count;
    }

    public void setCorrect_count(int correct_count) {
        this.correct_count = correct_count;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getSkip_count() {
        return skip_count;
    }

    public void setSkip_count(int skip_count) {
        this.skip_count = skip_count;
    }

    public List<TestListModel> getResponses() {
        return responses;
    }

    public void setResponses(List<TestListModel> responses) {
        this.responses = responses;
    }
}
