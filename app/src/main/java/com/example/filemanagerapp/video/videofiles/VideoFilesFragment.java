package com.example.filemanagerapp.video.videofiles;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.video.videoplayer.VideoPlayerFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;

public class VideoFilesFragment extends Fragment implements VideoFilesAdapter.VideoFilesInterface, InterfaceContract.setFileView {
    public static final String TAG = VideoFilesFragment.class.getName();

    private VideoFilesAdapter videoFilesAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private VideoFilesPresenter videoFilesPresenter;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.ActivityVideoFilesBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_video_files, container, false);
        View view = binding.getRoot();
        String folder_name = getArguments().getString("nameOFFolder");
        videoFilesPresenter = new VideoFilesPresenter(folder_name,getActivity(),this);
        binding.tvVideoFolder.setText(folder_name);
        binding.btnBack.setOnClickListener(v -> {
            if(getFragmentManager() != null){
                getFragmentManager().popBackStack();
            }
        });
        videoFilesAdapter = new VideoFilesAdapter(this);
        binding.videoRv.setAdapter(videoFilesAdapter);
        binding.videoRv.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,false));
        videoFilesAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public int getCount() {
        if(videoFilesPresenter.getFileItemArrayList() == null){
            return 0;
        } else {
            videoFilesPresenter.getFileItemArrayList() .size();
        }
        return videoFilesPresenter.getFileItemArrayList() .size();
    }

    @Override
    public FileItem file(int position) {
        return videoFilesPresenter.getFileItemArrayList() .get(position);
    }

    @Override
    public void onClickItem(int position) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("video_title", videoFilesPresenter.getFileItemArrayList() .get(position).getDisplayName());
        bundle.putParcelableArrayList("videoArrayList", videoFilesPresenter.getFileItemArrayList() );
        videoPlayerFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, videoPlayerFragment);
        fragmentTransaction.addToBackStack(VideoPlayerFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onLongClick(int position, View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(),v);
        popupMenu.getMenu().add("OPEN");
        popupMenu.getMenu().add("DELETE");
        popupMenu.getMenu().add("DETAIL");
        popupMenu.getMenu().add("SHARE");
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getTitle().equals("OPEN")){
                VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putString("video_title", videoFilesPresenter.getFileItemArrayList() .get(position).getDisplayName());
                bundle.putParcelableArrayList("videoArrayList", videoFilesPresenter.getFileItemArrayList() );
                videoPlayerFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentMain, videoPlayerFragment);
                fragmentTransaction.addToBackStack(VideoPlayerFragment.TAG);
                fragmentTransaction.commit();
            }
            if(item.getTitle().equals("DELETE")){
                AlertDialog.Builder alerDialog = new AlertDialog.Builder(getContext());
                alerDialog.setTitle("Deleta");
                alerDialog.setMessage("Do you want to delete this video ?");
                alerDialog.setPositiveButton("Delete", (dialog, which) -> {
                    Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            Long.parseLong(videoFilesPresenter.getFileItemArrayList() .get(position).getId()));
                    File file = new File(videoFilesPresenter.getFileItemArrayList() .get(position).getPath());
                    boolean delete = file.delete();
                    if(delete){
                        getActivity().getContentResolver().delete(uri,null,null);
                        videoFilesPresenter.getFileItemArrayList() .remove(position);
                        videoFilesAdapter.notifyItemRemoved(position);
                        videoFilesAdapter.notifyItemRangeChanged(position,videoFilesPresenter.getFileItemArrayList() .size());
                        Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Can't deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                alerDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                alerDialog.show();
            }
            if(item.getTitle().equals("DETAIL")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Information");
                FileItem file = videoFilesPresenter.getFileItemArrayList() .get(position);
                long longTime = Long.parseLong(file.getDateAdded()) * 1000;
                builder.setMessage("Name :" + file.getDisplayName() +
                        "\nSize :" + android.text.format.Formatter.formatFileSize(getContext(),
                        Long.parseLong(videoFilesPresenter.getFileItemArrayList() .get(position).getSize())) +
                        "\nDate :" + VideoFilesAdapter.convertEpouch(longTime));
                builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
                AlertDialog al = builder.create();
                al.show();
            }
            if(item.getTitle().equals("SHARE")){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Download this file";
                String sub = "http://play.google.com";
                intent.putExtra(Intent.EXTRA_TEXT,body);
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent,"Share using "));
            }
            return true;
        });
        popupMenu.show();
            return true;
    }

    @Override
    public void onMenuClick(int position) {
        bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetTheme);
        View bsView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout,
                requireActivity().findViewById(R.id.bottom_sheet));
        bsView.findViewById(R.id.bs_language).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(bsView);
        bottomSheetDialog.show();
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void setSuccess(String str) {
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setError(String str) {
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
}