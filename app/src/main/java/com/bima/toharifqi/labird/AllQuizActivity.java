package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.QuizGridAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneQuiz;
import com.bima.toharifqi.labird.model.QuizModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AllQuizActivity extends AppCompatActivity implements IFirebaseLoadDoneQuiz {
    private RecyclerView quizRecyclerView;
    private QuizGridAdapter quizGridAdapter;

    AlertDialog dialog;
    IFirebaseLoadDoneQuiz iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<QuizModel> quizList = new ArrayList<>();
            for (DataSnapshot quizSnapshot:dataSnapshot.getChildren())
                quizList.add(quizSnapshot.getValue(QuizModel.class));
            iFirebaseLoadDone.onFirebaseLoadSuccessQuiz(quizList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
        }
    };

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quiz);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);

        query = FirebaseDatabase.getInstance().getReference("quiz");
        iFirebaseLoadDone = this;

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(AllQuizActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        loadQuiz();

        quizRecyclerView = findViewById(R.id.recyclerview_quiz);
        quizRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private void loadQuiz() {
        dialog.show();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFirebaseLoadSuccessQuiz(List<QuizModel> quizList) {
        quizGridAdapter = new QuizGridAdapter(AllQuizActivity.this, quizList);
        quizRecyclerView.setAdapter(quizGridAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(AllQuizActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        query.removeEventListener(valueEventListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        query.removeEventListener(valueEventListener);
        super.onStop();
    }
}
