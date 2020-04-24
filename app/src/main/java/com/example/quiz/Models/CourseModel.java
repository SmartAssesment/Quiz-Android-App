package com.example.quiz.models;

import android.graphics.drawable.Drawable;

public class CourseModel {
    private String id;
    private Drawable icon;
    private String Title;

    public CourseModel() {
    }

    public CourseModel(String id, Drawable icon, String title) {
        this.id = id;
        this.icon = icon;
        Title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
