package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.adapters.RecommendationAdaptor;
import com.example.quiz.models.RecommendationItemModel;
import com.example.quiz.models.TestHistoryModel;
import com.example.quiz.models.TestListModel;
import com.example.quiz.models.UserModel;
import com.example.quiz.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVReader;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LearningPathActivity extends AppCompatActivity {
    private static final String TAG = "LearningPath";
    private Dialog loadingdialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private UserModel userModel;
    FirebaseUser fuser;
    private FirebaseAuth firebaseAuth;
    List<TestHistoryModel> list = new ArrayList<>();
    Map<Integer, Integer> levelmap = new HashMap<>();
    private RecyclerView recyclerView;
    private RecommendationAdaptor adapter;
    private List<RecommendationItemModel> recommendationItemModelList;
    private List<RecommendationItemModel> allItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_path);

        Utils.darkenStatusBar(this, R.color.learningpathbg);
        View decorView = getWindow().getDecorView(); //set status background black
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //set status text  light

        // Setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Learning Path");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing and formatting Loading Dialog Box
        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);
        firebaseAuth = FirebaseAuth.getInstance();

        allItemList = new ArrayList<>();
        readAllItemfromCSV();


        recyclerView = findViewById(R.id.learningpathcardrecyler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);



        String testhistoryId = getIntent().getStringExtra("testhistoryId");
        int count = getIntent().getIntExtra("testcount", 1);
        getTestHistoryData(testhistoryId, count - 1);

    }

    private void readAllItemfromCSV() {
        try {
            InputStreamReader inputreader = new InputStreamReader(getResources().openRawResource(R.raw.pythontopics));
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            StringBuilder text = new StringBuilder();

            try {
                while ((line = buffreader.readLine()) != null) {

                    String[] token = line.split(",");
                    RecommendationItemModel itemModel = new RecommendationItemModel(token[0], token[1], Integer.parseInt(token[2]), token[3], token[4]);
                    allItemList.add(itemModel);
                }
            } catch (IOException e) {

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    private void getTestHistoryData(String testhistoryId, int count) {
        myRef.child("TestHistory").child(testhistoryId).child(String.valueOf(count)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TestHistoryModel historyModel = dataSnapshot.getValue(TestHistoryModel.class);
                List<TestListModel> responseList = historyModel.getResponses();
                updateLevelCount(responseList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LearningPathActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateLevelCount(List<TestListModel> responseList) {
        for (TestListModel response : responseList) {
            int level = Integer.parseInt(response.getQlevel());
            boolean iscorrect = response.isLastresponse();
            if (levelmap.containsKey(level)) {
                int count = levelmap.get(level);
                if (!iscorrect) {
                    levelmap.put(level, count + 1);
                }
            } else {
                if (!iscorrect) {
                    levelmap.put(level, 1);
                }
            }

        }
        printMapDetails();

    }


    private void printMapDetails() {
        levelmap = sortByValue((HashMap<Integer, Integer>) levelmap);
        recommendationItemModelList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : levelmap.entrySet()){
            int level = entry.getKey();
            int count= entry.getValue();
            for(RecommendationItemModel itemModel : allItemList){
                if(itemModel.getLevel() == level && count > 0){
                    recommendationItemModelList.add(itemModel);
                    count--;
                }
            }
        }
        adapter = new RecommendationAdaptor(recommendationItemModelList);
        recyclerView.setAdapter(adapter);
        loadingdialog.dismiss();
    }

    public static HashMap<Integer, Integer> sortByValue(HashMap<Integer, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Integer>> list =
                new LinkedList<Map.Entry<Integer, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        Collections.reverse(list);

        // put data from sorted list to hashmap
        HashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}


