package com.example.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView profile,surveyResponse,takeTest,testHistory,learningPath,setting,rateUs,logOut;
    private View decorView;
    private TextView username;
    private CircularImageView dp;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // For Full Experince
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideNavigationBar());
                }
            }
        });

        // Initilizing CardViews and other Widgets
        profile = findViewById(R.id.profile);
        surveyResponse = findViewById(R.id.surveyHistory);
        takeTest = findViewById(R.id.takeTest);
        testHistory = findViewById(R.id.testHistory);
        learningPath = findViewById(R.id.learningPath);
        setting = findViewById(R.id.setting);
        rateUs = findViewById(R.id.rateUs);
        logOut = findViewById(R.id.logOut);
        username = findViewById(R.id.usrname);
        dp = findViewById(R.id.profileImage);
        firebaseAuth= FirebaseAuth.getInstance();
        final FirebaseUser user=firebaseAuth.getCurrentUser();
//        Drawable d = getDrawable(R.drawable.person);

        // Setting On ClickListner On Card Views
        profile.setOnClickListener(this);
        surveyResponse.setOnClickListener(this);
        takeTest.setOnClickListener(this);
        testHistory.setOnClickListener(this);
        learningPath.setOnClickListener(this);
        setting.setOnClickListener(this);
        rateUs.setOnClickListener(this);
        logOut.setOnClickListener(this);

//            username.setText(user.getDisplayName());
//            Glide.with(dp.getContext()).load(user.getPhotoUrl()).into(dp);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideNavigationBar());
        }
    }

    //For Hiding Navigation Bar and Status Bar
    private int hideNavigationBar(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            //case R.id.profile : break;
            //case R.id.surveyHistory : break;
            case R.id.takeTest : i = new Intent(DashboardActivity.this, CategoriesActivity.class);startActivity(i);finish();break;
            //case R.id.testHistory : break;
            //case R.id.learningPath : break;
            //case R.id.setting : break;
            //case R.id.rateUs : break;
            case R.id.logOut : i = new Intent(DashboardActivity.this, LoginActivity.class);firebaseAuth.signOut();startActivity(i);finish();break;
        }
    }
}
