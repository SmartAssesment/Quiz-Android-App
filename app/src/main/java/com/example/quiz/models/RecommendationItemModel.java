package com.example.quiz.models;

public class RecommendationItemModel {
    String subtopicid;
    String subtopic_title;
    int level;
    String articleLink;
    String videoLink;

    public RecommendationItemModel() {
    }

    public RecommendationItemModel(String subtopicid, String subtopic_title, int level, String articleLink, String videoLink) {
        this.subtopicid = subtopicid;
        this.subtopic_title = subtopic_title;
        this.level = level;
        this.articleLink = articleLink;
        this.videoLink = videoLink;
    }

    public String getSubtopicid() {
        return subtopicid;
    }

    public void setSubtopicid(String subtopicid) {
        this.subtopicid = subtopicid;
    }

    public String getSubtopic_title() {
        return subtopic_title;
    }

    public void setSubtopic_title(String subtopic_title) {
        this.subtopic_title = subtopic_title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}

