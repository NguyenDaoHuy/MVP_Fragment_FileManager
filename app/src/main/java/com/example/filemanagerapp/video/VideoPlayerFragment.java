package com.example.filemanagerapp.video;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.activity.MyMediaPlayer;
import com.example.filemanagerapp.model.FileItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import java.util.ArrayList;

public class VideoPlayerFragment extends Fragment implements VideoContract.PlayerVideoView {
    public static final String TAG = VideoPlayerFragment.class.getName();
    private SimpleExoPlayer player;
    private final MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.ActivityVideoPlayerBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_video_player, container, false);
        View view = binding.getRoot();
        int position = getArguments().getInt("position");
        ArrayList<FileItem> fileItemArrayList = getArguments().getParcelableArrayList("videoArrayList");
        binding.btnBack.setOnClickListener(v -> {
            if(getFragmentManager() != null){
                getFragmentManager().popBackStack();
            }
        });
        VideoPlayerPresenter videoPlayerPresenter = new VideoPlayerPresenter(this);
        videoPlayerPresenter.playerVideo(mediaPlayer, fileItemArrayList, position,player,getContext(), binding.exoplayerView);
        return view;
    }

    @Override
    public void playErrorToast(String str) {
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
}