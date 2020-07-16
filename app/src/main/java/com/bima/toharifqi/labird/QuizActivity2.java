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
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bima.toharifqi.labird.model.QuizModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class QuizActivity2 extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    Toolbar toolbar;

    //Views for audio
    private ImageButton playBtn;
    private Button soalBtn;
    private SeekBar seekBar;
    private TextView timeRemaining;
    private MediaPlayer mediaPlayer;
    protected boolean checked = false;

    RadioGroup rg1, rg2;
    private int numSoalPage = 0;
    private String cekSoalUrl, cekjawabanUrl;
    QuizModel quizModelExtra;
    private int poinPerSoal, poin, userPoin, userLevel, targetPoin;
    private TextView pageSoalTV, jawabA, jawabB, jawabC, jawabD, jawabE, jawabF;
    private String soalUrl;

    DatabaseReference daftarSoal, dataUser;

    private int mediaLength, realTimeLength;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

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
        Intent getPointExtra = getIntent();
        poinPerSoal = getPointExtra.getIntExtra("poinPerSoal", 0);
        poin = getPointExtra.getIntExtra("currentPoint", 0);

        daftarSoal = FirebaseDatabase.getInstance().getReference("quiz").child(quizModelExtra.getQuizId()).child("quizPhase2");
        dataUser = FirebaseDatabase.getInstance().getReference("leaderBoard");
        //get value from firebase
        dataUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //userPoin = String.valueOf(dataSnapshot.child(GlobalValueUser.userName).child("poin").getValue());
                String poinText = String.valueOf(dataSnapshot.child(GlobalValueUser.userName).child("poin").getValue());
                String levelText = String.valueOf(dataSnapshot.child(GlobalValueUser.userName).child("level").getValue());

                int levelInteger = Integer.valueOf(levelText);
                targetPoin = 20+(10*(levelInteger*levelInteger));
                userLevel = Integer.valueOf(levelText);
                userPoin = Integer.valueOf(poinText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //tipycal views
        TextView quizName = findViewById(R.id.quiz_name);
        TextView quizPoin = findViewById(R.id.quiz_poin);
        jawabA = findViewById(R.id.jawabA);
        jawabB = findViewById(R.id.jawabB);
        jawabC = findViewById(R.id.jawabC);
        jawabD = findViewById(R.id.jawabD);
        jawabE = findViewById(R.id.jawabE);
        jawabF = findViewById(R.id.jawabF);
        pageSoalTV = findViewById(R.id.textNumSoal);

        //set tipycal data
        quizName.setText("Langkah Belajar: "+quizModelExtra.getQuizName());
        quizPoin.setText("Nilai Poin: "+quizModelExtra.getQuizPoin());

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

        //select soundUrl based on what page now is
        selectSoundUrl(numSoalPage);

        //button for soal
        playBtn = findViewById(R.id.playStopButton);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()&&mediaPlayer!=null){
                    mediaPlayer.start();
                    playBtn.setImageResource(R.drawable.ic_stop);
                }else {
                    mediaPlayer.pause();
                    playBtn.setImageResource(R.drawable.ic_play_arrow);
                }
                updateSeekBar();
            }
        });

        soalBtn = findViewById(R.id.soalSoundBtn);
        soalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.seekTo(0);
                checked = true;

                playSound(soalUrl);
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
    }

    //method to select what page now is
    private void selectSoundUrl(int numSoalPage){
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
                soalUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soalUrl").getValue());
                cekSoalUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabSoal").getValue());
                String jawabAString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabA").getValue());
                String jawabBString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabB").getValue());
                String jawabCString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabC").getValue());
                String jawabDString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabD").getValue());
                String jawabEString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabE").getValue());
                String jawabFString = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("jawabF").getValue());

                //set data for every dinamic view
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

    //for changing soal
    public void nextSoal(View view){
        final BottomSheetDialog bottomDialog = new BottomSheetDialog(QuizActivity2.this, R.style.BottomSheetDialogTheme);
        bottomDialog.setContentView(R.layout.bottom_dialog);
        bottomDialog.setCanceledOnTouchOutside(false);

        //customize the views from bottom dialog
        Button okButton = bottomDialog.findViewById(R.id.okButton);
        TextView mainText = bottomDialog.findViewById(R.id.firstTextDialog);
        TextView scoreText = bottomDialog.findViewById(R.id.pointTextDialog);
        ImageView mainIcon = bottomDialog.findViewById(R.id.scoreCheck);
        mainIcon.setImageResource(R.drawable.warn_icon);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.hide();
            }
        });

        if (!checked){
            mainText.setText("pastikan anda sudah mengecek suara soal terlebih dahulu!");
            bottomDialog.show();
        }else if (cekjawabanUrl==null){
            mainText.setText("pilihlah salah satu jawaban terlebih dahulu!");
            bottomDialog.show();
        }else {
            if (cekSoalUrl.equals(cekjawabanUrl)){
                poin += poinPerSoal;
            }

            checked = false;
            mediaPlayer.reset();
            mediaPlayer.seekTo(0);
            numSoalPage +=1;
            cekSoalUrl = null;
            cekjawabanUrl = null;

            //uncheck all radiobutton
            rg1.clearCheck();
            rg2.clearCheck();

            //select soundUrl based on what page now is
            selectSoundUrl(numSoalPage);

            pageSoalTV.setText((numSoalPage+5)+"/8");
            if (numSoalPage>3){
                pageSoalTV.setText("8/8");
                mainText.setText("SELAMAT ANDA MENDAPATKAN");
                scoreText.setVisibility(View.VISIBLE);
                scoreText.setText("+"+poin+" poin");
                mainIcon.setImageResource(R.drawable.score_icon);
                bottomDialog.show();
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });


                int currentPoin = userPoin+poin*(-1);


                if (currentPoin*-1>targetPoin){
                    dataUser.child(GlobalValueUser.userName).child("level").setValue(userLevel+1);
                }


                dataUser.child(GlobalValueUser.userName).child("poin").setValue(currentPoin);


            }
        }

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

    //to playsound match with the url
    private void playSound(String soundUrl){
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(QuizActivity2.this).build();

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
                    playBtn.setImageResource(R.drawable.ic_play_arrow);
                }


                updateSeekBar();
                dialog.dismiss();
            }
        };

        mp3Play.execute(soundUrl);

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
        startActivity(new Intent(QuizActivity2.this, HomeActivity.class));
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
