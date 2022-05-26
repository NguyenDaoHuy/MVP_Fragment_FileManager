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

public class AudioFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {
    private RecyclerView recyclerView;
    private TextView tvThongBao;
    private FolderRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_folder);
        recyclerView = findViewById(R.id.lvFolderAudio);
        tvThongBao = findViewById(R.id.tvThongBaoAudio);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
        }
        if(MainActivity.itemAudioArrayList.size() == 0){
            tvThongBao.setText("Không có dữ liệu");
        }
        adapter = new FolderRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AudioFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }
  /*  public ArrayList<FileItem> fetchMedia(){
        ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
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
                if(!allFolderList.contains(subString)){
                    allFolderList.add(subString);
                    System.out.println("--------------------->"+allFolderList.size());
                }
                fileItemArrayList.add(fileItem);
            }while (cursor.moveToNext());
        }
        return fileItemArrayList;
    } */

    @Override
    public int getCount() {
        if(MainActivity.audioFolderList==null || MainActivity.audioFolderList.size()<0){
            return 0;
        }
        return MainActivity.audioFolderList.size();
    }

    @Override
    public String file(int position) {
        return MainActivity.audioFolderList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = MainActivity.audioFolderList.get(position).lastIndexOf("/");
        String nameOFFolder = MainActivity.audioFolderList.get(position).substring(indexPath+1);
        Intent intent = new Intent(this, AudioFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}