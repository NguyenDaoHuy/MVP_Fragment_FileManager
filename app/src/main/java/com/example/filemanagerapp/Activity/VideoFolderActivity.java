
package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
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

    private RecyclerView recyclerView;
    private TextView tvThongBao;
    private FolderRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);
        recyclerView = findViewById(R.id.lvFolderVideo);
        tvThongBao = findViewById(R.id.tvThongBao);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
        }
        if(MainActivity.videoFolderList.size() == 0){
            tvThongBao.setText("Không có dữ liệu");
        }
        adapter = new FolderRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(VideoFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(MainActivity.videoFolderList==null || MainActivity.videoFolderList.size()<0){
            return 0;
        }
        return MainActivity.videoFolderList.size();
    }

    @Override
    public String file(int position) {
        return MainActivity.videoFolderList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = MainActivity.videoFolderList.get(position).lastIndexOf("/");
        String nameOFFolder = MainActivity.videoFolderList.get(position).substring(indexPath+1);
        Intent intent = new Intent(this, VideoFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}