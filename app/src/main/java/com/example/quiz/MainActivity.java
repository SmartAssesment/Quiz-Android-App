package com.example.quiz;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quiz.activities.DashboardActivity;
import com.example.quiz.activities.LoginActivity;
import com.example.quiz.activities.SurveyActivity;
import com.example.quiz.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN ACTIVITY";
    private static int SPLASH_TIME_OUT=2000;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    private FirebaseAuth.AuthStateListener mAuthListner;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private View decorView;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
//        decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.splashscreen));


//        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                if(visibility == 0){
//                    decorView.setSystemUiVisibility(hideNavigationBar());
//                }
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth.addAuthStateListener(mAuthListner);
                user = mAuth.getCurrentUser();

                Log.d(TAG,"User not Null");
                chooseIntent(user);
            }
        },SPLASH_TIME_OUT);




        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };


    }
    private void chooseIntent(final FirebaseUser user) {
        if(user!=null){
            Log.d(TAG,"User not Null");
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
                        // Your previous code here.
                        Intent surveyIntent = new Intent(MainActivity.this, SurveyActivity.class);
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
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListner);
    }

    //For Hiding Navigation Bar and Status Bar
    private int hideNavigationBar(){
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if(hasFocus){
//            decorView.setSystemUiVisibility(hideNavigationBar());
//        }
//    }


}
