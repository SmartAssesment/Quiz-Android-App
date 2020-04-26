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
import com.example.quiz.adapters.TestReviewAdapter;
import com.example.quiz.models.TestHistoryModel;
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

public class TestReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private UserModel userModel;
    FirebaseUser fuser;
    private FirebaseAuth firebaseAuth;
    List<TestHistoryModel> list  = new ArrayList<>();
    TestReviewAdapter adapter = new TestReviewAdapter(list);
    private Dialog loadingdialog;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_review);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Test History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);


        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.testhistrv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fetchUserData();
        loadingdialog.show();
        mCountDownTimer = new CountDownTimer(3000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {

            }

            public void onFinish()
            {
                loadingdialog.dismiss();
                //Your action like intents are placed here
                updateAdapter();
            }
        }.start();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent(TestReviewActivity.this,DashboardActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchUserData() {
        fuser = firebaseAuth.getCurrentUser();
        String uid = fuser.getUid();
        myRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateAdapter() {
        myRef.child("TestHistory").child(userModel.getUtesthistId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(snapshot.getValue(TestHistoryModel.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TestReviewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
