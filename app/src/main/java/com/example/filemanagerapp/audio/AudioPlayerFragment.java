package com.example.filemanagerapp.audio;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.filemanagerapp.activity.MyMediaPlayer;
import com.example.filemanagerapp.databinding.ActivityAudioPlayerBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioPlayerFragment extends Fragment implements AudioContract.PlayerAudioView{
    public static final String TAG = AudioPlayerFragment.class.getName();
    private ArrayList<FileItem> audioArrayList;
    private final MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    private ActivityAudioPlayerBinding binding;
    private int position;
    private AudioPlayerPresenter audioPlayerPresenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_audio_player,container,false);
        View view = binding.getRoot();
        binding.audioName.setSelected(true);
        position = getArguments().getInt("position");
        audioArrayList = getArguments().getParcelableArrayList("LIST");
        audioPlayerPresenter = new AudioPlayerPresenter(this);
        setResourcesWithMusic();
        audioPlayerPresenter.checkPlaying(getActivity(),mediaPlayer,binding.seekBar);
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

    @Override
    public void setResourcesWithMusic() {
        FileItem audio = audioArrayList.get(position);
        binding.audioName.setText(audio.getDisplayName());
        binding.totalTime.setText(convertToMMSS(audio.getDuration()));
        audioPlayerPresenter.playMusic(mediaPlayer, audio,binding.seekBar);
        binding.next.setOnClickListener(v -> audioPlayerPresenter.nextMusic(audioArrayList,mediaPlayer));
        binding.previous.setOnClickListener(v -> audioPlayerPresenter.previousMusic(mediaPlayer));
        binding.pausePlay.setOnClickListener(v -> audioPlayerPresenter.pauseMusic(mediaPlayer));
    }

    @Override
    public void setTextCurrenTime() {
        binding.currentTime.setText(audioPlayerPresenter.convertToMMSS(mediaPlayer.getCurrentPosition()+""));
    }

    @Override
    public void setPlaying() {
        binding.pausePlay.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void setPausing() {
        binding.pausePlay.setImageResource(R.drawable.exo_icon_pause);
    }


    @SuppressLint("DefaultLocale")
    public static String convertToMMSS(String duration){
        long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}