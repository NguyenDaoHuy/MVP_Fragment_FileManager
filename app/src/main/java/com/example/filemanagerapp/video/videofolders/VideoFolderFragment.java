
package com.example.filemanagerapp.video.videofolders;

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
import com.example.filemanagerapp.adapter.FolderRecyclerViewAdapter;
import com.example.filemanagerapp.video.videofiles.VideoFilesFragment;

public class VideoFolderFragment extends Fragment implements FolderRecyclerViewAdapter.FolderInterface, InterfaceContract.setFileView {

    private VideoFolderPresenter videoFolderPresenter;
    private View view;
    public VideoFolderFragment(){

    }

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.ActivityVideoFolderBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_video_folder, container, false);
        view = binding.getRoot();
        videoFolderPresenter = new VideoFolderPresenter(this,getActivity());
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        binding.lvFolderVideo.setAdapter(adapter);
        binding.lvFolderVideo.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public int getCount() {
        if(videoFolderPresenter.getFolderVideoList() == null){
            return 0;
        } else {
            videoFolderPresenter.getFolderVideoList().size();
        }
        return videoFolderPresenter.getFolderVideoList().size();
    }

    @Override
    public String file(int position) {
        return videoFolderPresenter.getFolderVideoList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = videoFolderPresenter.getFolderVideoList().get(position).lastIndexOf("/");
        String nameOFFolder = videoFolderPresenter.getFolderVideoList().get(position).substring(indexPath+1);
        VideoFilesFragment videoFilesFragment = new VideoFilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nameOFFolder",nameOFFolder);
        videoFilesFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, videoFilesFragment);
        fragmentTransaction.addToBackStack(VideoFilesFragment.TAG);
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