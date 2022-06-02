
package com.example.filemanagerapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.adapter.FolderRecyclerViewAdapter;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.databinding.ActivityVideoFolderBinding;
import com.example.filemanagerapp.model.FileItem;

import java.util.ArrayList;

public class VideoFolderFragment extends Fragment implements FolderRecyclerViewAdapter.FolderInterface {

    private ArrayList<String> folderVideoList = new ArrayList<>();
    private ActivityVideoFolderBinding binding;
    private View view;
    public VideoFolderFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_video_folder,container,false);
        view = binding.getRoot();
        getFolderVideo();
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        binding.lvFolderVideo.setAdapter(adapter);
        binding.lvFolderVideo.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        if(folderVideoList == null){
            return 0;
        } else {
            folderVideoList.size();
        }
        return folderVideoList.size();
    }

    @Override
    public String file(int position) {
        return folderVideoList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = folderVideoList.get(position).lastIndexOf("/");
        String nameOFFolder = folderVideoList.get(position).substring(indexPath+1);
        VideoFilesFragment videoFilesFragment = new VideoFilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nameOFFolder",nameOFFolder);
        videoFilesFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.fragmentMain, videoFilesFragment).commit();
    }
    public void getFolderVideo(){
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
        if(cursor != null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);

                int index = path.lastIndexOf("/");
                String subString = path.substring(0,index);
                if(!folderVideoList.contains(subString)){
                    folderVideoList.add(subString);
                }
            }while (cursor.moveToNext());

        }
    }
}