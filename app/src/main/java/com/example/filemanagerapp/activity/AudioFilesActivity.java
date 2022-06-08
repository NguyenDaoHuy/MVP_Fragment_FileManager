package com.example.filemanagerapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.adapter.AudioFilesAdapter;
import com.example.filemanagerapp.audio.AudioFilesPresenter;
import com.example.filemanagerapp.audio.AudioPlayerFragment;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;

public class AudioFilesActivity extends Fragment implements AudioFilesAdapter.AudioFileInterface, InterfaceContract.setFileView {
    public static final String TAG = AudioFilesActivity.class.getName();
    private AudioFilesPresenter audioFilesPresenter;
    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.ActivityAudioBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_audio, container, false);
        View view = binding.getRoot();
        String folder_name = getArguments().getString("nameOFFolder");

        audioFilesPresenter = new AudioFilesPresenter(this, folder_name,getActivity());

        binding.tvAudioFolder.setText(folder_name);
        AudioFilesAdapter audioFilesAdapter = new AudioFilesAdapter(this);
        binding.audioRv.setAdapter(audioFilesAdapter);
        binding.audioRv.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,false));
        audioFilesAdapter.notifyDataSetChanged();

        binding.btnBack.setOnClickListener(v -> {
            if(getFragmentManager() != null){
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        if(audioFilesPresenter.getFileItemArrayList() == null){
            return 0;
        } else {
            audioFilesPresenter.getFileItemArrayList().size();
        }
        return audioFilesPresenter.getFileItemArrayList().size();
    }

    @Override
    public FileItem audio(int position) {
        return audioFilesPresenter.getFileItemArrayList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        AudioPlayerFragment audioPlayerFragment = new AudioPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putParcelableArrayList("LIST",audioFilesPresenter.getFileItemArrayList());
        audioPlayerFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, audioPlayerFragment);
        fragmentTransaction.addToBackStack(AudioPlayerFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void setSuccess(String str) {

    }

    @Override
    public void setError(String str) {
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
}