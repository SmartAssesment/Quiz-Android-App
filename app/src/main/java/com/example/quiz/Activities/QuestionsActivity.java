package com.example.quiz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.Models.QuestionDetails;
import com.example.quiz.Models.QuestionModel;
import com.example.quiz.Models.StaticQueue;
import com.example.quiz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.view.View.INVISIBLE;

public class QuestionsActivity extends AppCompatActivity {


    public static final String FILE_NAME = "Quiz";
    public static final String KEY_NAME = "Questions";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private TextView question, noIndicator, lvlIndicator, timer;
    private FloatingActionButton bookmarkBtn;
    private LinearLayout optionsContainer;
    private Button nextBtn, skipBtn, endBtn;
    private int count = 0, position = 0, skip_count = 0,
            answered = 0, score = 0, correct_count = 0,
            matchedQuestionPosition, randomQuestion,
            questionCounter = 0;
    private List<QuestionModel> qlist;
    private String courseID;
    private Dialog loadingdialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private List<QuestionModel> bookmarkslist;
    private boolean lastresponse = false;
    private StaticQueue sq = new StaticQueue(3);
    private String level = "1";

    //    Tensorflow Interpreter
    private Interpreter tflite;
    //    Already Downloaded QuestionDetails
    Map<String, List<QuestionDetails>> downQuestionDetails = new HashMap<>();
    //    Already Used Random Question
    Map<String, List<Integer>> usedRandomList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

//        First we must create an object of Interpreter
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
//        loadingdialog = new Dialog(this);
//        loadingdialog.setContentView(R.layout.loading);
//        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
//        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        loadingdialog.setCancelable(false);

        qlist = new ArrayList<>();

        courseID = getIntent().getStringExtra("CourseID");

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelmatch()) {
                    bookmarkslist.remove(matchedQuestionPosition);
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                } else {
                    bookmarkslist.add(qlist.get(position));
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                }
            }
        });

        // Start Loading Dialog
//        loadingdialog.show();

//        Load the Question
        loadQuestionsDetailList(level);


        // Getting List of Question Details
//        while (questionCounter < 10) {
//            myRef.child("Test").child(courseID).child("Levels").child(level).child("ques").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        questionDetails.add(snapshot.getValue(QuestionDetails.class));
//                    }
//
//                    // Loop for iterating through list of questions
//                    randomQuestion = randomNumberGenerator(questionDetails.size());
//                    String tid = String.valueOf(questionDetails.get(randomQuestion).getTypeId());
//                    myRef.child("Types").child(tid).child(questionDetails.get(randomQuestion).getqID()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            // Getting Question Details
//                            QuestionModel questionModel = dataSnapshot.getValue(QuestionModel.class);
//                            list.add(questionModel);
//
//                            if (list.size() > 0) {
//                                for (int i = 0; i < 4; i++) {
//                                    optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            checkAnswer((Button) v);
//                                        }
//                                    });
//                                }

//
//                                // Animating Question Loading
//                                playAnime(question, 0, list.get(position).getQues());
//
//                                nextBtn.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        nextBtn.setEnabled(false);
//                                        nextBtn.setAlpha(0.7f);
//                                        enableoption(true);
//                                        position++;
//                                        if (position == list.size()) {
//                                            nextActivity();
//                                        }
//                                        count = 0;
//                                        Log.d("Size of List:", String.valueOf(list.size()));
//                                        playAnime(question, 0, list.get(position).getQues());
//                                    }
//                                });
//
//                                skipBtn.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        enableoption(true);
//                                        position++;
//                                        if (position == list.size()) {
//                                            nextActivity();
//                                        }
//                                        count = 0;
//                                        playAnime(question, 0, list.get(position).getQues());
//                                    }
//                                });
//
//                                endBtn.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        nextActivity();
//                                    }
//                                });
//                            } else {
//                                finish();
//                                Toast.makeText(QuestionsActivity.this, "No Questions Available", Toast.LENGTH_SHORT).show();
//                                loadingdialog.dismiss();
//                                finish();
//                            }
//                            loadingdialog.dismiss();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            // Increasing question Counter as max question should be 10
//            questionCounter++;
//
//            // Generating random level. After adding TensorFlow Lite it will decide next level
////            float output[] = getInputValues();
////            int result = doInference(output);
////            level = String.valueOf(result);
//            Log.d("New Level",level);
//            Log.d("Question Counter", String.valueOf(questionCounter));
//            // If randomNumberGenerator generate generate 0 then again generate a random number as level 0 is not available
//            if(level.equals("0")){
//                level = String.valueOf(randomNumberGenerator(11));
//            }
//        }

        // Timer For Test
        CountDownTimer countDownTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //  ((millisUntilFinished / 1000)/60) + ":" + ((millisUntilFinished / 1000) % 60)
                timer.setText(getString(R.string.time) + " " + ((millisUntilFinished / 1000) / 60) + ":" + ((millisUntilFinished / 1000) % 60));
            }

            @Override
            public void onFinish() {
                nextActivity();
            }
        }.start();

    }

    //    Return the List of QuestionDetails from Firebase
    private void loadQuestionsDetailList(final String level) {
        final List<QuestionDetails> questionDetailsList = new ArrayList<>();
        myRef.child("Test").child(courseID).child("Levels").child(level).child("ques").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    questionDetailsList.add(snapshot.getValue(QuestionDetails.class));
                }
                downQuestionDetails.put(level, questionDetailsList);
                //generate random index using Math.random method
                int randomindex = getRandomIndex(level, questionDetailsList);
                Log.d("Random Index", String.valueOf(randomindex));
