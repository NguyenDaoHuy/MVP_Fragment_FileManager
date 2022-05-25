package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.example.filemanagerapp.Adapter.VideoFilesAdapter;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import java.util.ArrayList;

public class VideoFilesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private VideoFilesAdapter videoFilesAdapter;
    private String folder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_files);
        recyclerView = findViewById(R.id.video_rv);
        folder_name = getIntent().getStringExtra("folderName");
        showVideoFile();
    }

    private void showVideoFile() {
        fileItemArrayList = fetchMedia(folder_name);
        videoFilesAdapter = new VideoFilesAdapter(fileItemArrayList,this);
        recyclerView.setAdapter(videoFilesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL,false));
        videoFilesAdapter.notifyDataSetChanged();
    }

    private ArrayList<FileItem> fetchMedia(String folderName) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA+" like?";
        String[] selectionArg = new String[]{"%"+folderName+"%"};
        Cursor cursor = getContentResolver().query(uri,null,
                selection,selectionArg,null);
        if(cursor!=null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);
                fileItems.add(fileItem);
            }while(cursor.moveToNext());
        }
        return fileItems;
    }

 /*   @Override
    public int getCount() {
        if(fileItemArrayList==null || fileItemArrayList.size()<0){
            return 0;
        }
        return fileItemArrayList.size();
    }

    @Override
    public FileItem video(int position) {
        return fileItemArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("video_title", fileItemArrayList.get(position).getDisplayName());
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("videoArrayList", fileItemArrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public Context context() {
        return getApplicationContext();
    } */
}