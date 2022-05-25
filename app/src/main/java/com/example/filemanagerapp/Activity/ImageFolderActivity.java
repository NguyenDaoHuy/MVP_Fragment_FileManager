package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.TextView;
import com.example.filemanagerapp.Adapter.FolderRecyclerViewAdapter;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;

import java.util.ArrayList;

public class ImageFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {
    private RecyclerView recyclerView;
    private TextView tvThongBaoImage;
    private ArrayList<FileItem> fileItems = new ArrayList<>();
    private ArrayList<String> allFolderList = new ArrayList<>();
    private FolderRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder);
        recyclerView = findViewById(R.id.lvFolderImage);
        tvThongBaoImage = findViewById(R.id.tvThongBaoImage);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
        }
        showFolders();
    }
    private void showFolders() {
        fileItems = fetchMedia();
        if(fileItems.size() == 0){
            tvThongBaoImage.setText("Không có dữ liệu");
        }
        adapter = new FolderRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ImageFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }
    public ArrayList<FileItem> fetchMedia(){
        ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
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
                if(!allFolderList.contains(subString)){
                    allFolderList.add(subString);
                    System.out.println("--------------------->"+allFolderList.size());
                }
                fileItemArrayList.add(fileItem);
            }while (cursor.moveToNext());
        }
        return fileItemArrayList;
    }

    @Override
    public int getCount() {
        if(allFolderList==null || allFolderList.size()<0){
            return 0;
        }
        return allFolderList.size();
    }

    @Override
    public String file(int position) {
        return allFolderList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = allFolderList.get(position).lastIndexOf("/");
        String nameOFFolder = allFolderList.get(position).substring(indexPath+1);
        Intent intent = new Intent(this, ImageFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }

    public int getImageFolderSize(){
        return allFolderList.size();
    }
}