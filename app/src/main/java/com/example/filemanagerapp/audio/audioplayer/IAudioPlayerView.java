package com.example.filemanagerapp.audio.audioplayer;


public interface IAudioPlayerView {
    interface PlayerAudioView{
        void setResourcesWithMusic();
        void setTextCurrenTime();
        void setPlaying();
        void setPausing();
    }
}
