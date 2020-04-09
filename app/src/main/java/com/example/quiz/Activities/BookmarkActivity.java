package com.example.quiz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.quiz.Adapters.BookmarkAdapter;
import com.example.quiz.Models.QuestionModel;
import com.example.quiz.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.quiz.Activities.QuestionsActivity.FILE_NAME;
import static com.example.quiz.Activities.QuestionsActivity.KEY_NAME;

public class BookmarkActivity extends AppCompatActivity {

        private RecyclerView  recyclerView;
        private SharedPreferences preferences;
        private SharedPreferences.Editor editor;
        private Gson gson;
        private List<QuestionModel> bookmarkslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv_bookmarks);
        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        getBookmarks();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        BookmarkAdapter adapter = new BookmarkAdapter(bookmarkslist);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME,"");
        Type type = new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarkslist = gson.fromJson(json,type);
        if(bookmarkslist == null){
            bookmarkslist = new ArrayList<>();
        }
    }


    private void storeBookmarks(){
        String json = gson.toJson(bookmarkslist);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }
}
