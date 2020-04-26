package com.example.quiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.models.QuestionModel;
import com.example.quiz.models.TestListModel;

import java.util.List;

public class TestAnswersheetAdapter extends RecyclerView.Adapter<TestAnswersheetAdapter.ViewHolder> {
    private List<TestListModel> responses;
    private  List<QuestionModel> questionModelList;

    public TestAnswersheetAdapter(List<TestListModel> responses, List<QuestionModel> questionModelList) {
        this.responses = responses;
        this.questionModelList = questionModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testanswersheet_item,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(questionModelList.get(position).getQues(),questionModelList.get(position).getOptions(),
                questionModelList.get(position).getCorrect(),responses.get(position).getResponseindex());
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView question,option1,option2,option3,option4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.aquestion);
            option1 = itemView.findViewById(R.id.aoption1);
            option2 = itemView.findViewById(R.id.aoption2);
            option3 = itemView.findViewById(R.id.aoption3);
            option4 = itemView.findViewById(R.id.aoption4);
        }

        public void setData(String ques, List<String> options, String correct, int responseindex) {
            this.question.setText(ques);
//            if()
        }
    }
}
