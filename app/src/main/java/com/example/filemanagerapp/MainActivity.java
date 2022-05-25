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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.example.filemanagerapp.Adapter.CategoryAdapter;
import com.example.filemanagerapp.Model.Category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int STORAGE_PERMISSION = 1;
    public static final int REQUEST_PERMISSION_SETTINGS = 12;
    private RecyclerView rvDanhMuc;
    private ArrayList<Category> categoryArrayList;
    private int id = 0;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
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

        categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category(1,"Images",R.drawable.icons_image,getImageSize()));
        categoryArrayList.add(new Category(2,"Audio",R.drawable.icons_new_files,120));
        categoryArrayList.add(new Category(3,"Videos",R.drawable.icons_video,120));
        categoryArrayList.add(new Category(4,"Documents",R.drawable.icons8_documents,120));
        categoryArrayList.add(new Category(5,"Apps",R.drawable.icons_android,120));
        categoryArrayList.add(new Category(6,"New files",R.drawable.icon_file,120));
        categoryArrayList.add(new Category(7,"Cloud",R.drawable.icons_cloud,120));
        categoryArrayList.add(new Category(8,"Remote",R.drawable.icons8_remote,120));
        categoryArrayList.add(new Category(9,"Access from device",R.drawable.icons_remote,120));
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryArrayList,MainActivity.this);
        RecyclerView.LayoutManager layoutManager =new GridLayoutManager(getApplicationContext(),3,RecyclerView.VERTICAL,false);
        rvDanhMuc.setLayoutManager(layoutManager);
        rvDanhMuc.setAdapter(categoryAdapter);
    }
    private int getImageSize(){
        int size = 0;
        size = getIntent().getIntExtra("imageSize",5);
        return size;
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
}