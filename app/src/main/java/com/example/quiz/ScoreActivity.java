package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView correctCount, skipCount,totalscore;
    private Button doneBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        correctCount = findViewById(R.id.correct_count);
        skipCount = findViewById(R.id.skip_count);
        doneBtn = findViewById(R.id.done_btn);
        totalscore = findViewById(R.id.totalscore);

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
}
