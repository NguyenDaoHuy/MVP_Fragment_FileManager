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
import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.R;

public class ImageFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {
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
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ImageFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(MainActivity.getImageFolderList() == null){
            return 0;
        } else {
            MainActivity.getImageFolderList().size();
        }
        return MainActivity.getImageFolderList().size();
    }

    @Override
    public String file(int position) {
        return MainActivity.getImageFolderList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = MainActivity.getImageFolderList().get(position).lastIndexOf("/");
        String nameOFFolder = MainActivity.getImageFolderList().get(position).substring(indexPath+1);
        Intent intent = new Intent(this, ImageFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}