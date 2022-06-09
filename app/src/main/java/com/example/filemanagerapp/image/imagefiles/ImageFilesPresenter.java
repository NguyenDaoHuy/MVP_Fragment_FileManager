package com.example.filemanagerapp.image.imagefiles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.model.FileItem;
import java.util.ArrayList;

public class ImageFilesPresenter
{
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private final InterfaceContract.setFileView view;

    public ImageFilesPresenter(String folderName, Activity activity, InterfaceContract.setFileView view) {
        this.view = view;
        init(folderName,activity);
    }

    private void init(String folderName,Activity activity){
        fileItemArrayList = fetchMedia(folderName,activity);
        if(fileItemArrayList!=null){

        }else {
           view.setError("Không có dữ liệu ảnh");
        }
    }

    public ArrayList<FileItem> getFileItemArrayList() {
        return fileItemArrayList;
    }

    private ArrayList<FileItem> fetchMedia(String folderName,Activity activity) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media.DATA+" like?";
        String[] selectionArg = new String[]{"%"+folderName+"%"};
        @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(uri,null,
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

}
