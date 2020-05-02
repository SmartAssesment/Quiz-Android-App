package com.example.quiz.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.quiz.R;
import com.example.quiz.models.SurveyModel;
import com.example.quiz.models.UserModel;
import com.example.quiz.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;


public class DashboardActivity extends AppCompatActivity{

    private static final String TAG = "DashboardActivity";
    private View decorView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth firebaseAuth;
    private CardView quiztest,learningpath,testhistory,userprofile,logout;
    private CircularImageView userimg;
    FirebaseUser fuser;
    private TextView username,level_indicator,exppoints;
    Dialog loadingdialog;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();
//        // For Full Experince
//        decorView = getWindow().getDecorView();
//        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                if(visibility == 0){
//                    decorView.setSystemUiVisibility(hideNavigationBar());
//                }
//            }
//        });

//        Utils.darkenStatusBar(this,R.color.colorAccent);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(DashboardActivity.this,R.color.white));// set status background white

        // Initializing and formatting Loading Dialog Box
        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        // Start Loading Dialog
        loadingdialog.show();

        fetchUserData();

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(DashboardActivity.this,LoginActivity.class));

                }
            }
        });


//        Initialise
        username = findViewById(R.id.username);
        userimg = findViewById(R.id.userimg);
        level_indicator = findViewById(R.id.leveltextview);
        exppoints = findViewById(R.id.exppoint);
        quiztest = findViewById(R.id.quiztest);
        learningpath = findViewById(R.id.learningpathcard);
        testhistory = findViewById(R.id.testhist);
        userprofile = findViewById(R.id.userprofile);
        logout = findViewById(R.id.logoutcard);




        quiztest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Quiz Test Clicked");
                startActivity(new Intent(DashboardActivity.this,CategoriesActivity.class));

            }
        });

        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DashboardActivity.this,UserProfileActivity.class));

            }
        });

        learningpath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent learningpath = new Intent(DashboardActivity.this,LearningPathActivity.class);
                learningpath.putExtra("testhistoryId",userModel.getUtesthistId());
                learningpath.putExtra("testcount",userModel.getUtestcount());
                startActivity(learningpath);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();
            }
        });

        testhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testHistoryIntent = new Intent(DashboardActivity.this,TestReviewActivity.class);
                testHistoryIntent.putExtra("testhistid",userModel.getUtesthistId());
                startActivity(testHistoryIntent);
                finish();
            }
        });


//            username.setText(user.getDisplayName());
//            Glide.with(dp.getContext()).load(user.getPhotoUrl()).into(dp);
    }

    private void fetchUserData() {
        fuser = firebaseAuth.getCurrentUser();
        String uid = fuser.getUid();
        myRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);
                displayUserInfo();
                loadingdialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayUserInfo() {
        Glide.with(userimg.getContext()).load(fuser.getPhotoUrl()).into(userimg);
        username.setText(userModel.getUname());
        level_indicator.setText(""+userModel.getUlevel());
        exppoints.setText(""+userModel.getUexppoint());
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if(hasFocus){
//            decorView.setSystemUiVisibility(hideNavigationBar());
//        }
//    }

    //For Hiding Navigation Bar and Status Bar
    private int hideNavigationBar(){
        return  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

}
