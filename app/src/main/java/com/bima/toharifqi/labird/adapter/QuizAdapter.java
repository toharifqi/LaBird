package com.bima.toharifqi.labird.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.QuizActivity;
import com.bima.toharifqi.labird.model.QuizModel;

import java.util.List;

public class QuizAdapter extends PagerAdapter {
    private List<QuizModel> quizList;
    private LayoutInflater layoutInflater;
    private Context context;

    public QuizAdapter(Context context) {
        this.context = context;
    }

    public QuizAdapter(List<QuizModel> quizList, Context context) {
        this.quizList = quizList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return quizList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_quiz, container, false);

        TextView quizName, quizDate;

        quizName = view.findViewById(R.id.quizName);
        quizDate = view.findViewById(R.id.quizDate);

        quizName.setText(quizList.get(position).getQuizName());
        quizDate.setText(quizList.get(position).getQuizDate());

        final QuizModel quizModelExtra = quizList.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("quizModelExtra", quizModelExtra);

                context.startActivity(intent);
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
