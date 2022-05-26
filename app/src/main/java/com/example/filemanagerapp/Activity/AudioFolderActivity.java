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

public class AudioFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_folder);
        RecyclerView recyclerView = findViewById(R.id.lvFolderAudio);
        TextView tvThongBao = findViewById(R.id.tvThongBaoAudio);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
        }
        if(MainActivity.getItemAudioArrayList().size() == 0){
            tvThongBao.setText("Không có dữ liệu");
        }
        FolderRecyclerViewAdapter adapter = new FolderRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AudioFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(MainActivity.getAudioFolderList() == null){
            return 0;
        } else {
            MainActivity.getAudioFolderList().size();
        }
        return MainActivity.getAudioFolderList().size();
    }

    @Override
    public String file(int position) {
        return MainActivity.getAudioFolderList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = MainActivity.getAudioFolderList().get(position).lastIndexOf("/");
        String nameOFFolder = MainActivity.getAudioFolderList().get(position).substring(indexPath+1);
        Intent intent = new Intent(this, AudioFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}