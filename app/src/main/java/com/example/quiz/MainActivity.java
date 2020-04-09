package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quiz.Activities.BookmarkActivity;
import com.example.quiz.Activities.CategoriesActivity;

public class MainActivity extends AppCompatActivity {
    private Button startBtn,bookmarkBtn,survey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Status Bar
        hideNavigationBar();

        startBtn = findViewById(R.id.start_btn);
        bookmarkBtn = findViewById(R.id.bookmarks_btn);
        survey = findViewById(R.id.survey);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryIntent = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(categoryIntent);
            }
        });

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookmarkIntent = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivity(bookmarkIntent);
            }
        });

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent surveyIntent = new Intent(MainActivity.this,SurveyActivity.class);
                startActivity(surveyIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    //For Hiding Navigation Bar and Status Bar
    private void hideNavigationBar(){
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }
}
