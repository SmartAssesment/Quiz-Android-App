package com.example.quiz.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quiz.R;
import com.example.quiz.activities.QuestionsActivity;
import com.example.quiz.models.RecommendationItemModel;
import com.example.quiz.models.TestHistoryModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class RecommendationAdaptor extends RecyclerView.Adapter<RecommendationAdaptor.Viewholder> {
    private List<RecommendationItemModel> recommendationItemModelList ;

    public RecommendationAdaptor(List<RecommendationItemModel> recommedationItemModelList) {
        this.recommendationItemModelList = recommedationItemModelList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
// Adding Content in view
        holder.setData(recommendationItemModelList.get(position).getSubtopic_title(),recommendationItemModelList.get(position).getArticleLink(),recommendationItemModelList.get(position).getVideoLink());
    }

    @Override
    public int getItemCount() {
        return recommendationItemModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private TextView title;
        private Button articlebtn;
        private Button videobtn;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subtopictitle);
            articlebtn = itemView.findViewById(R.id.articlebtn);
            videobtn = itemView.findViewById(R.id.videobtn);
        }

        public void setData(String subtopic_title, final String articleLink, final String videoLink) {
            this.title.setText(subtopic_title);
            this.articlebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(articleLink)));
                }
            });

            this.videobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink)));
                }
            });
        }
    }
}
