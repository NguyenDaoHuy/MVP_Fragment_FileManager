
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
import com.example.filemanagerapp.Adapter.FolderRecyclerViewAdapter;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.databinding.ActivityVideoFolderBinding;

import java.util.ArrayList;

public class VideoFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {

    private ArrayList<String> folderVideoList;
    private ActivityVideoFolderBinding binding;
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video_folder);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
        }
        folderVideoList = getIntent().getExtras().getStringArrayList("folderVideoList");
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        binding.lvFolderVideo.setAdapter(adapter);
        binding.lvFolderVideo.setLayoutManager(new LinearLayoutManager(VideoFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
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
        Intent intent = new Intent(this, VideoFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}