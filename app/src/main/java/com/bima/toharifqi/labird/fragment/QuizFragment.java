package com.bima.toharifqi.labird.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.adapter.QuizAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneQuiz;
import com.bima.toharifqi.labird.model.QuizModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment implements IFirebaseLoadDoneQuiz {
    ViewPager viewPagerQuiz;
    QuizAdapter quizAdapter;

    IFirebaseLoadDoneQuiz iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<QuizModel> quizList = new ArrayList<>();
            for (DataSnapshot quizSnapshot:dataSnapshot.getChildren())
                quizList.add(quizSnapshot.getValue(QuizModel.class));
            iFirebaseLoadDone.onFirebaseLoadSuccess(quizList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
        }
    };

    Query query;
    View view;

    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quiz, container, false);

        query = FirebaseDatabase.getInstance().getReference("quiz").orderByChild("order");

        iFirebaseLoadDone = this;

        loadQuiz();

        viewPagerQuiz = (ViewPager) view.findViewById(R.id.quizViewPager);
        viewPagerQuiz.setPadding(20,0,280,0);
        viewPagerQuiz.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void loadQuiz() {
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onFirebaseLoadSuccess(List<QuizModel> quizList) {
        quizAdapter = new QuizAdapter(quizList, getContext());
        viewPagerQuiz.setAdapter(quizAdapter);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        query.removeEventListener(valueEventListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        query.removeEventListener(valueEventListener);
        super.onStop();
    }
}
