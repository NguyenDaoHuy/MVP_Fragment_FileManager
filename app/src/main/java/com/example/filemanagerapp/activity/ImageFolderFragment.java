package com.example.filemanagerapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.adapter.FolderRecyclerViewAdapter;
import com.example.filemanagerapp.model.FileItem;

import java.util.ArrayList;

public class ImageFolderFragment extends Fragment implements FolderRecyclerViewAdapter.FolderInterface{
    private final ArrayList<String> folderImageList = new ArrayList<>();
    private final ArrayList<FileItem> fileImageList = new ArrayList<>();
    private View view;

    public ImageFolderFragment() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.FragmentFolderImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_folder_image, container, false);
        view = binding.getRoot();
        getFolderImage();
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
        folderImageList.size();
        return folderImageList.size();
    }

    @Override
    public String file(int position) {
        return folderImageList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = folderImageList.get(position).lastIndexOf("/");
        String nameOFFolder = folderImageList.get(position).substring(indexPath+1);
        ImageFilesFragment imageFilesFragment = new ImageFilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nameOFFolder",nameOFFolder);
        imageFilesFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, imageFilesFragment);
        fragmentTransaction.addToBackStack(ImageFilesFragment.TAG);
        fragmentTransaction.commit();
    }
    public void getFolderImage(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
        if(cursor != null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);

                int index = path.lastIndexOf("/");
                String subString = path.substring(0,index);
                if(!folderImageList.contains(subString)){
                    folderImageList.add(subString);
                }
                fileImageList.add(fileItem);
            }while (cursor.moveToNext());
        }
    }
}