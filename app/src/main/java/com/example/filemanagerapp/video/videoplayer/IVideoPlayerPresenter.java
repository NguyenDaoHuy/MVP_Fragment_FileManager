package com.example.filemanagerapp.video.videoplayer;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.filemanagerapp.model.FileItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;

public interface IVideoPlayerPresenter {

    interface PlayerVideoPresenter{
        void playerVideo(MediaPlayer mediaPlayer, ArrayList<FileItem> fileItemArrayList, int position, SimpleExoPlayer player, Context context, PlayerView playerView);
        void playError(SimpleExoPlayer player);
    }
}
