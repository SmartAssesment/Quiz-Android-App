package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quiz.R;
import com.example.quiz.adapters.TestAnswersheetAdapter;
import com.example.quiz.models.QuestionModel;
import com.example.quiz.models.TestHistoryModel;
import com.example.quiz.models.TestListModel;
import com.example.quiz.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TestAnswersheetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
//    private UserModel userModel;
//    FirebaseUser fuser;
//    private FirebaseAuth firebaseAuth;
    private Dialog loadingdialog;
    private CountDownTimer mCountDownTimer;
    List<TestListModel> testListModels = new ArrayList<>();
    List<QuestionModel> questionModels = new ArrayList<>();
    List<TestHistoryModel> testHistoryModels = new ArrayList<>();
    TestAnswersheetAdapter adapter = new TestAnswersheetAdapter(testListModels,questionModels);
//    private long time = 0;
//    private int testno = 0;

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

        //firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.answerSheetrv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //fetchUserData();
        //updateAdapter();
        loadingdialog.show();
        getResponceData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent(TestAnswersheetActivity.this,DashboardActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

//    private void fetchUserData() {
//        fuser = firebaseAuth.getCurrentUser();
//        String uid = fuser.getUid();
//        myRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userModel = dataSnapshot.getValue(UserModel.class);
//                loadingdialog.dismiss();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void updateAdapter() {
//        time = getIntent().getLongExtra("timeStamp",0);
//        myRef.child("TestHistory").child(userModel.getUtesthistId()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    testHistoryModels.add(snapshot.getValue(TestHistoryModel.class));
//                }
//                for(int i = 0; i < testHistoryModels.size(); i++){
//                    if(testHistoryModels.get(i).getTimeStamp() == time){
//                        testno = i;
//                        break;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(TestAnswersheetActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//    }

    private void getResponceData() {
        myRef.child("TestHistory").child(getIntent().getStringExtra("testhistid")).child(String.valueOf(getIntent()
                .getIntExtra("index",0))).child("responses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    testListModels.add(snapshot.getValue(TestListModel.class));
                }
                Toast.makeText(TestAnswersheetActivity.this, "Responces fetched", Toast.LENGTH_SHORT).show();
                getQuestionData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TestAnswersheetActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getQuestionData() {
        for(int i = 0; i < testHistoryModels.size(); i++){
            myRef.child("Types").child(String.valueOf(testListModels.get(i).getQtypeid())).child(testListModels.get(i).getqId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    questionModels.add(dataSnapshot.getValue(QuestionModel.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TestAnswersheetActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        loadingdialog.dismiss();
        adapter.notifyDataSetChanged();
    }
}
