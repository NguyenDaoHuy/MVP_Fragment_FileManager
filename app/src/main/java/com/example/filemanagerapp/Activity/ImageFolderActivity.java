package com.example.filemanagerapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Adapter.FolderRecyclerViewAdapter;
import com.example.filemanagerapp.R;

import java.util.ArrayList;

public class ImageFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {
    private ArrayList<String> folderImageList;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder);
        RecyclerView recyclerView = findViewById(R.id.lvFolderImage);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
        }
        folderImageList = getIntent().getExtras().getStringArrayList("folderImageList");
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ImageFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(folderImageList == null){
            return 0;
        } else {
            folderImageList.size();
        }
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
        Intent intent = new Intent(this, ImageFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}