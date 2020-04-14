package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.MainActivity;
import com.example.quiz.R;
import com.example.quiz.models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private TextView tvLogin,tvForgot;
    private Button login;
    private View decorView;
    private String uemail,upassword;
    private EditText email,password;
    private Dialog loadingdialog;
    private FirebaseAuth firebaseAuth;
    private SignInButton gSignInButton;
    private int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private boolean userfound = false;
    final List<UserModel> list = new ArrayList<>();

//    @Override
//    protected void onStart() {
//        super.onStart();
//        final FirebaseUser user = firebaseAuth.getCurrentUser();
//        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String parts[] = user.getEmail().split("@");
//                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    list.add(snapshot.getValue(UserModel.class));
//                }
//                for (int i = 0; i < list.size(); i++){
//                    if(list.get(i).getUid().equals(parts[0])){
//                        userfound = true;
//                        break;
//                    }
//                }
//                if(userfound){
//                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
//                    mainIntent.putExtra("usremail",user.getEmail());
//                    startActivity(mainIntent);
//                    finish();
//                }else{
//                    Intent surveyIntent = new Intent(LoginActivity.this,SurveyActivity.class);
//                    surveyIntent.putExtra("usremail",user.getEmail());
//                    surveyIntent.putExtra("usrname",user.getDisplayName());
//                    startActivity(surveyIntent);
//                    finish();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        // Initializing and formatting Loading Dialog Box
        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        ImageButton btRegister = findViewById(R.id.btRegister);
        tvLogin = findViewById(R.id.tvLogin);
        login = findViewById(R.id.btLogin);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        tvForgot = findViewById(R.id.tvForgot);
        gSignInButton = findViewById(R.id.btGSignIn);

        firebaseAuth=FirebaseAuth.getInstance();

        createRequest();

        gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regisstationIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                Pair[]  pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(regisstationIntent,activityOptions.toBundle());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithCredentials();
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotpasswordIntent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                Pair[]  pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(forgotpasswordIntent,activityOptions.toBundle());
            }
        });


    }

    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String parts[] = user.getEmail().split("@");
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        list.add(snapshot.getValue(UserModel.class));
                                    }
                                    for (int i = 0; i < list.size(); i++){
                                        if(list.get(i).getUid().equals(parts[0])){
                                            userfound = true;
                                            break;
                                        }
                                    }
                                    if(userfound){
                                        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                                        mainIntent.putExtra("usremail",user.getEmail());
                                        startActivity(mainIntent);
                                        finish();
                                    }else{
                                        Intent surveyIntent = new Intent(LoginActivity.this,SurveyActivity.class);
                                        surveyIntent.putExtra("usremail",user.getEmail());
                                        surveyIntent.putExtra("usrname",user.getDisplayName());
                                        startActivity(surveyIntent);
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"Google SignIn Failed", Toast.LENGTH_SHORT).show();
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

    private void signInWithCredentials(){
        uemail = email.getText().toString().trim();
        upassword = password.getText().toString().trim();

        if(TextUtils.isEmpty(uemail)){
            email.setError("Email is Required");
            return;
        }
        if(TextUtils.isEmpty(upassword)){
            password.setError("Password is Required");
            return;
        }

        if(upassword.length()<8){
            password.setError("Password must be more than 8 charachter");
            return;
        }

        //Start Loading Dialog
        loadingdialog.show();
        firebaseAuth.signInWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String parts[] = uemail.split("@");
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    list.add(snapshot.getValue(UserModel.class));
                                }
                                for (int i = 0; i < list.size(); i++){
                                    if(list.get(i).getUid().equals(parts[0])){
                                        userfound = true;
                                        break;
                                    }
                                }
                                if(userfound){
                                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                                    mainIntent.putExtra("usremail",uemail);
                                    startActivity(mainIntent);
                                    finish();
                                }else{
                                    Intent surveyIntent = new Intent(LoginActivity.this,SurveyActivity.class);
                                    surveyIntent.putExtra("usremail",uemail);
                                    startActivity(surveyIntent);
                                    finish();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.this,"Please verify your email address",Toast.LENGTH_LONG).show();
                        // Stop Loading Dialog if login fails
                        loadingdialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    // Stop Loading Dialog if login fails
                    loadingdialog.dismiss();
                }
            }
        });
    }
}
