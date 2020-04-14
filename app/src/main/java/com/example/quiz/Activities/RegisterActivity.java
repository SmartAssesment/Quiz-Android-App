package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.quiz.R;
import com.example.quiz.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private View decorView;
    private EditText U_Email,U_Password,U_RPassword;
    private FirebaseAuth firebaseAuth;
    private Button signup;
    private String email,password,rpassword,name;
    private Dialog loadingdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // For Animation
        RelativeLayout rlayout = findViewById(R.id.rlayout);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        U_Email = findViewById(R.id.etU_Email);
        U_Password = findViewById(R.id.etU_Password);
        U_RPassword = findViewById(R.id.etU_RPassword);
        signup = findViewById(R.id.btSignUp);
        firebaseAuth= FirebaseAuth.getInstance();

        // Initializing and formatting Loading Dialog Box
        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = U_Email.getText().toString().trim();
                password = U_Password.getText().toString().trim();
                rpassword  = U_RPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    U_Email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    U_Password.setError("Password is Required");
                    return;
                }
                if(password.length()<8){
                    U_Password.setError("Password must be more than 8 charachter");
                    return;
                }

                if(!email.contains("@")){
                    U_Email.setError("Enter a valid Email");
                    return;
                }

                //Start Loading Dialog
                loadingdialog.show();

                if(password.equals(rpassword)){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //send verification mail
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Registration Successful. Please Check your email for verification.", Toast.LENGTH_SHORT).show();
                                            Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            loginIntent.putExtra("usrname",name);
                                            startActivity(loginIntent);
                                            finish();
                                        }else {
                                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            loadingdialog.dismiss();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                loadingdialog.dismiss();
                            }

                        }
                    });
                }
            }
        });
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
