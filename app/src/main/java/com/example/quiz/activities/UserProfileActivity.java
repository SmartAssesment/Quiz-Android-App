package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private CircularImageView userimage;
    private TextView username,useremail,option1_text,option2_text,option3_text,option4_text,option5_text;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private UserModel userModel;
    private Dialog loadingdialog;
    private List<String> surveyresponses;
    private List<String> options1,options2,options3,options4,options5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Utils.darkenStatusBar(this,R.color.userprofile);
        View decorView = getWindow().getDecorView(); //set status background black
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //set status text  light

        // Setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing and formatting Loading Dialog Box
        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

//        Intialise
        userimage = findViewById(R.id.userimage);
        username = findViewById(R.id.username);
        useremail = findViewById(R.id.useremail);
        option1_text = findViewById(R.id.option1_text);
        option2_text = findViewById(R.id.option2_text);
        option3_text = findViewById(R.id.option3_text);
        option4_text = findViewById(R.id.option4_text);
        option5_text = findViewById(R.id.option5_text);

        options1 = new ArrayList<>();

        options1.add("0 to 1 Year");
        options1.add("1 to 5 Year");
        options1.add("More than 5 Year");

        options2 = new ArrayList<>();
        options2.add("Placements");
        options2.add("Upskill");

        options3 = new ArrayList<>();
        options3.add("Agree");
        options3.add("Not Agree");

        options4 = new ArrayList<>();
        options4.add("Researching and Analyzing");
        options4.add("Jumping into Project Head On");

        options5 = new ArrayList<>();
        options5.add("Student");
        options5.add("Working Professional");

        fetchUserData();

    }

    private void fetchUserData() {
        user = mAuth.getCurrentUser();
        String uid = user.getUid();


        myRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);
                getSurveyResponse(userModel.getUsurveyId());

                loadingdialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSurveyResponse(String usurveyId) {
        myRef.child("SurveyHistory").child(usurveyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SurveyModel surveyModel = dataSnapshot.getValue(SurveyModel.class);
                surveyresponses = surveyModel.getSurveyresponces();
                displayUserInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayUserInfo() {
        Glide.with(userimage.getContext()).load(user.getPhotoUrl()).into(userimage);
        username.setText(userModel.getUname());
        useremail.setText(user.getEmail());
        option1_text.setText(options1.get(Integer.parseInt(surveyresponses.get(0))));
        option2_text.setText(options2.get(Integer.parseInt(surveyresponses.get(1))));
        option3_text.setText(options3.get(Integer.parseInt(surveyresponses.get(2))));
        option4_text.setText(options4.get(Integer.parseInt(surveyresponses.get(3))));
        option5_text.setText(options5.get(Integer.parseInt(surveyresponses.get(4))));

    }
}
