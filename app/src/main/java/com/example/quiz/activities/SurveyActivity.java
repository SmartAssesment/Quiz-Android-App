package com.example.quiz.activities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.quiz.models.SurveyModel;
import com.example.quiz.models.UserModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {
    private static final String TAG = "SurveyDetails";
    private StepView stepView;

    private List<String> surveydetails;
    private View decorView;
    private TextView question;
    private LinearLayout optionsContainer;
    private Button previous, next;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private String surveyid = "";
    private String testhistoryid = "";
    private int position = 0, count = 0;
    private String[] question_array, option1, option2, option3;
    private Button selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().hide();


        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(5);
        stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                // 0 is the first step
                stepView.go(step, true);
            }
        });

        question = findViewById(R.id.question);
        optionsContainer = findViewById(R.id.linearLayout2);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        surveydetails = new ArrayList<>();

        question_array = getResources().getStringArray(R.array.question_array);
        option1 = getResources().getStringArray(R.array.option1);
        option2 = getResources().getStringArray(R.array.option2);
        option3 = getResources().getStringArray(R.array.option3);

        //Display Question
        displayQuestion();
    }

    private void displayQuestion() {
        // Animating Question Loading
        playAnime(question, 0, question_array[position]);
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
        discolorSelectedButton(selectedButton);
        int currentStep = position;
        if (currentStep > 0) {
            currentStep--;
        }
        stepView.done(false);
        stepView.go(currentStep, true);

        position--;
        if (position == 0) {
            previous.setVisibility(View.INVISIBLE);
            previous.setEnabled(false);
        } else {
            previous.setVisibility(View.VISIBLE);
            previous.setEnabled(true);
        }

        count = 0;
        playAnime(question, 0, question_array[position]);
    }

    private void handleNextButtonClick() {
        next.setEnabled(false);
        next.setAlpha(0.7f);
        enableoption(true);
        discolorSelectedButton(selectedButton);
        previous.setEnabled(true);

        int currentStep = position;
        if (currentStep < stepView.getStepCount() - 1) {
            currentStep++;
            stepView.go(currentStep, true);
        } else {
            stepView.done(true);
        }

        position++;
        if(position == 4){
            next.setText("Submit");
        }
        if (position == 5) {
//            next.setVisibility(View.INVISIBLE);
            storeInDatabase();
            nextActivity();
        } else {
            next.setText("Next");
            count = 0;
            next.setVisibility(View.VISIBLE);
            playAnime(question, 0, question_array[position]);
        }
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

    private void storeanswer(Button selectedoption, int index) {
//        selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3BB9FF")));
        selectedButton = selectedoption;
        colorSelectedButton(selectedButton);
        enableoption(false);
        next.setEnabled(true);
        next.setAlpha(1);
        surveydetails.add(position,String.valueOf(index));
    }

    private void colorSelectedButton(Button selectedoption) {
        selectedoption.setBackground(getDrawable(R.drawable.rectangle_selected_border));
        selectedoption.setTextColor(getResources().getColor(R.color.colorAccent));
    }
    private void discolorSelectedButton(Button selectedoption) {
        selectedoption.setBackground(getDrawable(R.drawable.rectangle_border));
        selectedoption.setTextColor(Color.parseColor("#999999"));
    }


    private void enableoption(boolean enable) {
        for (int i = 0; i < 3; i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);
//            if (enable) {
//                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
//            }
        }
    }

    private void storeInDatabase() {
        surveyid = myRef.child("SurveyHistory").push().getKey();
        testhistoryid = myRef.child("TestHistory").push().getKey();
        String uid = getIntent().getStringExtra("userid");
        String email = getIntent().getStringExtra("useremail").split("@")[0];
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Log.d("User Email",email);
        int level;
        switch (surveydetails.get(0)){
            case "0": level=1;
                break;
            case "1": level = 3;
                break;
            case "2": level = 5;
                break;
            default:level=1;
        }
        UserModel userModel = new UserModel(uid,username,surveyid,testhistoryid,0,0,level);
        myRef.child("Users").child(uid).setValue(userModel);
        SurveyModel surveyModel = new SurveyModel(uid,surveydetails);
        myRef.child("SurveyHistory").child(surveyid).setValue(surveyModel);
        Toast.makeText(SurveyActivity.this, "Response Recorded Successfully", Toast.LENGTH_SHORT).show();
    }
    private void nextActivity() {
        Intent dashboardIntent = new Intent(SurveyActivity.this, DashboardActivity.class);
        startActivity(dashboardIntent);
        finish();
        return;
    }

}