//              So Now we have a random Index which was not used for this level, Used this to get the new Index
                loadQuestion(randomindex, questionDetailsList);
//                saveData(questionDetailsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestion(int randomindex, List<QuestionDetails> questionDetailsList) {
        String tid = String.valueOf(questionDetailsList.get(randomindex).getTypeId());
        String qid = questionDetailsList.get(randomindex).getqID();
        myRef.child("Types").child(tid).child(qid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Getting Question Details
                QuestionModel question = dataSnapshot.getValue(QuestionModel.class);
                if (question == null) {
                    finish();
                    Toast.makeText(QuestionsActivity.this, "No Questions Available", Toast.LENGTH_SHORT).show();
                    loadingdialog.dismiss();
                    finish();
                }
                qlist.add(question);
                displayQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayQuestion() {
        // Animating Question Loading
        Log.d("Position", String.valueOf(position));
        playAnime(question, 0, qlist.get(position).getQues());
//        Handle Listner For Next Button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextButtonClick();
            }
        });
//        Handle Listner for Skip Question
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSkipButtonClick();
            }
        });
//        Handle for Exit Button
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });

        setOptionsListner();

    }

    private void handleSkipButtonClick() {
        enableoption(true);
        position++;
        count = 0;

        List<QuestionDetails> questionDetailsList = downQuestionDetails.get(level);
        int randomindex = getRandomIndex(level,questionDetailsList);
        loadQuestion(randomindex,questionDetailsList);

    }

    private void handleNextButtonClick() {
        nextBtn.setEnabled(false);
        nextBtn.setAlpha(0.7f);
        enableoption(true);
        position = position+1;
        count = 0;

//      Generating random level. After adding TensorFlow Lite it will decide next level
        float inputValues[] = getInputValues();
        Log.d("Input Values 1", String.valueOf(inputValues[0]));
        Log.d("Input Values 2", String.valueOf(inputValues[1]));
        Log.d("Input Values 3", String.valueOf(inputValues[2]));
        int result = doInference(inputValues);
        String templevel = level;
        level = String.valueOf(result);
//        check if level changes
        if(level !=templevel){
//            We need to flush the queue;
            sq.queueDequeue();
            sq.queueDequeue();
            sq.queueDequeue();
        }
        Log.d("Level",level);
//        Check if we have this level data
        if(downQuestionDetails.containsKey(level)){
            Log.d("TAG","Already have the data downloded");
//            If they already downloaded the data retrieve it and get a new random question
            List<QuestionDetails> questionDetailsList = downQuestionDetails.get(level);
            int randomindex = getRandomIndex(level,questionDetailsList);
            Log.d("New Random Index", String.valueOf(randomindex));
            loadQuestion(randomindex,questionDetailsList);
        }
        else{
//            if this level has not been downloaded
            loadQuestionsDetailList(level);
        }
    }

    private void setOptionsListner() {
        for (int i = 0; i < 4; i++) {
            optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer((Button) v);
                }
            });
        }
    }

    private int getRandomIndex(String level, List<QuestionDetails> questionDetailsList) {
        int randomIndex = (int) (Math.random() * questionDetailsList.size());
        Log.d("Calculated Random", String.valueOf(randomIndex));
        List<Integer> randomIndexList = usedRandomList.get(level);

        if (randomIndexList == null) {
            Log.d("Random List is"," Null");
//          initiallise the list with first value;
            randomIndexList = new ArrayList<>();
            randomIndexList.add(randomIndex);
            usedRandomList.put(level, randomIndexList);
        } else {
            Log.d("Random List is"," Not Null");
//          It contains element, So check if the randomindex produce already contain in the list if not then produce the new number
            if(randomIndexList.contains(randomIndex)) {
//                        Get a new Random number
                int newIndex = (int) (Math.random() * questionDetailsList.size());
                while(true){
                    if(newIndex != randomIndex)
                        break;
                    newIndex = (int) (Math.random() * questionDetailsList.size());
                    Log.d("New Index Calculated:", String.valueOf(newIndex));
                }
                randomIndex = newIndex;
            }
//                    We get the new random number which doesnot contain in the List so we add it
            randomIndexList.add(randomIndex);
            usedRandomList.put(level, randomIndexList);  //rewrite the arraylist again
        }
        return randomIndex;
    }

    private void saveData(List<QuestionDetails> questionDetailsList) {
        Iterator<QuestionDetails> questionDetailsIterator = questionDetailsList.iterator();
        while (questionDetailsIterator.hasNext()) {
            Log.d("QID:", questionDetailsIterator.next().getqID());
        }
        Log.d("Size", String.valueOf(questionDetailsList.size()));

    }

    private float[] getInputValues() {
        float[] input = new float[3];
        input[0] = Float.parseFloat(level);
        int lres = lastresponse ? 1 : 0;
        input[1] = lres;
        input[2] = sq.getCount();
        return input;
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    // For applying Animation for loading of question
    private void playAnime(final View view, final int value, final String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // For indicating level of question
                lvlIndicator.setText(getString(R.string.level) + qlist.get(position).getLevel());

                // Getting each option from Question List Model
                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {
                        option = qlist.get(position).getOptions().get(0);
                    } else if (count == 1) {
                        option = qlist.get(position).getOptions().get(1);
                    } else if (count == 2) {
                        if (qlist.get(position).getOptions().size() >= 3) {
                            optionsContainer.getChildAt(count).setVisibility(View.VISIBLE);
                            option = qlist.get(position).getOptions().get(2);
                        } else {
                            optionsContainer.getChildAt(count).setVisibility(INVISIBLE);
                        }
                    } else if (count == 3) {
                        if (qlist.get(position).getOptions().size() == 4) {
                            optionsContainer.getChildAt(count).setVisibility(View.VISIBLE);
                            option = qlist.get(position).getOptions().get(3);
                        } else {
                            optionsContainer.getChildAt(count).setVisibility(INVISIBLE);
                        }
                    }
                    playAnime(optionsContainer.getChildAt(count), 0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //data change
                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        noIndicator.setText(position + 1 + "/" + qlist.size());
                        if (modelmatch()) {
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                        } else {
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                        }
                    } catch (ClassCastException e) {
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

    // For checking answer and adding Score
    private void checkAnswer(Button selectedoption) {
        answered++;
        enableoption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        int pos = Integer.parseInt(qlist.get(position).getCorrect()) - 1;
        int lvl = Integer.parseInt(qlist.get(position).getLevel());
        if (selectedoption.getText().toString().equals(qlist.get(position).getOptions().get(pos))) {
            //correct Answer add Score
            lastresponse = true;
            switch (lvl) {
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
        } else {
            lastresponse = false;
            //incorrect answer
            selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
            Button correctoption = (Button) optionsContainer.findViewWithTag(qlist.get(position).getOptions().get(pos));
            correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
        }
//        Last Response will be pushed into the queue;
        sq.queueEnqueue(lastresponse);
    }

    // Enabling option for next question
    private void enableoption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

    // Moving to next activity
    private void nextActivity() {
        //score Activity
        skip_count = qlist.size() - answered;
        Intent scoreIntent = new Intent(QuestionsActivity.this, ScoreActivity.class);
        scoreIntent.putExtra("score", score);
        //scoreIntent.putExtra("total", list.size());
        scoreIntent.putExtra("correct_count", correct_count);
        scoreIntent.putExtra("skip_count", skip_count);
        startActivity(scoreIntent);
        finish();
        return;
    }

    // Getting Random number
    private int randomNumberGenerator(int limit) {
        int random = -1;
        Random rand = new Random();
        random = rand.nextInt(limit);
        return random;
    }

    private void getBookmarks() {
        String json = preferences.getString(KEY_NAME, "");
        Type type = new TypeToken<List<QuestionModel>>() {
        }.getType();
        bookmarkslist = gson.fromJson(json, type);
        if (bookmarkslist == null) {
            bookmarkslist = new ArrayList<>();
        }
    }

    private boolean modelmatch() {
        boolean matched = false;
        int i = 0;
        int pos = Integer.parseInt(qlist.get(position).getCorrect()) - 1;
        for (QuestionModel model : bookmarkslist) {
            if (model.getQues().equals(qlist.get(position).getQues())
                    && model.getOptions().get(Integer.parseInt(model.getCorrect())).equals(qlist.get(position).getOptions().get(pos))) {
                matched = true;
                matchedQuestionPosition = i;
            }
            i++;
        }
        return matched;
    }

    private void storeBookmarks() {
        String json = gson.toJson(bookmarkslist);
        editor.putString(KEY_NAME, json);
        editor.commit();
    }

    //    Memory Map the file in the buffer
    private MappedByteBuffer loadModelFile() throws Exception {
//        Open the file
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("litemodel.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private int doInference(float[] inputvalues) {
        float output[][] = new float[1][11];
        tflite.run(inputvalues, output);
        int inferredval = findclassIndex(output);
        return inferredval;
    }

    private int findclassIndex(float[][] output) {
        float a[] = output[0];

        float max = a[0];
        int index = 0;

        for (int i = 0; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
                index = i;
            }
        }
        return index;
    }

}
