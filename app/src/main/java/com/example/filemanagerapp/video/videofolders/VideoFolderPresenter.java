package com.example.filemanagerapp.video.videofolders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.model.FileItem;

import java.util.ArrayList;

public class VideoFolderPresenter {
    private final ArrayList<String> folderVideoList = new ArrayList<>();
    private final InterfaceContract.setFileView view;

    public VideoFolderPresenter(InterfaceContract.setFileView view,Activity activity) {
        this.view = view;
        inti(activity);
    }
    private void inti(Activity activity){
        getFolderVideo(activity);
        if(folderVideoList.size() > 0){

        }else {
            view.setError("Khong co du lieu");
        }
    }

    public ArrayList<String> getFolderVideoList() {
        return folderVideoList;
    }

    public void getFolderVideo(Activity activity){
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(uri,null,null,null,null);
        if(cursor != null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);

                int index = path.lastIndexOf("/");
                String subString = path.substring(0,index);
                if(!folderVideoList.contains(subString)){
                    folderVideoList.add(subString);
                }
            }while (cursor.moveToNext());

        }
    }
}
