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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.filemanagerapp.adapter.AudioFilesAdapter;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;
import java.util.ArrayList;

public class AudioFilesActivity extends Fragment implements AudioFilesAdapter.AudioFileInterface {
    public static final String TAG = AudioFilesActivity.class.getName();
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private String folder_name;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.ActivityAudioBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_audio, container, false);
        View view = binding.getRoot();
        folder_name = getArguments().getString("nameOFFolder");
        showVideoFile();
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

    @SuppressLint("NotifyDataSetChanged")
    private void showVideoFile() {
        fileItemArrayList = fetchMedia(folder_name);

    }

    private ArrayList<FileItem> fetchMedia(String folderName) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.DATA+" like?";
        String[] selectionArg = new String[]{"%"+folderName+"%"};
        @SuppressLint("Recycle") Cursor cursor = getActivity().getContentResolver().query(uri,null,
                selection,selectionArg,null);
        if(cursor!=null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);
                fileItems.add(fileItem);
            }while(cursor.moveToNext());
        }
        return fileItems;
    }

    @Override
    public int getCount() {
        if(fileItemArrayList == null){
            return 0;
        } else {
            fileItemArrayList.size();
        }
        return fileItemArrayList.size();
    }

    @Override
    public FileItem audio(int position) {
        return fileItemArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        AudioPlayerFragment audioPlayerFragment = new AudioPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putParcelableArrayList("LIST",fileItemArrayList);
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
}