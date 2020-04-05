package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.models.QuestionDetails;
import com.example.quiz.models.QuestionModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.View.INVISIBLE;

public class QuestionsActivity extends AppCompatActivity {


    public static final  String FILE_NAME = "Quiz";
    public static final  String KEY_NAME  = "Questions";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private TextView question,noIndicator,lvlIndicator,timer;
    private FloatingActionButton bookmarkBtn;
    private LinearLayout optionsContainer;
    private Button nextBtn,skipBtn,endBtn;
    private int count = 0,position = 0,skip_count = 0,answered = 0,score = 0,correct_count = 0,matchedQuestionPosition,randomQuestion,questionCounter = 0;
    private List<QuestionModel> list;
    private String courseID,level = "1";
    private Dialog loadingdialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private List<QuestionModel> bookmarkslist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //Linking layout component with variables
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        optionsContainer = findViewById(R.id.options_container);

        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);
        lvlIndicator = findViewById(R.id.level_indicator);
        timer = findViewById(R.id.timer);

        bookmarkBtn = findViewById(R.id.bookmark_btn);
        nextBtn = findViewById(R.id.next_btn);
        skipBtn = findViewById(R.id.skip_btn);
        endBtn = findViewById(R.id.end_btn);


        // For storing bookmarks
        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        getBookmarks();

        // Initializing and formatting Loading Dialog Box
        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        list = new ArrayList<>();
        final List<QuestionDetails> questionDetails = new ArrayList<>();

        courseID = getIntent().getStringExtra("CourseID");

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelmatch()){
                    bookmarkslist.remove(matchedQuestionPosition);
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                }else{
                    bookmarkslist.add(list.get(position));
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                }
            }
        });

        // Start Loading Dialog
        loadingdialog.show();

        // Getting List of Question Details
        while (questionCounter < 10) {
            myRef.child("Test").child(courseID).child("Levels").child(level).child("ques").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        questionDetails.add(snapshot.getValue(QuestionDetails.class));
                    }

                    // Loop for iterating through list of questions
                    randomQuestion = randomNumberGenerator(questionDetails.size());
                    String tid = String.valueOf(questionDetails.get(randomQuestion).getTypeId());
                    myRef.child("Types").child(tid).child(questionDetails.get(randomQuestion).getqID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Getting Question Details
                            QuestionModel questionModel = dataSnapshot.getValue(QuestionModel.class);
                            list.add(questionModel);

                            if (list.size() > 0) {
                                for (int i = 0; i < 4; i++) {
                                    optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            checkAnswer((Button) v);
                                        }
                                    });
                                }

                                // Animating Question Loading
                                playAnime(question, 0, list.get(position).getQues());

                                nextBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        nextBtn.setEnabled(false);
                                        nextBtn.setAlpha(0.7f);
                                        enableoption(true);
                                        position++;
                                        if (position == 10) {
                                            nextActivity();
                                        }
                                        count = 0;
                                        playAnime(question, 0, list.get(position).getQues());
                                    }
                                });

                                skipBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        enableoption(true);
                                        position++;
                                        if (position == 10) {
                                            nextActivity();
                                        }
                                        count = 0;
                                        playAnime(question, 0, list.get(position).getQues());
                                    }
                                });

                                endBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        nextActivity();
                                    }
                                });
                            } else {
                                finish();
                                Toast.makeText(QuestionsActivity.this, "No Questions Available", Toast.LENGTH_SHORT).show();
                                loadingdialog.dismiss();
                                finish();
                            }
                            loadingdialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Increasing question Counter as max question should be 10
            questionCounter++;

            // Generating random level. After adding TensorFlow Lite it will decide next level
            level = String.valueOf(randomNumberGenerator(11));

            // If randomNumberGenerator generate generate 0 then again generate a random number as level 0 is not available
            if(level.equals("0")){
                level = String.valueOf(randomNumberGenerator(11));
            }
        }

        // Timer For Test
        CountDownTimer countDownTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //  ((millisUntilFinished / 1000)/60) + ":" + ((millisUntilFinished / 1000) % 60)
                timer.setText(getString(R.string.time) +" "+ ((millisUntilFinished / 1000)/60) + ":" + ((millisUntilFinished / 1000) % 60));
            }

            @Override
            public void onFinish() {
                nextActivity();
            }
        }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    // For applying Animation for loading of question
    private void playAnime(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // For indicating level of question
                lvlIndicator.setText(getString(R.string.level) + list.get(position).getLevel());

                // Getting each option from Question List Model
                if(value == 0 && count < 4){
                    String option = "";
                    if(count == 0){
                        option = list.get(position).getOptions().get(0);
                    }else if(count == 1){
                        option = list.get(position).getOptions().get(1);
                    }else if(count == 2){
                        if(list.get(position).getOptions().size() >= 3) {
                            optionsContainer.getChildAt(count).setVisibility(View.VISIBLE);
                            option = list.get(position).getOptions().get(2);
                        }else{
                            optionsContainer.getChildAt(count).setVisibility(INVISIBLE);
                        }
                    }else if(count == 3){
                        if(list.get(position).getOptions().size() == 4 ){
                            optionsContainer.getChildAt(count).setVisibility(View.VISIBLE);
                            option = list.get(position).getOptions().get(3);
                        }else{
                            optionsContainer.getChildAt(count).setVisibility(INVISIBLE);
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
                    try {
                        ((TextView)view).setText(data);
                        noIndicator.setText(position + 1 + "/" + list.size());
                        if(modelmatch()){
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                        }else{
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                        }
                    }catch (ClassCastException e){
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playAnime(view,1,data);
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

    // For checking answer and adding Score
    private void checkAnswer(Button selectedoption){
        answered++;
        enableoption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        int pos = Integer.parseInt(list.get(position).getCorrect()) - 1;
        int lvl = Integer.parseInt(list.get(position).getLevel());
        if(selectedoption.getText().toString().equals(list.get(position).getOptions().get(pos))){
            //correct Answer add Score
            switch(lvl){
                case 1:
                    score += 5;
                    break;
                case 2:
                    score += 10;
                    break;
                case 3:
                    score += 15;
                    break;
                case 4:
                    score += 20;
                    break;
                case 5:
                    score += 25;
                    break;
                case 6:
                    score += 30;
                    break;
                case 7:
                    score += 35;
                    break;
                case 8:
                    score += 40;
                    break;
                case 9:
                    score += 45;
                    break;
                case 10:
                    score += 50;
                    break;
            }
            correct_count++;
            selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
        }else{
            //incorrect answer
            selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
            Button correctoption = (Button) optionsContainer.findViewWithTag(list.get(position).getOptions().get(pos));
            correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
        }
    }

    // Enabling option for next question
    private void enableoption(boolean enable){
        for(int i = 0; i < 4; i++){
            optionsContainer.getChildAt(i).setEnabled(enable);
            if(enable){
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

    // Moving to next activity
    private void nextActivity(){
            //score Activity
            skip_count = list.size() - answered;
            Intent scoreIntent = new Intent(QuestionsActivity.this, ScoreActivity.class);
            scoreIntent.putExtra("score", score);
            //scoreIntent.putExtra("total", list.size());
            scoreIntent.putExtra("correct_count",correct_count);
            scoreIntent.putExtra("skip_count",skip_count);
            startActivity(scoreIntent);
            finish();
            return;
    }

    // Getting Random number
    private int randomNumberGenerator(int limit){
        int random = -1;
        Random rand = new Random();
        random = rand.nextInt(limit);
        return random;
    }

    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME,"");
        Type type = new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarkslist = gson.fromJson(json,type);
        if(bookmarkslist == null){
            bookmarkslist = new ArrayList<>();
        }
    }

    private boolean modelmatch(){
        boolean matched = false;
        int i = 0;
        int pos = Integer.parseInt(list.get(position).getCorrect()) - 1;
        for(QuestionModel model :bookmarkslist){
            if(model.getQues().equals(list.get(position).getQues())
            && model.getOptions().get(Integer.parseInt(model.getCorrect())).equals(list.get(position).getOptions().get(pos))){
                matched = true;
                matchedQuestionPosition = i;
            }
            i++;
        }
        return matched;
    }

    private void storeBookmarks(){
        String json = gson.toJson(bookmarkslist);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }

}
