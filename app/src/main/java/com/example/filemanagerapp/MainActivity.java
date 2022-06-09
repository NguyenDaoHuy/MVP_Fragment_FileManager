package com.example.filemanagerapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.audio.audiofolder.AudioFolderFragment;
import com.example.filemanagerapp.documents.documentfile.DocumentsFileFragment;
import com.example.filemanagerapp.image.imagefolders.ImageFolderFragment;
import com.example.filemanagerapp.documents.ListAppFragment;
import com.example.filemanagerapp.documents.NewFilesFragment;
import com.example.filemanagerapp.video.videofolders.VideoFolderFragment;
import com.example.filemanagerapp.adapter.CategoryAdapter;
import com.example.filemanagerapp.databinding.ActivityMainBinding;
import com.example.filemanagerapp.model.Category;

import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryInterFace {
    private static final int STORAGE_PERMISSION = 100;
    private static final String TAG = "PERMISSON_TAG";
    private ArrayList<Category> categoryArrayList;
    private final Activity mActivity = MainActivity.this;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        displayCaregory();
        connectPermission();
    }

      private void displayCaregory(){
          categoryArrayList = new ArrayList<>();
          categoryArrayList.add(new Category(1,"Images",R.drawable.icons_image,10));
          categoryArrayList.add(new Category(2,"Audio",R.drawable.icons_new_files,10));
          categoryArrayList.add(new Category(3,"Videos",R.drawable.icons_video,10));
          categoryArrayList.add(new Category(4,"Documents",R.drawable.icons8_documents,10));
          categoryArrayList.add(new Category(5,"Apps",R.drawable.icons_android,120));
          categoryArrayList.add(new Category(6,"New files",R.drawable.icon_file,10));
          categoryArrayList.add(new Category(7,"Cloud",R.drawable.icons_cloud,120));
          categoryArrayList.add(new Category(8,"Remote",R.drawable.icons8_remote,120));
          categoryArrayList.add(new Category(9,"Access device",R.drawable.icons_remote,120));
          CategoryAdapter categoryAdapter = new CategoryAdapter(this);
          RecyclerView.LayoutManager layoutManager =new GridLayoutManager(getApplicationContext(),3,RecyclerView.VERTICAL,false);
          binding.rvDanhMuc.setLayoutManager(layoutManager);
          binding.rvDanhMuc.setAdapter(categoryAdapter);
      }

      private void connectPermission(){
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
            getFragment(new ImageFolderFragment());
        }else if(id == 2){
            getFragment(new AudioFolderFragment());
        }else if(id == 3){
            getFragment(new VideoFolderFragment());
        }else if(id == 4){
            getFragment(new DocumentsFileFragment());
        }else if(id == 5){
            getFragment(new ListAppFragment());
        }else if(id == 6){
            getFragment(new NewFilesFragment());
        }else if(id == 7){
            Toast.makeText(this,"Not data",Toast.LENGTH_SHORT).show();
        }else if(id == 8){
            Toast.makeText(this,"Not data",Toast.LENGTH_SHORT).show();
        }else if(id == 9){
            Toast.makeText(this,"Not data",Toast.LENGTH_SHORT).show();
        }
    }
    public void getFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain,fragment);
        fragmentTransaction.commit();
    }

}