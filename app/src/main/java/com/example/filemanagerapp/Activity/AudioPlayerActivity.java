package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioPlayerActivity extends AppCompatActivity {

    private TextView audio_name,current_time,total_time;
    private SeekBar seek_bar;
    private ImageView next,pause_play,previous;
    private ArrayList<FileItem> audioArrayList;
    private FileItem audio;
    private MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        audio_name = findViewById(R.id.audio_name);
        current_time = findViewById(R.id.current_time);
        total_time = findViewById(R.id.total_time);
        seek_bar = findViewById(R.id.seek_bar);
        next = findViewById(R.id.next);
        pause_play = findViewById(R.id.pause_play);
        previous = findViewById(R.id.previous);
        audio_name.setSelected(true);

        audioArrayList = (ArrayList<FileItem>) getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();
        AudioPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                 if(mediaPlayer != null){
                     seek_bar.setProgress(mediaPlayer.getCurrentPosition());
                     current_time.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                     if(mediaPlayer.isPlaying()){
                         pause_play.setImageResource(R.drawable.exo_icon_pause);
                     }else {
                         pause_play.setImageResource(R.drawable.ic_play);
                     }
                 }
                 new Handler().postDelayed(this,100);
            }
        });
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                     mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setResourcesWithMusic() {
        audio = audioArrayList.get(MyMediaPlayer.currentIndex);
        audio_name.setText(audio.getDisplayName());
        total_time.setText(convertToMMSS(audio.getDuration()));
        playMusic();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 nextMusic();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 previousMusic();
            }
        });
        pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 pauseMusic();
            }
        });
    }
    private void playMusic(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(audio.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seek_bar.setProgress(0);
            seek_bar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void nextMusic(){
        if(MyMediaPlayer.currentIndex == audioArrayList.size()-1)
             return;
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void previousMusic(){
        if(MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void pauseMusic(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }
    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}