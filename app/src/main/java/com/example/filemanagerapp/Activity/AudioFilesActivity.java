package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.example.filemanagerapp.Adapter.AudioFilesAdapter;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import java.util.ArrayList;

public class AudioFilesActivity extends AppCompatActivity implements AudioFilesAdapter.AudioFileInterface {

    private RecyclerView recyclerView;
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private String folder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        recyclerView = findViewById(R.id.audio_rv);
        folder_name = getIntent().getStringExtra("folderName");
        showVideoFile();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showVideoFile() {
        fileItemArrayList = fetchMedia(folder_name);
        AudioFilesAdapter audioFilesAdapter = new AudioFilesAdapter(this);
        recyclerView.setAdapter(audioFilesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL,false));
        audioFilesAdapter.notifyDataSetChanged();
    }

    private ArrayList<FileItem> fetchMedia(String folderName) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.DATA+" like?";
        String[] selectionArg = new String[]{"%"+folderName+"%"};
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri,null,
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
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new AudioFilesAdapter(this));
        }
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
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.currentIndex = position;
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("LIST",fileItemArrayList);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public Context context() {
        return getApplicationContext();
    }
}