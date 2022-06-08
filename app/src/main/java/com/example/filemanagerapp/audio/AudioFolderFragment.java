package com.example.filemanagerapp.audio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.activity.AudioFilesActivity;
import com.example.filemanagerapp.adapter.FolderRecyclerViewAdapter;

public class AudioFolderFragment extends Fragment implements FolderRecyclerViewAdapter.FolderInterface, InterfaceContract.setFileView {

    private View view;
    private AudioFolderPresenter audioFolderPresenter;
    public AudioFolderFragment(){

    }

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.ActivityAudioFolderBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_audio_folder, container, false);
        view = binding.getRoot();
        audioFolderPresenter = new AudioFolderPresenter(this,getActivity());
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        binding.lvFolderAudio.setAdapter(adapter);
        binding.lvFolderAudio.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public int getCount() {
        audioFolderPresenter.getFolderAudioList().size();
        return audioFolderPresenter.getFolderAudioList().size();
    }

    @Override
    public String file(int position) {
        return audioFolderPresenter.getFolderAudioList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = audioFolderPresenter.getFolderAudioList().get(position).lastIndexOf("/");
        String nameOFFolder = audioFolderPresenter.getFolderAudioList().get(position).substring(indexPath+1);
        AudioFilesActivity audioFilesActivity = new AudioFilesActivity();
        Bundle bundle = new Bundle();
        bundle.putString("nameOFFolder",nameOFFolder);
        audioFilesActivity.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, audioFilesActivity);
        fragmentTransaction.addToBackStack(AudioFilesActivity.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void setSuccess(String str) {

    }

    @Override
    public void setError(String str) {
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
}