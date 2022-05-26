
package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.R;

public class VideoFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);
        RecyclerView recyclerView = findViewById(R.id.lvFolderVideo);
        TextView tvThongBao = findViewById(R.id.tvThongBao);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
        }
        if(MainActivity.getVideoFolderList().size() == 0){
            tvThongBao.setText("Không có dữ liệu");
        }
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(VideoFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(MainActivity.getVideoFolderList() == null){
            return 0;
        } else {
            MainActivity.getVideoFolderList().size();
        }
        return MainActivity.getVideoFolderList().size();
    }

    @Override
    public String file(int position) {
        return MainActivity.getVideoFolderList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = MainActivity.getVideoFolderList().get(position).lastIndexOf("/");
        String nameOFFolder = MainActivity.getVideoFolderList().get(position).substring(indexPath+1);
        Intent intent = new Intent(this, VideoFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}