package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.QuizActivity;
import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.model.QuizModel;

import java.util.List;

public class QuizSnapAdapter extends RecyclerView.Adapter<QuizSnapAdapter.RecyclerViewHolder> {

    private List<QuizModel> quizList;
    private LayoutInflater layoutInflater;
    private Context context;

    public QuizSnapAdapter(List<QuizModel> quizList, Context context) {
        this.quizList = quizList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_quiz, parent, false);
        return new RecyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.quizName.setText(quizList.get(position).getQuizName());
        holder.quizDate.setText(quizList.get(position).getQuizDate());

        final QuizModel quizModelExtra = quizList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("quizModelExtra", quizModelExtra);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView quizName, quizDate;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            quizName = itemView.findViewById(R.id.quizName);
            quizDate = itemView.findViewById(R.id.quizDate);
        }
    }
}
