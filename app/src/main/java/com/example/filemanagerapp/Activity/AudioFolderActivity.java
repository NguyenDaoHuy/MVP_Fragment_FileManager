package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import com.example.filemanagerapp.Adapter.FolderRecyclerViewAdapter;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.databinding.ActivityAudioFolderBinding;

import java.util.ArrayList;

public class AudioFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {
    private ArrayList<String> folderAudioList;
    private ActivityAudioFolderBinding binding;
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_audio_folder);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
        }
        folderAudioList = getIntent().getExtras().getStringArrayList("folderAudioList");

        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        binding.lvFolderAudio.setAdapter(adapter);
        binding.lvFolderAudio.setLayoutManager(new LinearLayoutManager(AudioFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
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
        Intent intent = new Intent(this, AudioFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}