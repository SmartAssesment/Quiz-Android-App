package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quiz.R;
import com.example.quiz.models.TestHistoryModel;
import com.example.quiz.models.TestListModel;
import com.example.quiz.models.UserModel;
import com.example.quiz.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LearningPathActivity extends AppCompatActivity {
    private static final String TAG ="LearningPath" ;
    private Dialog loadingdialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private UserModel userModel;
    FirebaseUser fuser;
    private FirebaseAuth firebaseAuth;
    List<TestHistoryModel> list  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_path);

        Utils.darkenStatusBar(this,R.color.learningpathbg);
        View decorView = getWindow().getDecorView(); //set status background black
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //set status text  light

        // Setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Learning Path");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing and formatting Loading Dialog Box
        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        String testhistoryId = getIntent().getStringExtra("testhistoryId");
        int count = getIntent().getIntExtra("testcount",1);
        getTestHistoryData(testhistoryId,count-1);

    }

    private void getTestHistoryData(String testhistoryId, int count) {
        myRef.child("TestHistory").child(testhistoryId).child(String.valueOf(count)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TestHistoryModel historyModel = dataSnapshot.getValue(TestHistoryModel.class);
                List<TestListModel> responseList = historyModel.getResponses();
                updateLevelCount(responseList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LearningPathActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateLevelCount(List<TestListModel> responseList) {
        for(TestListModel response :responseList){
            Log.d(TAG,"Level:-"+response.getQlevel());
            Log.d(TAG,"Answer:-"+response.isLastresponse());
        }
    }
}
