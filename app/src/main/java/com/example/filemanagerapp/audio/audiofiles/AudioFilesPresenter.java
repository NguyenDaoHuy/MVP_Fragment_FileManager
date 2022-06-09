package com.example.filemanagerapp.audio.audiofiles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.model.FileItem;
import java.util.ArrayList;

public class AudioFilesPresenter implements IAudioFilePresenter{
    private InterfaceContract.setFileView view;
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();

    public AudioFilesPresenter(InterfaceContract.setFileView view, String folderName, Activity activity) {
        this.view = view;
        inti(folderName,activity);
    }

    public ArrayList<FileItem> getFileItemArrayList() {
        return fileItemArrayList;
    }

    @Override
    public void inti(String folderName, Activity activity){
        fileItemArrayList = fetchMedia(folderName,activity);
        if(fileItemArrayList!=null){

        }else {
            view.setError("Không có dữ liệu ảnh");
        }
    }


    private ArrayList<FileItem> fetchMedia(String folderName, Activity activity) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.DATA+" like?";
        String[] selectionArg = new String[]{"%"+folderName+"%"};
        @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(uri,null,
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
}
