package com.example.filemanagerapp.activity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.adapter.VideoFilesAdapter;
import com.example.filemanagerapp.databinding.ActivityVideoFilesBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;

public class VideoFilesActivity extends AppCompatActivity implements VideoFilesAdapter.VideoFilesInterface {

    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private VideoFilesAdapter videoFilesAdapter;
    private String folder_name;
    private BottomSheetDialog bottomSheetDialog;
    private ActivityVideoFilesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video_files);
        folder_name = getIntent().getStringExtra("folderName");
        showVideoFile();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showVideoFile() {
        fileItemArrayList = fetchMedia(folder_name);
        videoFilesAdapter = new VideoFilesAdapter(this);
        binding.videoRv.setAdapter(videoFilesAdapter);
        binding.videoRv.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL,false));
        videoFilesAdapter.notifyDataSetChanged();
    }

    private ArrayList<FileItem> fetchMedia(String folderName) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA+" like?";
        String[] selectionArg = new String[]{"%"+folderName+"%"};
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri,null,
                selection,selectionArg,null);
        if(cursor!=null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);
                fileItems.add(fileItem);
            }while(cursor.moveToNext());
        }
        return fileItems;
    }

    @Override
    public int getCount() {
        if(fileItemArrayList == null){
            return 0;
        } else {
            fileItemArrayList.size();
        }
        return fileItemArrayList.size();
    }

    @Override
    public FileItem file(int position) {
        return fileItemArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("video_title", fileItemArrayList.get(position).getDisplayName());
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("videoArrayList", fileItemArrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(int position, View v) {
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.getMenu().add("OPEN");
        popupMenu.getMenu().add("DELETE");
        popupMenu.getMenu().add("DETAIL");
        popupMenu.getMenu().add("SHARE");
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getTitle().equals("OPEN")){
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("video_title", fileItemArrayList.get(position).getDisplayName());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videoArrayList", fileItemArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            if(item.getTitle().equals("DELETE")){
                AlertDialog.Builder alerDialog = new AlertDialog.Builder(VideoFilesActivity.this);
                alerDialog.setTitle("Deleta");
                alerDialog.setMessage("Do you want to delete this video ?");
                alerDialog.setPositiveButton("Delete", (dialog, which) -> {
                    Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            Long.parseLong(fileItemArrayList.get(position).getId()));
                    File file = new File(fileItemArrayList.get(position).getPath());
                    boolean delete = file.delete();
                    if(delete){
                        getContentResolver().delete(uri,null,null);
                        fileItemArrayList.remove(position);
                        videoFilesAdapter.notifyItemRemoved(position);
                        videoFilesAdapter.notifyItemRangeChanged(position,fileItemArrayList.size());
                        Toast.makeText(VideoFilesActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(VideoFilesActivity.this,"Can't deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                alerDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                alerDialog.show();
            }
            if(item.getTitle().equals("DETAIL")){
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoFilesActivity.this);
                builder.setTitle("Information");
                FileItem file = fileItemArrayList.get(position);
                long longTime = Long.parseLong(file.getDateAdded()) * 1000;
                builder.setMessage("Name :" + file.getDisplayName() +
                        "\nSize :" + android.text.format.Formatter.formatFileSize(getApplicationContext(),
                        Long.parseLong(fileItemArrayList.get(position).getSize())) +
                        "\nDate :" + VideoFilesAdapter.convertEpouch(longTime));
                builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
                AlertDialog al = builder.create();
                al.show();
            }
            if(item.getTitle().equals("SHARE")){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Download this file";
                String sub = "http://play.google.com";
                intent.putExtra(Intent.EXTRA_TEXT,body);
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent,"Share using "));
            }
            return true;
        });
        popupMenu.show();
            return true;
    }

    @Override
    public void onMenuClick(int position) {
        bottomSheetDialog = new BottomSheetDialog(VideoFilesActivity.this,R.style.BottomSheetTheme);
        View bsView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout,
                findViewById(R.id.bottom_sheet));
        bsView.findViewById(R.id.bs_language).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(bsView);
        bottomSheetDialog.show();
    }

    @Override
    public Context context() {
        return getApplicationContext();
    }

}