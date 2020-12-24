package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.model.QuizModel;

import java.util.List;

public class QuizGridAdapter extends RecyclerView.Adapter<QuizGridAdapter.MyViewHolder> {
    private Context context;
    private List<QuizModel> quizList;

    public QuizGridAdapter(Context context, List<QuizModel> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_quiz, parent, false);
        return new QuizGridAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final QuizModel quizModelExtra = quizList.get(position);

        holder.cardView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        holder.quizName.setText(quizList.get(position).getQuizName());
        holder.quizDate.setText(quizList.get(position).getQuizDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Finish the quiz activity first dude!
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView quizName, quizDate;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            quizName = itemView.findViewById(R.id.quizName);
            quizDate = itemView.findViewById(R.id.quizDate);
            cardView = itemView.findViewById(R.id.cardview_quiz);
        }
    }
}
