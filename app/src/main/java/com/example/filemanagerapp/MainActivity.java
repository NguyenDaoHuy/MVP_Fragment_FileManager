package com.example.filemanagerapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;
import com.example.filemanagerapp.Activity.AudioFolderActivity;
import com.example.filemanagerapp.Activity.DocumentsFileActivity;
import com.example.filemanagerapp.Activity.ImageFolderActivity;
import com.example.filemanagerapp.Activity.ListAppActivity;
import com.example.filemanagerapp.Activity.NewFilesActivity;
import com.example.filemanagerapp.Activity.VideoFolderActivity;
import com.example.filemanagerapp.Adapter.CategoryAdapter;
import com.example.filemanagerapp.Model.Category;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.Model.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryInterFace {
    public static final int STORAGE_PERMISSION = 1;
    public static final int REQUEST_PERMISSION_SETTINGS = 12;
    private RecyclerView rvDanhMuc;
    private ArrayList<Category> categoryArrayList;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public static ArrayList<Item> itemDocumentsArrayList = new ArrayList<>();
    public static ArrayList<Item> itemNewFileArrayList = new ArrayList<>();
    public static ArrayList<FileItem> itemImageArrayList = new ArrayList<>();
    public static ArrayList<String> imageFolderList = new ArrayList<>();
    public static ArrayList<FileItem> itemVideoArrayList = new ArrayList<>();
    public static ArrayList<String> videoFolderList = new ArrayList<>();
    public static ArrayList<FileItem> itemAudioArrayList = new ArrayList<>();
    public static ArrayList<String> audioFolderList = new ArrayList<>();
    private File dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvDanhMuc = findViewById(R.id.rvDanhMuc);
        drawerLayout = findViewById(R.id.drawer_layout);

        applyForPermission();
        SharedPreferences preferences = getSharedPreferences("AllowAccess",MODE_PRIVATE);
        String value = preferences.getString("Allow","OK");
        if(value.equals("OK")){
            Toast.makeText(MainActivity.this,"OK",Toast.LENGTH_SHORT).show();
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Allow","OK");
            editor.apply();
        }
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        dir = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory()));
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
        rvDanhMuc.setLayoutManager(layoutManager);
        rvDanhMuc.setAdapter(categoryAdapter);
    }
    private void applyForPermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE },STORAGE_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_PERMISSION){
            for (int i = 0; i < permissions.length; i++) {
                String per = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(per);
                    if (!showRationale) {
                        //user clicked on never ask again
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("App Permission")
                                .setMessage("You must allow this aoo to access files on your device"
                                +"\n\n"+"Now follow the below steps"+"\n\n"+
                                        "Open Settings from below button"+"\n"
                                +"Click on Permission"+"\n"+"Allow access for storage")
                                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",getPackageName(),null);
                                        intent.setData(uri);
                                        startActivityForResult(intent,REQUEST_PERMISSION_SETTINGS);
                                    }
                                }).create().show();
                    }
                    else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                              new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                }
                } else {
                    Toast.makeText(MainActivity.this,"OK",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
        }
    }

    @Override
    public int getCount() {
        if(categoryArrayList==null || categoryArrayList.size()<0){
            return 0;
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
            this.startActivity(intent);
        }else if(id == 2){
            Intent intent = new Intent(this, AudioFolderActivity.class);
            this.startActivity(intent);
        }else if(id == 3){
            Intent intent = new Intent(this, VideoFolderActivity.class);
            this.startActivity(intent);
        }else if(id == 4){
            Intent intent = new Intent(this, DocumentsFileActivity.class);
            this.startActivity(intent);
        }else if(id == 5){
            Intent intent = new Intent(this, ListAppActivity.class);
            this.startActivity(intent);
        }else if(id == 6){
            Intent intent = new Intent(this, NewFilesActivity.class);
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
        File listFile[] = dir.listFiles();
        int monthNow = Calendar.getInstance().get(Calendar.MONTH) +1;
        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        try{
            if (listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].isDirectory() == true) {
                        walkdir(listFile[i]);
                    } else {
                        String name = listFile[i].getName();
                        String path = listFile[i].getPath();
                        Date date = new Date(listFile[i].lastModified());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);

                        if (listFile[i].getName().endsWith(".docx") ||
                                listFile[i].getName().endsWith(".pdf") ||
                                listFile[i].getName().endsWith(".txt") ||
                                listFile[i].getName().endsWith(".pptx") ||
                                listFile[i].getName().endsWith(".xls")) {
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
                if(!imageFolderList.contains(subString)){
                    imageFolderList.add(subString);
                }
                itemImageArrayList.add(fileItem);
            }while (cursor.moveToNext());
        }
    }
    public void getFolderAudio(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
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
                if(!videoFolderList.contains(subString)){
                    videoFolderList.add(subString);
                }
                itemVideoArrayList.add(fileItem);
            }while (cursor.moveToNext());

        }
    }
}