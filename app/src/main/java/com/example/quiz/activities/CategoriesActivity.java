package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quiz.adapters.CategoryAdapter;
import com.example.quiz.adapters.CoursesAdaptor;
import com.example.quiz.models.CategoryModel;
import com.example.quiz.R;
import com.example.quiz.models.CourseModel;
import com.example.quiz.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

//        // Initializing and formatting Loading Dialog Box
//        loadingdialog = new Dialog(this);
//        loadingdialog.setContentView(R.layout.loading);
//        loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
//        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        loadingdialog.setCancelable(false);

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

        //Start Loading Dialog
//        loadingdialog.show();
//
//        // Retrieving Category Details into Adapter
//        myRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    list.add(dataSnapshot1.getValue(CategoryModel.class));
//                }
//                adapter.notifyDataSetChanged();
//                // Stop Loading Dialog once data in loaded in RecyclerView
//                loadingdialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Display Error Occured
//                Toast.makeText(CategoriesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                loadingdialog.dismiss();
//                finish();
//            }
//        });
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
