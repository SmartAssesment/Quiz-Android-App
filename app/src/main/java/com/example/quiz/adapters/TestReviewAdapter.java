package com.example.quiz.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.models.TestHistoryModel;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TestReviewAdapter extends RecyclerView.Adapter<TestReviewAdapter.ViewHolder> {

    private List<TestHistoryModel> testHistoryModelList;

    public TestReviewAdapter(List<TestHistoryModel> testHistoryModelList) {
        this.testHistoryModelList = testHistoryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testreview_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(testHistoryModelList.get(position).getScore(),testHistoryModelList.get(position).getTimeStamp(),position);
    }

    @Override
    public int getItemCount() {
        return testHistoryModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView testno,points,timestamp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            testno = itemView.findViewById(R.id.testno);
            points = itemView.findViewById(R.id.points);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
        private void setData(final int marksObtained, final long timeStamp, final int position){
            this.testno.setText("Test Number: "+(position + 1));
            //this.points.setText(marksObtained);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String time = dateFormat.format(timeStamp);
            this.timestamp.setText("Test Given On "+time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

}