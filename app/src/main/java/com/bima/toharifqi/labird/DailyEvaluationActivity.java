package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.VidAdapter;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDonePic;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class DailyEvaluationActivity extends AppCompatActivity implements IFirebaseLoadDonePic {
    private RecyclerView vidRecyclerView;
    private VidAdapter vidAdapter;
    private TextView questionTv1, questionTv2, questionTv3, questionTv4, questionTv5, evaluationDateTv;
    private TextInputLayout answer1, answer2, answer3, answer4, answer5;

    AlertDialog dialog;

    IFirebaseLoadDonePic iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<String> vidList = new ArrayList<>();
            for (DataSnapshot vidSnapshot:dataSnapshot.getChildren())
                vidList.add(vidSnapshot.getValue().toString());

            iFirebaseLoadDone.onFirebaseLoadSuccess(vidList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    DatabaseReference vidList, questionList, userAnswerList;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_evaluation);

        // Action Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        evaluationDateTv = findViewById(R.id.evaluation_date);

        //question and answer textviews
        questionTv1 = findViewById(R.id.question1);
        questionTv2 = findViewById(R.id.question2);
        questionTv3 = findViewById(R.id.question3);
        questionTv4 = findViewById(R.id.question4);
        questionTv5 = findViewById(R.id.question5);

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        answer5 = findViewById(R.id.answer5);

        vidList = FirebaseDatabase.getInstance().getReference("dailyEvaluation").child("vidList");
        questionList = FirebaseDatabase.getInstance().getReference("dailyEvaluation");
        userAnswerList = FirebaseDatabase.getInstance().getReference("userAnswer").child(GlobalValueUser.userName);

        //get data for questions
        questionList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String question1 = String.valueOf(dataSnapshot.child("question1").getValue());
                String question2 = String.valueOf(dataSnapshot.child("question2").getValue());
                String question3 = String.valueOf(dataSnapshot.child("question3").getValue());
                String question4 = String.valueOf(dataSnapshot.child("question4").getValue());
                String question5 = String.valueOf(dataSnapshot.child("question5").getValue());
                String evaluationDate = String.valueOf(dataSnapshot.child("date").getValue());

                questionTv1.setText(question1);
                questionTv2.setText(question2);
                questionTv3.setText(question3);
                questionTv4.setText(question4);
                questionTv5.setText(question5);
                evaluationDateTv.setText(evaluationDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(DailyEvaluationActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        iFirebaseLoadDone = this;
        loadVid();

        vidRecyclerView = findViewById(R.id.vidRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DailyEvaluationActivity.this, LinearLayoutManager.HORIZONTAL, false);
        vidRecyclerView.setLayoutManager(layoutManager);
        vidRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private boolean validateAnswer1(){
        String val = answer1.getEditText().getText().toString();
        if (val.isEmpty()){
            answer1.setError("jawaban tidak boleh kosong!");
            return false;
        }else {
            answer1.setError(null);
            answer1.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateAnswer2(){
        String val = answer2.getEditText().getText().toString();
        if (val.isEmpty()){
            answer2.setError("jawaban tidak boleh kosong!");
            return false;
        }else {
            answer2.setError(null);
            answer2.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateAnswer3(){
        String val = answer3.getEditText().getText().toString();
        if (val.isEmpty()){
            answer3.setError("jawaban tidak boleh kosong!");
            return false;
        }else {
            answer3.setError(null);
            answer3.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateAnswer4(){
        String val = answer4.getEditText().getText().toString();
        if (val.isEmpty()){
            answer4.setError("jawaban tidak boleh kosong!");
            return false;
        }else {
            answer4.setError(null);
            answer4.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateAnswer5(){
        String val = answer5.getEditText().getText().toString();
        if (val.isEmpty()){
            answer5.setError("jawaban tidak boleh kosong!");
            return false;
        }else {
            answer5.setError(null);
            answer5.setErrorEnabled(false);
            return true;
        }
    }

    //this method is for submitting the answers
    public void submitAnswer(View view){
        if (!validateAnswer1() | !validateAnswer2() | !validateAnswer3() | !validateAnswer4() | !validateAnswer5()){
            return;
        }

        BottomSheetDialog bottomDialog = new BottomSheetDialog(DailyEvaluationActivity.this, R.style.BottomSheetDialogTheme);
        bottomDialog.setContentView(R.layout.bottom_dialog);
        bottomDialog.setCanceledOnTouchOutside(false);

        //customize the views from bottom dialog
        Button okButton = bottomDialog.findViewById(R.id.okButton);
        TextView mainText = bottomDialog.findViewById(R.id.firstTextDialog);
        TextView mainText2 = bottomDialog.findViewById(R.id.secondTextDialog);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DailyEvaluationActivity.this, HomeActivity.class));
            }
        });

        mainText.setText("Anda Telah Mengumpulkan Evaluasi Harian Hari ini!");
        mainText2.setText("Tekan tombol MENGERTI untuk kembali ke beranda.");
        mainText2.setVisibility(View.VISIBLE);

        String answer1Val = answer1.getEditText().getText().toString();
        String answer2Val = answer2.getEditText().getText().toString();
        String answer3Val = answer3.getEditText().getText().toString();
        String answer4Val = answer4.getEditText().getText().toString();
        String answer5Val = answer5.getEditText().getText().toString();
        String dateString = evaluationDateTv.getText().toString();

        userAnswerList.child("dailyEvaluationAnswer").child(dateString).child("date").setValue(dateString);
        userAnswerList.child("dailyEvaluationAnswer").child(dateString).child("answer1").setValue(answer1Val);
        userAnswerList.child("dailyEvaluationAnswer").child(dateString).child("answer2").setValue(answer2Val);
        userAnswerList.child("dailyEvaluationAnswer").child(dateString).child("answer3").setValue(answer3Val);
        userAnswerList.child("dailyEvaluationAnswer").child(dateString).child("answer4").setValue(answer4Val);
        userAnswerList.child("dailyEvaluationAnswer").child(dateString).child("answer5").setValue(answer5Val);

        bottomDialog.show();
    }

    private void loadVid() {
        dialog.show();
        vidList.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onFirebaseLoadSuccess(List<String> picList) {
        vidAdapter = new VidAdapter(picList, DailyEvaluationActivity.this, DailyEvaluationActivity.this.getLifecycle());
        vidRecyclerView.setAdapter(vidAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(DailyEvaluationActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        vidList.removeEventListener(valueEventListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        vidList.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        vidList.removeEventListener(valueEventListener);
        super.onStop();
    }
}
