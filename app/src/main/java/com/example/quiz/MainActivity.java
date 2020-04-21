package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.activities.DashboardActivity;
import com.example.quiz.activities.LoginActivity;
import com.example.quiz.activities.SurveyActivity;
import com.example.quiz.activities.SurveyDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mauth;
    private FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user = mauth.getCurrentUser();
        chooseIntent(user);
    }

    private void chooseIntent(final FirebaseUser user) {
        if(user!=null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final String userUid = user.getUid();
            ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(userUid)) {
                        // use "username" already exists
//                      User Data Already Exist So go to Dashboard
                        Intent mainIntent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        // User does not exist. NOW call createUserWithEmailAndPassword
//                    saveUserData(user);

                        // Your previous code here.
                        Intent surveyIntent = new Intent(MainActivity.this, SurveyDetailsActivity.class);
                        surveyIntent.putExtra("userid",userUid);
                        surveyIntent.putExtra("useremail",user.getEmail());
                        startActivity(surveyIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
