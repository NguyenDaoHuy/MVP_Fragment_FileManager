package com.example.filemanagerapp.audio;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.SeekBar;
import com.example.filemanagerapp.model.FileItem;
import java.util.ArrayList;
public interface AudioContract {
    interface PlayerAudioView{
        void setResourcesWithMusic();
        void setTextCurrenTime();
        void setPlaying();
        void setPausing();
    }
    interface PlayerAudioPresenter{
        void playMusic(MediaPlayer mediaPlayer, FileItem audio, SeekBar seek_bar);
        void nextMusic(ArrayList<FileItem> audioArrayList, MediaPlayer mediaPlayer);
        void previousMusic(MediaPlayer mediaPlayer);
        void pauseMusic(MediaPlayer mediaPlayer);
        String convertToMMSS(String duration);
        void checkPlaying(Activity activity, MediaPlayer mediaPlayer, SeekBar seek_bar);
    }
}
