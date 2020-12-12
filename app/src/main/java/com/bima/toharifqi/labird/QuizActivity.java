package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.model.QuizModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

public class QuizActivity extends AppCompatActivity {
    Toolbar toolbar;

    private int numSoalPage = 0;
    private String cekSoalUrl, cekjawabanUrl;
    private TextView pageSoalTV, jawabA, jawabB, jawabC, jawabD, jawabE, jawabF;
    private ImageView soalPic;
    private String picUrl;
    RadioGroup rg1, rg2;
    QuizModel quizModelExtra;
    private int poinPerSoal, poin;
    private ProgressBar progressBar;

    DatabaseReference daftarSoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Action Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        //radioGroup setting
        rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
        rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);

        //get data from previous activity
        quizModelExtra = getIntent().getParcelableExtra("quizModelExtra");

        //setting poin per question
        poinPerSoal = quizModelExtra.getQuizPoin()/8;

        daftarSoal = FirebaseDatabase.getInstance().getReference("quiz").child(quizModelExtra.getQuizId()).child("quizPhase1");

        //tipycal views
        TextView quizName = findViewById(R.id.quiz_name);
        TextView quizPoin = findViewById(R.id.quiz_poin);
        jawabA = findViewById(R.id.jawabA);
        jawabB = findViewById(R.id.jawabB);
        jawabC = findViewById(R.id.jawabC);
        jawabD = findViewById(R.id.jawabD);
        jawabE = findViewById(R.id.jawabE);
        jawabF = findViewById(R.id.jawabF);
        soalPic = findViewById(R.id.soalPic);
        pageSoalTV = findViewById(R.id.textNumSoal);

        //set tipycal data
        quizName.setText("Langkah Belajar: "+quizModelExtra.getQuizName());
        quizPoin.setText("Nilai Poin: "+quizModelExtra.getQuizPoin());

        //to make image zoomable
        ZoomInImageViewAttacher imageAttacher = new ZoomInImageViewAttacher();
        imageAttacher.attachImageView(soalPic);
        imageAttacher.setZoomable(true);

        //button for answers
        jawabA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekjawabanUrl = jawabA.getText().toString();
            }
        });

        jawabB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekjawabanUrl = jawabB.getText().toString();
            }
        });

        jawabC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekjawabanUrl = jawabC.getText().toString();
            }
        });

        jawabD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekjawabanUrl = jawabD.getText().toString();
            }
        });

        jawabE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekjawabanUrl = jawabE.getText().toString();
            }
        });

        jawabF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekjawabanUrl = jawabF.getText().toString();
            }
        });

        //select soundUrl based on what page now is
        selectUrl(numSoalPage);

        progressBar = findViewById(R.id.progressbar_pic);

    }

    //untuk mengatur 2 radiogroup as one radiogroup
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                rg2.clearCheck(); // clear the second RadioGroup!
                rg2.setOnCheckedChangeListener(listener2); //reset the listener
                Log.e("XXX2", "do the work");
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null);
                rg1.clearCheck();
                rg1.setOnCheckedChangeListener(listener1);
                Log.e("XXX2", "do the work");
            }
        }
    };

    //method to select what page now is
    private void selectUrl(int numSoalPage){
        switch (numSoalPage){
            case 0:
                getUrl(0);
                break;
            case 1:
                getUrl(1);
                break;
            case 2:
                getUrl(2);
                break;
            case 3:
                getUrl(3);
                break;
        }
    }

    //to get every Url from firebase
    private void getUrl(final int Case){
        daftarSoal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                picUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soalPic").getValue());
                cekSoalUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabSoal").getValue());
                String jawabAString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabA").getValue());
                String jawabBString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabB").getValue());
                String jawabCString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabC").getValue());
                String jawabDString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabD").getValue());
                String jawabEString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabE").getValue());
                String jawabFString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabF").getValue());

                //set data for every dinamic view
                Glide.with(QuizActivity.this).load(picUrl).placeholder(R.drawable.species_placeholder).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(soalPic);
                jawabA.setText(jawabAString);
                jawabB.setText(jawabBString);
                jawabC.setText(jawabCString);
                jawabD.setText(jawabDString);
                jawabE.setText(jawabEString);
                jawabF.setText(jawabFString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //think about this!!
    public void nextSoal(View view){
        final BottomSheetDialog bottomDialog = new BottomSheetDialog(QuizActivity.this, R.style.BottomSheetDialogTheme);
        bottomDialog.setContentView(R.layout.bottom_dialog);
        bottomDialog.setCanceledOnTouchOutside(false);

        //customize the views from bottom dialog
        Button okButton = bottomDialog.findViewById(R.id.okButton);
        TextView mainText = bottomDialog.findViewById(R.id.firstTextDialog);
        ImageView mainIcon = bottomDialog.findViewById(R.id.scoreCheck);
        mainIcon.setImageResource(R.drawable.warn_icon);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.hide();
            }
        });

        if (cekjawabanUrl==null){
            mainText.setText("pilihlah salah satu jawaban terlebih dahulu!");
            bottomDialog.show();
        }else{
            if (cekSoalUrl.equals(cekjawabanUrl)){
                poin += poinPerSoal;
            }

            numSoalPage +=1;
            cekSoalUrl = null;
            cekjawabanUrl = null;

            //uncheck all radiobutton
            rg1.clearCheck();
            rg2.clearCheck();

            //select the Url based on what page now is
            selectUrl(numSoalPage);

            pageSoalTV.setText((numSoalPage+1)+"/8");
            if (numSoalPage>3){
                //think later!
                Intent intent = new Intent(QuizActivity.this, QuizActivity2.class);
                intent.putExtra("quizModelExtra", quizModelExtra);
                intent.putExtra("currentPoint", poin);
                intent.putExtra("poinPerSoal", poinPerSoal);
                startActivity(intent);
            }
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
