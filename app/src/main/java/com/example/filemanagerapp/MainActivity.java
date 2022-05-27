package com.example.filemanagerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.Activity.AudioFolderActivity;
import com.example.filemanagerapp.Activity.DocumentsFileActivity;
import com.example.filemanagerapp.Activity.ImageFolderActivity;
import com.example.filemanagerapp.Activity.ListAppActivity;
import com.example.filemanagerapp.Activity.NewFilesActivity;
import com.example.filemanagerapp.Activity.VideoFolderActivity;
import com.example.filemanagerapp.Adapter.CategoryAdapter;
import com.example.filemanagerapp.model.Category;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.model.Item;
import com.example.filemanagerapp.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class    MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryInterFace {
    private static final int STORAGE_PERMISSION = 100;
    private static final String TAG = "PERMISSON_TAG";
    private ArrayList<Category> categoryArrayList;
    private final ArrayList<Item> itemDocumentsArrayList = new ArrayList<>();
    private final ArrayList<Item> itemNewFileArrayList = new ArrayList<>();
    private final ArrayList<FileItem> itemImageArrayList = new ArrayList<>();
    private final ArrayList<String> imageFolderList = new ArrayList<>();
    private final ArrayList<FileItem> itemVideoArrayList = new ArrayList<>();
    private final ArrayList<String> videoFolderList = new ArrayList<>();
    private final ArrayList<FileItem> itemAudioArrayList = new ArrayList<>();
    private final ArrayList<String> audioFolderList = new ArrayList<>();
    private final Activity mActivity = MainActivity.this;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        if(checkPermission()){

        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("App Permission")
                    .setMessage("You must allow this aoo to access files on your device"
                            +"\n\n"+"Now follow the below steps"+"\n\n"+
                            "Open Settings from below button"+"\n"
                            +"Click on Permission"+"\n"+"Allow access for storage")
                    .setPositiveButton("Open Settings", (dialog, which) -> requestPermission()).create().show();
        }

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        File dir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        walkdir(dir);
        getFolderImage();
        getFolderAudio();
        getFolderVideo();

        categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category(1,"Images",R.drawable.icons_image,imageFolderList.size()));
        categoryArrayList.add(new Category(2,"Audio",R.drawable.icons_new_files,audioFolderList.size()));
        categoryArrayList.add(new Category(3,"Videos",R.drawable.icons_video,videoFolderList.size()));
        categoryArrayList.add(new Category(4,"Documents",R.drawable.icons8_documents,itemDocumentsArrayList.size()));
        categoryArrayList.add(new Category(5,"Apps",R.drawable.icons_android,120));
        categoryArrayList.add(new Category(6,"New files",R.drawable.icon_file,itemNewFileArrayList.size()));
        categoryArrayList.add(new Category(7,"Cloud",R.drawable.icons_cloud,120));
        categoryArrayList.add(new Category(8,"Remote",R.drawable.icons8_remote,120));
        categoryArrayList.add(new Category(9,"Access device",R.drawable.icons_remote,120));
        CategoryAdapter categoryAdapter = new CategoryAdapter(this);
        RecyclerView.LayoutManager layoutManager =new GridLayoutManager(getApplicationContext(),3,RecyclerView.VERTICAL,false);
        binding.rvDanhMuc.setLayoutManager(layoutManager);
        binding.rvDanhMuc.setAdapter(categoryAdapter);
    }

      private void requestPermission(){
          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
              try {
                  Log.d(TAG, "requestPermission: try");
                  Intent intent = new Intent();
                  intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                  Uri uri = Uri.fromParts("package", this.getPackageName(),null);
                  intent.setData(uri);
                  storageActivityResultLauncher.launch(intent);
              }catch (Exception e){
                  Log.d(TAG, "requestPermission: catch");
                  Intent intent = new Intent();
                  intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                  storageActivityResultLauncher.launch(intent);
              }
          }else {
              ActivityCompat.requestPermissions(this,
                      new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                      STORAGE_PERMISSION);
          }
      }

      private final ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(
              new ActivityResultContracts.StartActivityForResult(),
              result -> {
                  Log.d(TAG,"onActivityResult: ");
                  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                       if(Environment.isExternalStorageManager()){
                            Log.d(TAG,"onActivityResult: Manage External Storage Permission is granted");
                            Toast.makeText(MainActivity.this,"Manage External Storage Permission is granted",Toast.LENGTH_SHORT).show();
                            //hàm
                            restartActivity(mActivity);
                       }
                  }else {
                      Log.d(TAG,"onActivityResult: Manage External Storage Permission is denied");
                      Toast.makeText(MainActivity.this,"Manage External Storage Permission is denied",Toast.LENGTH_SHORT).show();
                  }
              }
      );

      public boolean checkPermission(){
          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                return Environment.isExternalStorageManager();
          }else {
              int write = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
              int read = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
              return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
          }
      }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION){
            if(grantResults.length > 0){
                 boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(write){
                       // hàm
                      restartActivity(mActivity);
                      Log.d(TAG,"onRequestPermissionResult: External Storage permission granted");
                 }else {
                     Log.d(TAG,"onRequestPermissionResult: External Storage permission denied");
                     Toast.makeText(MainActivity.this,"External Storage permission denied",Toast.LENGTH_SHORT).show();
                 }
            }
        }
    }
    public static void restartActivity(Activity activity) {
        activity.recreate();
    }

    @Override
    public int getCount() {
        if(categoryArrayList == null){
            return 0;
        } else {
            categoryArrayList.size();
        }
        return categoryArrayList.size();
    }

    @Override
    public Category category(int position) {
        return categoryArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Category category = categoryArrayList.get(position);
        int id = category.getId();
        if(id == 1){
            Intent intent = new Intent(this, ImageFolderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("folderImageList", imageFolderList);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }else if(id == 2){
            Intent intent = new Intent(this, AudioFolderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("folderAudioList", audioFolderList);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }else if(id == 3){
            Intent intent = new Intent(this, VideoFolderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("folderVideoList", videoFolderList);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }else if(id == 4){
            Intent intent = new Intent(this, DocumentsFileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("folderDocumentsList",itemDocumentsArrayList);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }else if(id == 5){
            Intent intent = new Intent(this, ListAppActivity.class);
            this.startActivity(intent);
        }else if(id == 6){
            Intent intent = new Intent(this, NewFilesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("folderNewFilesList",itemNewFileArrayList);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }else if(id == 7){
            Toast.makeText(this,"Not data",Toast.LENGTH_SHORT).show();
        }else if(id == 8){
            Toast.makeText(this,"Not data",Toast.LENGTH_SHORT).show();
        }else if(id == 9){
            Toast.makeText(this,"Not data",Toast.LENGTH_SHORT).show();
        }
    }
    public void walkdir(File dir) {
        File[] listFile = dir.listFiles();
        int monthNow = Calendar.getInstance().get(Calendar.MONTH) +1;
        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        try{
            if (listFile.length > 0) {
                for (File file : listFile) {
                    if (file.isDirectory()) {
                        walkdir(file);
                    } else {
                        String name = file.getName();
                        String path = file.getPath();
                        Date date = new Date(file.lastModified());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);

                        if (file.getName().endsWith(".docx") ||
                                file.getName().endsWith(".pdf") ||
                                file.getName().endsWith(".txt") ||
                                file.getName().endsWith(".pptx") ||
                                file.getName().endsWith(".xls")) {
                            if (month == monthNow && year == yearNow) {
                                itemNewFileArrayList.add(new Item(name, path));
                            }
                            itemDocumentsArrayList.add(new Item(name, path));
                            System.out.println(date);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getFolderImage(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri,null,null,null,null);
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
                if(!imageFolderList.contains(subString)){
                    imageFolderList.add(subString);
                }
                itemImageArrayList.add(fileItem);
            }while (cursor.moveToNext());
        }
    }
    public void getFolderAudio(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri,null,null,null,null);
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
                if(!audioFolderList.contains(subString)){
                    audioFolderList.add(subString);
                }
                itemAudioArrayList.add(fileItem);
            }while (cursor.moveToNext());
        }
    }
    public void getFolderVideo(){
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri,null,null,null,null);
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
                if(!videoFolderList.contains(subString)){
                    videoFolderList.add(subString);
                }
                itemVideoArrayList.add(fileItem);
            }while (cursor.moveToNext());

        }
    }
}