package com.example.filemanagerapp.image.imageplayer;

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
import com.example.filemanagerapp.databinding.ActivityDetailBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;

public class ImagePlayerFragment extends Fragment implements IImagePlayerView.PlayerImageView{

    private ActivityDetailBinding binding;
    public static final String TAG = ImagePlayerFragment.class.getName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_detail,container,false);
        View view = binding.getRoot();
        getDataImage();
        binding.btnScreenshot.setOnClickListener(v -> {

        });
        shareButton();
        binding.btnBack.setOnClickListener(v -> {
            if(getFragmentManager() != null){
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }
    @Override
    public void getDataImage(){
        FileItem fileDevices = (FileItem) getArguments().getSerializable("anh");
        String str = fileDevices.getPath();
        binding.imgView.setImageBitmap(BitmapFactory.decodeFile(str));
    }
    @Override
    public void shareButton(){
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
}