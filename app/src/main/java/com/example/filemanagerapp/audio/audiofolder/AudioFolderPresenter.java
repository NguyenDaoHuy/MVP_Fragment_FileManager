package com.example.filemanagerapp.audio.audiofolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.model.FileItem;
import java.util.ArrayList;

public class AudioFolderPresenter implements IAudioFolderPresenter{
    private final ArrayList<String> folderAudioList = new ArrayList<>();
    private final ArrayList<FileItem> itemAudioArrayList = new ArrayList<>();
    private final InterfaceContract.setFileView view;

    public AudioFolderPresenter(InterfaceContract.setFileView view,Activity activity) {
        this.view = view;
        inti(activity);
    }
    @Override
    public void inti(Activity activity){
        getFolderAudio(activity);
        if (folderAudioList.size() > 0){

        }else {
            view.setError("Khong co du lieu");
        }
    }

    public ArrayList<String> getFolderAudioList() {
        return folderAudioList;
    }

    private void getFolderAudio(Activity activity){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(uri,null,null,null,null);
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
                if(!folderAudioList.contains(subString)){
                    folderAudioList.add(subString);
                }
                itemAudioArrayList.add(fileItem);
            }while (cursor.moveToNext());
        }
    }
}
