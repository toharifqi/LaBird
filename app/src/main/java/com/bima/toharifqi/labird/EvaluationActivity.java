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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.model.CourseModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class EvaluationActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    Toolbar toolbar;

    //Views for audio
    private ImageButton playBtn;
    private Button soalBtn;
    private SeekBar seekBar;
    private TextView timeRemaining, pageSoalTV;
    private MediaPlayer mediaPlayer;

    private int numSoalPage = 0;
    private String cekSoalUrl, cekjawabanUrl;
    CourseModel courseModelExtra;
    RadioGroup rg1, rg2;
    private String soalUrl, soundAUrl, soundBUrl, soundCUrl, soundDUrl, soundEUrl, soundFUrl;

    DatabaseReference daftarSoundUrl;

    private int mediaLength, realTimeLength;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);


        //radioGroup setting
        rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
        rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);

        // Action Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        //get data from previous activity
        courseModelExtra = getIntent().getParcelableExtra("courseModelExtra");

        daftarSoundUrl = FirebaseDatabase.getInstance().getReference("course").child(courseModelExtra.getCourseId()).child("evaluasi1Url");

        //tipycal views
        TextView courseName = findViewById(R.id.courseName);
        TextView soundABtn = findViewById(R.id.soundA);
        TextView soundBBtn = findViewById(R.id.soundB);
        TextView soundCBtn = findViewById(R.id.soundC);
        TextView soundDBtn = findViewById(R.id.soundD);
        TextView soundEBtn = findViewById(R.id.soundE);
        TextView soundFBtn = findViewById(R.id.soundF);
        pageSoalTV = findViewById(R.id.textNumSoal);

        //set tipycal data
        courseName.setText("Langkah Belajar: "+courseModelExtra.getCourseTitle());

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
                cekSoalUrl = soalUrl;

                playSound(soalUrl);
            }
        });

        //button for answers
        soundABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.seekTo(0);
                cekjawabanUrl = soundAUrl;
                playSound(soundAUrl);
            }
        });
        soundBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.seekTo(0);
                cekjawabanUrl = soundBUrl;
                playSound(soundBUrl);
            }
        });
        soundCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.seekTo(0);
                cekjawabanUrl = soundCUrl;
                playSound(soundCUrl);
            }
        });
        soundDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.seekTo(0);
                cekjawabanUrl = soundDUrl;
                playSound(soundDUrl);
            }
        });
        soundEBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.seekTo(0);
                cekjawabanUrl = soundEUrl;
                playSound(soundEUrl);
            }
        });
        soundFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer.seekTo(0);
                cekjawabanUrl = soundFUrl;
                playSound(soundFUrl);
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
        daftarSoundUrl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                soalUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soal").getValue());
                soundAUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soundA").getValue());
                soundBUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soundB").getValue());
                soundCUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soundC").getValue());
                soundDUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soundD").getValue());
                soundEUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soundE").getValue());
                soundFUrl = String.valueOf(dataSnapshot.child(String.valueOf(Case)).child("soundF").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //for changing soal
    public void nextSoal(View view){
        final BottomSheetDialog bottomDialog = new BottomSheetDialog(EvaluationActivity.this, R.style.BottomSheetDialogTheme);
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

        if (cekSoalUrl==null){
            mainText.setText("pastikan anda sudah mengecek suara soal terlebih dahulu!");
            bottomDialog.show();
        }else if (cekjawabanUrl==null){
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

            //select soundUrl based on what page now is
            selectSoundUrl(numSoalPage);

            pageSoalTV.setText((numSoalPage+1)+"/8");
            if (numSoalPage>3){
                Intent intent = new Intent(EvaluationActivity.this, EvaluationActivity2.class);
                intent.putExtra("courseModelExtra", courseModelExtra);
                startActivity(intent);
            }
        }else {
            Toast.makeText(this, "jawaban anda salah, pastikan lagi!", Toast.LENGTH_SHORT).show();
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
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(EvaluationActivity.this).build();

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
