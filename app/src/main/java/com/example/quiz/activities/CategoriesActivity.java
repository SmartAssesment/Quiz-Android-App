package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.quiz.adapters.CoursesAdaptor;
import com.example.quiz.R;
import com.example.quiz.models.CourseModel;
import com.example.quiz.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Dialog loadingdialog;
    private View decorView;

    // Firebase reference initialization
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Utils.darkenStatusBar(this,R.color.colorPurple);
        View decorView = getWindow().getDecorView(); //set status background black
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //set status text  light

        // Setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Configuring recyclerview
        recyclerView = findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

//        final List<CategoryModel> list = new ArrayList<>();
         final List<CourseModel> courseModelList = new ArrayList<>();
         courseModelList.add(new CourseModel("C1",this.getResources().getDrawable(R.drawable.icon_python),"Python"));
         courseModelList.add(new CourseModel("C2",this.getResources().getDrawable(R.drawable.icon_java),"Java"));
//        list.add()
//               Drawable s =  this.getResources().getDrawable(R.drawable.icon_python);

        // Adapter to Store and Display Category Details
//        final CategoryAdapter adapter = new CategoryAdapter(list);
        final CoursesAdaptor adapter = new CoursesAdaptor(courseModelList);
        recyclerView.setAdapter(adapter);

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == android.R.id.home){
                startActivity(new Intent(CategoriesActivity.this,DashboardActivity.class));
                finish();
            }
        return super.onOptionsItemSelected(item);
    }
}
