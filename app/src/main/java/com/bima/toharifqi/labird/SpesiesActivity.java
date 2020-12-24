package com.bima.toharifqi.labird;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bima.toharifqi.labird.model.SpesiesModel;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class SpesiesActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
    private TextView spesiesName, spesiesLatin, sDescText, sSoundText, sDistributionText, sHabitText, sFoundedText;

    Toolbar toolbar;

    //Views for audio
    private ImageButton playBtn;
    private SeekBar seekBar;
    private TextView timeRemaining;
    private MediaPlayer mediaPlayer;

    private int mediaLength, realTimeLength;

    final Handler handler = new Handler();

    SpesiesModel spesiesModelExtra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spesies);

        // Action Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        //get data from adapter
        spesiesModelExtra = getIntent().getParcelableExtra("spesiesModelExtra");


        //tipycal views
        spesiesName = findViewById(R.id.spesiesName);
        spesiesLatin = findViewById(R.id.spesiesLatin);
        sDescText = findViewById(R.id.spesies_desc);
        sSoundText = findViewById(R.id.spesies_sound);
        sDistributionText = findViewById(R.id.spesies_distribution);
        sHabitText = findViewById(R.id.spesies_habit);
        sFoundedText = findViewById(R.id.spesies_founded);

        //set tipycal data
        spesiesName.setText(spesiesModelExtra.getSpesiesName());
        spesiesLatin.setText("(" + spesiesModelExtra.getSpesiesLatin() + ")");
        sDescText.setText(Html.fromHtml(spesiesModelExtra.getsDescText()));
        sSoundText.setText(Html.fromHtml(spesiesModelExtra.getsSoundText()));
        sDistributionText.setText(Html.fromHtml(spesiesModelExtra.getsDistributionText()));
        sHabitText.setText(Html.fromHtml(spesiesModelExtra.getsHabitText()));
        sFoundedText.setText(Html.fromHtml(spesiesModelExtra.getsFoundedText()));

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
                final AlertDialog dialog = new SpotsDialog.Builder().setContext(SpesiesActivity.this).build();

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

                mp3Play.execute(spesiesModelExtra.getSpesiesSound());
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

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
