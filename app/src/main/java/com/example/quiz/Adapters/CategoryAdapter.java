package com.example.quiz.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quiz.Models.CategoryModel;
import com.example.quiz.Activities.QuestionsActivity;
import com.example.quiz.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {

    private List<CategoryModel> categoryModelsList;

    public CategoryAdapter(List<CategoryModel> categoryModelsList) {
        this.categoryModelsList = categoryModelsList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Adding Content in view
        holder.setData(categoryModelsList.get(position).getUrl(),categoryModelsList.get(position).getName(),categoryModelsList.get(position).getCourseID());
    }

    @Override
    public int getItemCount() {
        return categoryModelsList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private CircularImageView imageView;
        private TextView title;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
        }

        private void setData(String url, final String title, final String courseID){
            // Load Image in imageview using glide library
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setIntent = new Intent(itemView.getContext(), QuestionsActivity.class);
                    // Passing CourseId to next Activity
                    setIntent.putExtra("CourseID",courseID);
                    itemView.getContext().startActivity(setIntent);
                }
            });
        }
    }
}