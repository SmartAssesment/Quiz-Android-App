package com.example.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.LinearLayout;

import com.example.quiz.R;
import com.example.quiz.adapters.TestAnswersheetAdapter;
import com.example.quiz.models.QuestionModel;
import com.example.quiz.models.TestListModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TestAnswersheetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private Dialog loadingdialog;
    private CountDownTimer mCountDownTimer;
    List<TestListModel> testListModels = new ArrayList<>();
    List<QuestionModel> questionModels = new ArrayList<>();
    TestAnswersheetAdapter adapter = new TestAnswersheetAdapter(testListModels,questionModels);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_answersheet);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Answersheet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        recyclerView = findViewById(R.id.answerSheetrv);
        recyclerView = findViewById(R.id.testhistrv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
