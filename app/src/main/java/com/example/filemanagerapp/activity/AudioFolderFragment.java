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
import com.example.filemanagerapp.databinding.ActivityAudioFolderBinding;
import com.example.filemanagerapp.model.FileItem;

import java.util.ArrayList;

public class AudioFolderFragment extends Fragment implements FolderRecyclerViewAdapter.FolderInterface {
    private ArrayList<String> folderAudioList = new ArrayList<>();
    private ActivityAudioFolderBinding binding;
    private ArrayList<FileItem> itemAudioArrayList = new ArrayList<>();
    private View view;

    public AudioFolderFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_audio_folder,container,false);
        view = binding.getRoot();
        getFolderAudio();
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        binding.lvFolderAudio.setAdapter(adapter);
        binding.lvFolderAudio.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
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
        if(folderAudioList == null){
            return 0;
        } else {
            folderAudioList.size();
        }
        return folderAudioList.size();
    }

    @Override
    public String file(int position) {
        return folderAudioList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = folderAudioList.get(position).lastIndexOf("/");
        String nameOFFolder = folderAudioList.get(position).substring(indexPath+1);
        AudioFilesActivity audioFilesActivity = new AudioFilesActivity();
        Bundle bundle = new Bundle();
        bundle.putString("nameOFFolder",nameOFFolder);
        audioFilesActivity.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.fragmentMain, audioFilesActivity).commit();
    }
    public void getFolderAudio(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
        if(cursor != null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);

                int index = path.lastIndexOf("/");
                String subString = path.substring(0,index);
                if(!folderAudioList.contains(subString)){
                    folderAudioList.add(subString);
                }
                itemAudioArrayList.add(fileItem);
            }while (cursor.moveToNext());
        }
    }
}