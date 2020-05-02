package com.example.quiz.adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.activities.QuestionsActivity;
import com.example.quiz.models.CourseModel;

import java.util.List;

public class CoursesAdaptor extends RecyclerView.Adapter<CoursesAdaptor.Viewholder> {

    private List<CourseModel> courseModelsList;

    public CoursesAdaptor(List<CourseModel> courseModelsList) {
        this.courseModelsList = courseModelsList;
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
        holder.setData(courseModelsList.get(position).getId(),courseModelsList.get(position).getIcon(),courseModelsList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return courseModelsList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView title;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
        }

        private void setData(final String courseID, final Drawable icon, final String title){
            // Load Image in imageview using glide library
            this.imageView.setImageDrawable(icon);
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
