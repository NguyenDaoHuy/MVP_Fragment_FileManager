package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;

import com.example.filemanagerapp.Adapter.ImagesAdapter;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;

import java.util.ArrayList;

public class ImageFilesActivity extends AppCompatActivity {
    private ListView lvListItem;
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private ImagesAdapter imagesAdapter;
    private String folder_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        lvListItem = findViewById(R.id.lvListItem);
        folder_name = getIntent().getStringExtra("folderName");
        showImageFile();
    }
    private void showImageFile() {
        fileItemArrayList = fetchMedia(folder_name);
        imagesAdapter = new ImagesAdapter(ImageFilesActivity.this, fileItemArrayList);
        lvListItem.setAdapter(imagesAdapter);
    }

    private ArrayList<FileItem> fetchMedia(String folderName) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media.DATA+" like?";
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
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);
                fileItems.add(fileItem);
            }while(cursor.moveToNext());
        }
        return fileItems;
    }
}