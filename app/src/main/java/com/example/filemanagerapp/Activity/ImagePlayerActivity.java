package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.example.filemanagerapp.databinding.ActivityDetailBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ImagePlayerActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        getDataImage();
        binding.btnScreenshot.setOnClickListener(v -> {
            takeScreenshot();
        });
        shareButton();
    }
    private void getDataImage(){
        FileItem fileDevices = (FileItem) getIntent().getSerializableExtra("anh");
        String str = fileDevices.getPath();
        binding.imgView.setImageBitmap(BitmapFactory.decodeFile(str));
    }
    private void shareButton(){
        binding.btnShare.setOnClickListener(v -> {
             Intent intent = new Intent(Intent.ACTION_SEND);
             intent.setType("text/plain");
             String body = "Download this file";
             String sub = "http://play.google.com";
             intent.putExtra(Intent.EXTRA_TEXT,body);
             intent.putExtra(Intent.EXTRA_TEXT,sub);
             startActivity(Intent.createChooser(intent,"Share using "));
        });
    }
    @SuppressWarnings("deprecation")
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