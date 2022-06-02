package com.example.filemanagerapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.databinding.ActivityDetailBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;

public class ImagePlayerFragment extends Fragment {

    private ActivityDetailBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_detail,container,false);
        view = binding.getRoot();
        getDataImage();
        binding.btnScreenshot.setOnClickListener(v -> {
          //  takeScreenshot();
        });
        shareButton();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
//                getActivity().onBackPressed();

            }
        });
        return view;
    }

    private void getDataImage(){
        FileItem fileDevices = (FileItem) getArguments().getSerializable("anh");
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
 /*    @SuppressWarnings("deprecation")
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
    } */
}