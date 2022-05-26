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

public class ImageFolderActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.FolderInterface {
    private RecyclerView recyclerView;
    private TextView tvThongBaoImage;
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
        adapter = new FolderRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ImageFolderActivity.this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

 /*   public ArrayList<FileItem> fetchMedia(){
        ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
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
                if(!allFolderList.contains(subString)){
                    allFolderList.add(subString);
                }
                fileItemArrayList.add(fileItem);
            }while (cursor.moveToNext());
        }
        return fileItemArrayList;
    } */

    @Override
    public int getCount() {
        if(MainActivity.imageFolderList==null || MainActivity.imageFolderList.size()<0){
            return 0;
        }
        return MainActivity.imageFolderList.size();
    }

    @Override
    public String file(int position) {
        return MainActivity.imageFolderList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        int indexPath = MainActivity.imageFolderList.get(position).lastIndexOf("/");
        String nameOFFolder = MainActivity.imageFolderList.get(position).substring(indexPath+1);
        Intent intent = new Intent(this, ImageFilesActivity.class);
        intent.putExtra("folderName",nameOFFolder);
        startActivity(intent);
    }
}