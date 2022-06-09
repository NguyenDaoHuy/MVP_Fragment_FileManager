package com.example.filemanagerapp.image.imagefolders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.model.FileItem;
import java.util.ArrayList;

public class ImageFolderPresenter implements IImageFolderPresenter {
    private final InterfaceContract.setFileView view;
    private final ArrayList<String> folderImageList = new ArrayList<>();

    public ArrayList<String> getFolderImageList() {
        return folderImageList;
    }

    private final ArrayList<FileItem> fileImageList = new ArrayList<>();

    public ImageFolderPresenter(InterfaceContract.setFileView view,Activity activity) {
        this.view = view;
        inti(activity);
    }
    @Override
    public void inti(Activity activity){
        getFolderImage(activity);
        if(folderImageList!=null){

        }else {
            view.setError("Không có dữ liệu ảnh");
        }
    }

    private void getFolderImage(Activity activity){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(uri,null,null,null,null);
        if(cursor != null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);

                int index = path.lastIndexOf("/");
                String subString = path.substring(0,index);
                if(!folderImageList.contains(subString)){
                    folderImageList.add(subString);
                }
                fileImageList.add(fileItem);
            }while (cursor.moveToNext());
        }
    }
}
