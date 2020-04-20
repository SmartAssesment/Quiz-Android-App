package com.example.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.DashboardActivity;
import com.example.quiz.MainActivity;
import com.example.quiz.R;
import com.example.quiz.models.SurveyModel;
import com.example.quiz.models.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {

    private List<String> surveydetails;
    private View decorView;
    private TextView no_indicator,question;
    private LinearLayout optionsContainer;
    private Button previous,next;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private String surveyid = "";
    private String testhistoryid = "";
    private int position = 0,count = 0;
    private String[] question_array,option1,option2,option3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // For Full Experince
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideNavigationBar());
                }
            }
        });

        // Linking layout component with variables
        Toolbar toolbar = findViewById(R.id.surveyheader);
        setSupportActionBar(toolbar);

        no_indicator = findViewById(R.id.no_indicator);
        question = findViewById(R.id.question);
        optionsContainer = findViewById(R.id.options_container);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        surveydetails = new ArrayList<>();

        question_array = getResources().getStringArray(R.array.question_array);
        option1 = getResources().getStringArray(R.array.option1);
        option2 = getResources().getStringArray(R.array.option2);
        option3 = getResources().getStringArray(R.array.option3);

        // setting default data in list
        setDatainList();

        //Display Question
        displayQuestion();

    }

    private void displayQuestion() {
        // Animating Question Loading
        playAnime(question,0,question_array[position]);
        // Handle Listener For Next Button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextButtonClick();
            }
        });
        // Handle Listener for Previous Question
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePreviousButtonClick();
            }
        });
        // Add Listener to the Options
        setOptionsListener();
    }

    private void handlePreviousButtonClick() {
        enableoption(true);
        next.setEnabled(true);
        next.setAlpha(1);
        position--;
        if(position == 0){
            previous.setEnabled(false);
        }else {
            previous.setEnabled(true);
        }
        count = 0;
        playAnime(question,0,question_array[position]);
    }

    private void handleNextButtonClick() {
        next.setEnabled(false);
        next.setAlpha(0.7f);
        enableoption(true);
        previous.setEnabled(true);
        position++;
        if(position == 5){
            storeInDatabase();
            nextActivity();
        }else{
            count = 0;
            playAnime(question,0,question_array[position]);
        }

    }

    private void storeInDatabase() {
        surveyid = myRef.child("SurveyHistory").push().getKey();
        testhistoryid = myRef.child("TestHistory").push().getKey();
        String uid = getIntent().getStringExtra("userid");
        String email = getIntent().getStringExtra("useremail").split("@")[0];
        Log.d("User Email",email);
        UserModel userModel = new UserModel("0",uid,"1",email,surveyid,testhistoryid,0);
        myRef.child("Users").child(uid).setValue(userModel);
        SurveyModel surveyModel = new SurveyModel(uid,surveydetails);
        myRef.child("SurveyHistory").child(surveyid).setValue(surveyModel);
        Toast.makeText(SurveyActivity.this, "Response Recorded Successfully", Toast.LENGTH_SHORT).show();
    }

    private void setOptionsListener() {
        for(int x = 0; x < 3; x++){
            final int finalX = x;
            optionsContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storeanswer((Button) v,finalX);
                }
            });
        }
    }

    private void playAnime(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(value == 0 && count < 3){
                    String option = "";
                    if(count ==0){
                        option = option1[position];
                    }else if(count == 1){
                        option = option2[position];
                    }else if(count == 2){
                        if(position == 0){
                            optionsContainer.getChildAt(count).setVisibility(View.VISIBLE);
                            option = option3[position];
                        }else{
                            optionsContainer.getChildAt(count).setVisibility(View.INVISIBLE);
                        }
                    }
                    playAnime(optionsContainer.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //data change
                if(value == 0){
                    try{
                        ((TextView) view).setText(data);
                        no_indicator.setText("Question: "+(position+1)+"/5");
                    }catch (ClassCastException e){
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnime(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    private void nextActivity() {
        Intent dashboardIntent = new Intent(SurveyActivity.this, DashboardActivity.class);
        startActivity(dashboardIntent);
        finish();
        return;
    }

    private void storeanswer(Button selectedoption, int index) {
        selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3BB9FF")));
        enableoption(false);
        next.setEnabled(true);
        next.setAlpha(1);
        surveydetails.set(position,String.valueOf(index));
    }

    private void enableoption(boolean enable) {
        for (int i = 0; i < 3; i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

    private void setDatainList() {
        surveydetails.add("0");
        surveydetails.add("0");
        surveydetails.add("0");
        surveydetails.add("0");
        surveydetails.add("0");
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideNavigationBar());
        }
    }

    //For Hiding Navigation Bar and Status Bar
    private int hideNavigationBar(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

}
