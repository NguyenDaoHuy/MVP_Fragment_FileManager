package com.example.filemanagerapp.image.imagefolders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.adapter.FolderRecyclerViewAdapter;
import com.example.filemanagerapp.image.imagefiles.ImageFilesFragment;

public class ImageFolderFragment extends Fragment implements FolderRecyclerViewAdapter.FolderInterface, InterfaceContract.setFileView {
    private View view;
    private ImageFolderPresenter imageFolderPresenter;
    public ImageFolderFragment() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.FragmentFolderImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_folder_image, container, false);
        view = binding.getRoot();
        imageFolderPresenter = new ImageFolderPresenter(this,getActivity());
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        binding.lvFolderImage.setAdapter(adapter);
        binding.lvFolderImage.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
        binding.btnBack.setOnClickListener(v -> {
             Intent intent = new Intent(view.getContext(), MainActivity.class);
             startActivity(intent);
        });
        return view;
    }
    @Override
    public int getCount() {
        imageFolderPresenter.getFolderImageList().size();
        return imageFolderPresenter.getFolderImageList().size();
    }

    @Override
    public String file(int position) {
        return imageFolderPresenter.getFolderImageList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = imageFolderPresenter.getFolderImageList().get(position).lastIndexOf("/");
        String nameOFFolder = imageFolderPresenter.getFolderImageList().get(position).substring(indexPath+1);
        ImageFilesFragment imageFilesFragment = new ImageFilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nameOFFolder",nameOFFolder);
        imageFilesFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, imageFilesFragment);
        fragmentTransaction.addToBackStack(ImageFilesFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void setSuccess(String str) {

    }

    @Override
    public void setError(String str) {

    }
}