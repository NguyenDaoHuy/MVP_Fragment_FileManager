package com.example.filemanagerapp.audio;


public interface AudioView {
    interface PlayerAudioView{
        void setResourcesWithMusic();
        void setTextCurrenTime();
        void setPlaying();
        void setPausing();
    }
}
