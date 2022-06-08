package com.example.filemanagerapp.audio;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;
import com.example.filemanagerapp.activity.MyMediaPlayer;
import com.example.filemanagerapp.model.FileItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioPlayerPresenter implements AudioContract.PlayerAudioPresenter {

    AudioContract.PlayerAudioView view;

    public AudioPlayerPresenter(AudioContract.PlayerAudioView view) {
        this.view = view;
    }

    @Override
    public void playMusic(MediaPlayer mediaPlayer, FileItem audio, SeekBar seek_bar) {
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

    @Override
    public void nextMusic(ArrayList<FileItem> audioArrayList,MediaPlayer mediaPlayer) {
        if(MyMediaPlayer.currentIndex == audioArrayList.size()-1)
            return;
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        view.setResourcesWithMusic();
    }

    @Override
    public void previousMusic(MediaPlayer mediaPlayer) {
        if(MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        view.setResourcesWithMusic();
    }

    @Override
    public void pauseMusic(MediaPlayer mediaPlayer) {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    @Override
    public String convertToMMSS(String duration) {
        long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    @Override
    public void checkPlaying(Activity activity,MediaPlayer mediaPlayer, SeekBar seek_bar) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    seek_bar.setProgress(mediaPlayer.getCurrentPosition());
                    view.setTextCurrenTime();
                    if(mediaPlayer.isPlaying()){
                        view.setPausing();
                    }else {
                        view.setPlaying();
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
    }

}
