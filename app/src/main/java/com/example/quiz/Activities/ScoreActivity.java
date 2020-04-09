package com.example.quiz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quiz.R;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Hide Status Bar
        hideNavigationBar();

        TextView correctCount = findViewById(R.id.correct_count);
        TextView skipCount = findViewById(R.id.skip_count);
        Button doneBtn = findViewById(R.id.done_btn);
        TextView totalscore = findViewById(R.id.totalscore);

        totalscore.setText("Total Score:- " + String.valueOf(getIntent().getIntExtra("score",0)));
        correctCount.setText("Correct Answers:- " + String.valueOf(getIntent().getIntExtra("correct_count",0)));
        skipCount.setText("Skipped Questions:- " + String.valueOf(getIntent().getIntExtra("skip_count",0)));


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
