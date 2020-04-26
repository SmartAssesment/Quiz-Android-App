package com.example.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quiz.R;
import com.example.quiz.utils.Utils;

public class ScoreActivity extends AppCompatActivity {

    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(ScoreActivity.this,R.color.white));// set status background white


        TextView scoretext =(TextView) findViewById(R.id.scoretextbox);
        TextView resulttext =(TextView) findViewById(R.id.resulttext);
        Button homebtn  = (Button) findViewById(R.id.homebtn);
        Button reviewbtn  = (Button) findViewById(R.id.reviewbtn);

        int score = getIntent().getIntExtra("score",0);
        int correctCount = getIntent().getIntExtra("correct_count",0);
        int skipCount = getIntent().getIntExtra("skip_count",0);
        int total = getIntent().getIntExtra("total",0);

        float percentage = (float) (correctCount / total)*100;
        Log.d("Correct:", String.valueOf(correctCount));
        Log.d("Total:", String.valueOf(total));
        Log.d("Percentage:", String.valueOf(percentage));
        String scorepercentage = percentage+"% Score";
        scoretext.setText(scorepercentage);
        String attempted = getColoredSpanned(total+" questions","#715CFF");
        String corrected = getColoredSpanned(correctCount + " answers","#0F80F6");
        String skipped = getColoredSpanned(skipCount+" question(s)","#D2303E");

        Spanned resultstring = Html.fromHtml("You attempted "+ attempted +"\n" +
                "and from that "+ corrected+" is correct,\n" +
                "and you skipped "+ skipped+".");
        resulttext.setText(resultstring);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScoreActivity.this,DashboardActivity.class));
                finish();
            }
        });
    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

}
