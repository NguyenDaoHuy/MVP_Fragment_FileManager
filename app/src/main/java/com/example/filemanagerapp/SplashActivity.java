package com.example.filemanagerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {
    Button btn;
    private static final int STORAGE_PERMISSION = 100;
    private static final String TAG = "PERMISSON_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slap);
        btn = findViewById(R.id.btn);
        if(checkPermission()){
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("App Permission")
                    .setMessage("You must allow this aoo to access files on your device"
                            +"\n\n"+"Now follow the below steps"+"\n\n"+
                            "Open Settings from below button"+"\n"
                            +"Click on Permission"+"\n"+"Allow access for storage")
                    .setPositiveButton("Open Settings", (dialog, which) -> requestPermission()).create().show();
        }
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
                        Toast.makeText(SplashActivity.this,"Manage External Storage Permission is granted",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }else {
                    Log.d(TAG,"onActivityResult: Manage External Storage Permission is denied");
                    Toast.makeText(SplashActivity.this,"Manage External Storage Permission is denied",Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    Log.d(TAG,"onRequestPermissionResult: External Storage permission granted");
                }else {
                    Log.d(TAG,"onRequestPermissionResult: External Storage permission denied");
                    Toast.makeText(SplashActivity.this,"External Storage permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}