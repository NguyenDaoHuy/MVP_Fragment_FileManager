package com.example.filemanagerapp.activity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.filemanagerapp.databinding.ActivityAudioPlayerBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioPlayerFragment extends Fragment {
    public static final String TAG = AudioPlayerFragment.class.getName();
    private ArrayList<FileItem> audioArrayList;
    private FileItem audio;
    private final MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    private ActivityAudioPlayerBinding binding;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_audio_player,container,false);
        View view = binding.getRoot();
        binding.audioName.setSelected(true);
        position = getArguments().getInt("position");
        audioArrayList = getArguments().getParcelableArrayList("LIST");
        setResourcesWithMusic();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    binding.currentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                    if(mediaPlayer.isPlaying()){
                        binding.pausePlay.setImageResource(R.drawable.exo_icon_pause);
                    }else {
                        binding.pausePlay.setImageResource(R.drawable.ic_play);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
        binding.btnBack.setOnClickListener(v -> {
            if(getFragmentManager() != null){
                getFragmentManager().popBackStack();
            }
        });
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        return view;
    }


    private void setResourcesWithMusic() {
        audio = audioArrayList.get(position);
        binding.audioName.setText(audio.getDisplayName());
        binding.totalTime.setText(convertToMMSS(audio.getDuration()));
        playMusic();
        binding.next.setOnClickListener(v -> nextMusic());
        binding.previous.setOnClickListener(v -> previousMusic());
        binding.pausePlay.setOnClickListener(v -> pauseMusic());
    }
    private void playMusic(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(audio.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            binding.seekBar.setProgress(0);
            binding.seekBar.setMax(mediaPlayer.getDuration());
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
    @SuppressLint("DefaultLocale")
    public static String convertToMMSS(String duration){
        long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}