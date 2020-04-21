package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mauth;
    private FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user = mauth.getCurrentUser();
        chooseIntent(user);
    }

    private void chooseIntent(FirebaseUser user) {
        if(user!=null){
            startActivity(new Intent(MainActivity.this,DashboardActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();
        user = mauth.getCurrentUser();
        chooseIntent(user);
    }


}
