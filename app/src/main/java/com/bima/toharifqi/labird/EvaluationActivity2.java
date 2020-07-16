package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.model.CourseModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class EvaluationActivity2 extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener{
    Toolbar toolbar;

    //Views for audio
    private ImageButton playBtn;
    private Button soalBtn;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;

    private int numSoalPage = 0;
    private String cekSoalUrl, cekjawabanUrl;
    private TextView timeRemaining, pageSoalTV, jawabA, jawabB, jawabC, jawabD, jawabE, jawabF;
    private ImageView soalPic;
    private String picUrl, soundUrl;
    CourseModel courseModelExtra;
    RadioGroup rg1, rg2;

    DatabaseReference daftarSoal;

    private int mediaLength, realTimeLength;

    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation2);

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
        courseModelExtra = getIntent().getParcelableExtra("courseModelExtra");

        daftarSoal = FirebaseDatabase.getInstance().getReference("course").child(courseModelExtra.getCourseId()).child("evaluasi2Url");

        //tipycal views
        TextView courseName = findViewById(R.id.courseName);
        jawabA = findViewById(R.id.jawabA);
        jawabB = findViewById(R.id.jawabB);
        jawabC = findViewById(R.id.jawabC);
        jawabD = findViewById(R.id.jawabD);
        jawabE = findViewById(R.id.jawabE);
        jawabF = findViewById(R.id.jawabF);
        soalPic = findViewById(R.id.soalPic);
        pageSoalTV = findViewById(R.id.textNumSoal);

        //set tipycal data
        courseName.setText("Langkah Belajar: "+courseModelExtra.getCourseTitle());

        //to make image zoomable
        ZoomInImageViewAttacher imageAttacher = new ZoomInImageViewAttacher();
        imageAttacher.attachImageView(soalPic);
        imageAttacher.setZoomable(true);

        //audio stuff
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(99);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mediaPlayer.isPlaying()){
                    SeekBar seekBar = (SeekBar) view;
                    int playPosition = (mediaLength/100)*seekBar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                }
                return false;
            }
        });

        timeRemaining = findViewById(R.id.textTimer);

        playBtn = findViewById(R.id.playStopButton);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new SpotsDialog.Builder().setContext(EvaluationActivity2.this).build();

                AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        dialog.setMessage("Mohon tunggu...");
                        dialog.show();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            mediaPlayer.setDataSource(strings[0]);
                            mediaPlayer.prepare();
                        }catch (Exception ex){

                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mediaLength = mediaPlayer.getDuration();
                        realTimeLength = mediaLength;
                        if(!mediaPlayer.isPlaying()){
                            mediaPlayer.start();
                            playBtn.setImageResource(R.drawable.ic_stop);
                        }else {
                            mediaPlayer.pause();
                            playBtn.setImageResource(R.drawable.ic_play_arrow);
                        }

                        updateSeekBar();
                        dialog.dismiss();
                    }
                };

                mp3Play.execute(soundUrl);
            }
        });

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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        //select soundUrl based on what page now is
        selectUrl(numSoalPage);



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
                soundUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soalSound").getValue());
                picUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soalPic").getValue());
                cekSoalUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabSoal").getValue());
                String jawabAString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabA").getValue());
                String jawabBString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabB").getValue());
                String jawabCString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabC").getValue());
                String jawabDString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabD").getValue());
                String jawabEString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabE").getValue());
                String jawabFString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabF").getValue());

                //set data for every dinamic view
                Glide.with(EvaluationActivity2.this).load(picUrl).into(soalPic);
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
        final BottomSheetDialog bottomDialog = new BottomSheetDialog(EvaluationActivity2.this, R.style.BottomSheetDialogTheme);
        bottomDialog.setContentView(R.layout.bottom_dialog);
        bottomDialog.setCanceledOnTouchOutside(false);

        //customize the views from bottom dialog
        Button okButton = bottomDialog.findViewById(R.id.okButton);
        TextView mainText = bottomDialog.findViewById(R.id.firstTextDialog);
        TextView secondText = bottomDialog.findViewById(R.id.secondTextDialog);
        ImageView mainIcon = bottomDialog.findViewById(R.id.scoreCheck);
        LinearLayout doubleBtnLayout = bottomDialog.findViewById(R.id.doubleButtonLayout);
        Button homeButton = bottomDialog.findViewById(R.id.homeBtn);
        Button resetButton = bottomDialog.findViewById(R.id.resetBtn);


        mainIcon.setImageResource(R.drawable.warn_icon);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.hide();
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EvaluationActivity2.this, HomeActivity.class));
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EvaluationActivity2.this, EvaluationActivity.class);
                intent.putExtra("courseModelExtra", courseModelExtra);
                startActivity(intent);
            }
        });

        if (cekjawabanUrl==null){
            mainText.setText("pilihlah salah satu jawaban terlebih dahulu!");
            bottomDialog.show();
        }else if (cekSoalUrl.equals(cekjawabanUrl)){
            mediaPlayer.reset();
            mediaPlayer.seekTo(0);
            Toast.makeText(this, "jawaban anda benar!", Toast.LENGTH_SHORT).show();
            numSoalPage +=1;
            cekSoalUrl = null;
            cekjawabanUrl = null;

            //uncheck all radiobutton
            rg1.clearCheck();
            rg2.clearCheck();

            //select the Url based on what page now is
            selectUrl(numSoalPage);

            pageSoalTV.setText((numSoalPage+5)+"/8");
            if (numSoalPage>3){
                secondText.setVisibility(View.VISIBLE);
                mainIcon.setImageResource(R.drawable.score_icon);
                mainText.setText("Selamat semua jawaban anda benar");
                secondText.setText("Anda tidak akan mendapatkan point di evaluasi, dapatkan di bagian kuis!");
                okButton.setVisibility(View.GONE);
                doubleBtnLayout.setVisibility(View.VISIBLE);
                bottomDialog.show();
            }
        }else {
            Toast.makeText(this, "jawaban anda salah, pastikan lagi!", Toast.LENGTH_SHORT).show();
        }

    }
    private void updateSeekBar(){
        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition() / mediaLength)*100));
        if (mediaPlayer.isPlaying()){
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                    realTimeLength-=1000;
                    timeRemaining.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realTimeLength),
                            TimeUnit.MILLISECONDS.toSeconds(realTimeLength) -
                                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realTimeLength))));
                }
            };
            handler.postDelayed(updater, 1000);
        }
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        seekBar.setSecondaryProgress(i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playBtn.setImageResource(R.drawable.ic_play_arrow);
    }
}
