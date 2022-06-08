package com.example.filemanagerapp.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.NonNull;
import com.example.filemanagerapp.model.FileItem;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.io.File;
import java.util.ArrayList;

public class VideoPlayerPresenter implements VideoContract.PlayerVideoPresenter {
    VideoContract.PlayerVideoView view;

    public VideoPlayerPresenter(VideoContract.PlayerVideoView view) {
        this.view = view;
    }

    @Override
    public void playerVideo(MediaPlayer mediaPlayer, ArrayList<FileItem> fileItemArrayList, int position, SimpleExoPlayer player, Context context, PlayerView playerView) {
        mediaPlayer.reset();
        String path = fileItemArrayList.get(position).getPath();
        Uri uri = Uri.parse(path);
        player = new SimpleExoPlayer.Builder(context).build();
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context,"app"));
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        for(int i = 0; i< fileItemArrayList.size(); i++){
            new File(String.valueOf(fileItemArrayList.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                    .createMediaSource(Uri.parse(String.valueOf(uri)));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.prepare(concatenatingMediaSource);
        player.seekTo(position, C.TIME_UNSET);
        playError(player);
    }

    @Override
    public void playError(SimpleExoPlayer player) {
        player.addListener(new Player.EventListener(){
            @Override
            public void onPlayerError(@NonNull ExoPlaybackException error) {
                view.playErrorToast("Video Playing Error");
            }
        });
        player.setPlayWhenReady(true);
    }
}
