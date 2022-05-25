package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
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

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private int position;
    private String videoTitle;
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private TextView title;
    private ConcatenatingMediaSource concatenatingMediaSource;
    private MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.exoplayer_view);
//        getSupportActionBar().hide();
        position = getIntent().getIntExtra("position",1);
        videoTitle = getIntent().getStringExtra("video_title");
        fileItemArrayList = getIntent().getExtras().getParcelableArrayList("videoArrayList");

     //   title = findViewById(R.id.video_title);
     //   title.setText(videoTitle);

        playerVideo();
    }

    private void playerVideo() {
        mediaPlayer.reset();
        String path = fileItemArrayList.get(position).getPath();
        Uri uri = Uri.parse(path);
        player = new SimpleExoPlayer.Builder(this).build();
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(
                this, Util.getUserAgent(this,"app"));
        concatenatingMediaSource = new ConcatenatingMediaSource();
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
        playError();
    }

    private void playError() {
        player.addListener(new Player.EventListener(){
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Toast.makeText(VideoPlayerActivity.this,"Video Playing Error",Toast.LENGTH_SHORT).show();
            }
        });
        player.setPlayWhenReady(true);
    }
}