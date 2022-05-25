package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ImagePlayerActivity extends AppCompatActivity {

    ImageView imgView,btnScreenshot,btnShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imgView = findViewById(R.id.imgView);
        btnScreenshot = findViewById(R.id.btnScreenshot);
        btnShare = findViewById(R.id.btnShare);
        getDataImage();
      //  verifyStoragePermission(this);
        btnScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //    takeScreenShot(getWindow().getDecorView().getRootView(),"result");
                takeScreenshot();
            }
        });
        shareButton();
    }
    private void getDataImage(){
        FileItem fileDevices = (FileItem) getIntent().getSerializableExtra("anh");
        String str = fileDevices.getPath();
        imgView.setImageBitmap(BitmapFactory.decodeFile(str));
    }
 /*   public static File takeScreenShot(View view, String fileName){
         Date date = new Date();
         CharSequence format = DateFormat.format("yyyy-MM-dd_hh:mm:ss",date);
         try{
             String dirPath = Environment.getExternalStorageDirectory().toString() + "/learnwithDeeksha";
             File fileDir = new File(dirPath);
             if(!fileDir.exists()){
                 boolean mkdir = fileDir.mkdir();
             }
             String path = dirPath + "/" + fileName + "-" + format +".jpeg";
             view.setDrawingCacheEnabled(true);
             Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
             view.setDrawingCacheEnabled(false);
             File imageFile = new File(path);
             FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
             int quaility = 100;
             bitmap.compress(Bitmap.CompressFormat.JPEG,quaility,fileOutputStream);
             fileOutputStream.flush();
             fileOutputStream.close();
             return imageFile;
         }catch(FileNotFoundException e){
                   e.printStackTrace();
         }catch (IOException e){
             e.printStackTrace();
         }
         return null;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static  String[] PERMISSION_STORAGE ={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static void verifyStoragePermission(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    } */
    private void shareButton(){
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(Intent.ACTION_SEND);
                 intent.setType("text/plain");
                 String body = "Download this file";
                 String sub = "http://play.google.com";
                 intent.putExtra(Intent.EXTRA_TEXT,body);
                 intent.putExtra(Intent.EXTRA_TEXT,sub);
                 startActivity(Intent.createChooser(intent,"Share using "));
            }
        });
    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}