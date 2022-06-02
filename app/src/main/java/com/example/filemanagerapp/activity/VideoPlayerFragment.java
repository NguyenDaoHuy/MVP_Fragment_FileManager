package com.example.filemanagerapp.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.filemanagerapp.databinding.ActivityVideoPlayerBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;

public class VideoPlayerFragment extends Fragment {

    private SimpleExoPlayer player;
    private int position;
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private final MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    private ActivityVideoPlayerBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_video_player,container,false);
        view = binding.getRoot();
        position = getArguments().getInt("position");
        fileItemArrayList = getArguments().getParcelableArrayList("videoArrayList");
        playerVideo();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void playerVideo() {
        mediaPlayer.reset();
        String path = fileItemArrayList.get(position).getPath();
        Uri uri = Uri.parse(path);
        player = new SimpleExoPlayer.Builder(getContext()).build();
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(
                getContext(), Util.getUserAgent(getContext(),"app"));
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        for(int i = 0; i< fileItemArrayList.size(); i++){
            new File(String.valueOf(fileItemArrayList.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                    .createMediaSource(Uri.parse(String.valueOf(uri)));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        binding.exoplayerView.setPlayer(player);
        binding.exoplayerView.setKeepScreenOn(true);
        player.prepare(concatenatingMediaSource);
        player.seekTo(position, C.TIME_UNSET);
        playError();
    }

    private void playError() {
        player.addListener(new Player.EventListener(){
            @Override
            public void onPlayerError(@NonNull ExoPlaybackException error) {
                Toast.makeText(getContext(),"Video Playing Error",Toast.LENGTH_SHORT).show();
            }
        });
        player.setPlayWhenReady(true);
    }
}