package com.example.quiz.Models;

public class CategoryModel {
    public String url,name,courseID;

    public CategoryModel() {
        // For Firebase
    }

    public CategoryModel(String url, String name, String courseID) {
        this.url = url;
        this.name = name;
        this.courseID = courseID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public CategoryModel(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
