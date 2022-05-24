package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.filemanagerapp.Activity.VideoFilesActivity;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import java.util.ArrayList;

public class VideoFoldersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FileItem> fileItems;
    private ArrayList<String> folderPaths;

    public VideoFoldersAdapter(Context context, ArrayList<FileItem> fileItems, ArrayList<String> folderPath) {
        this.context = context;
        this.fileItems = fileItems;
        this.folderPaths = folderPath;
    }

    @Override
    public int getCount() {
        return fileItems.size();
    }

    @Override
    public Object getItem(int position) {
        return fileItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.folder_item,parent,false);
        TextView folderName = view.findViewById(R.id.folderName);
        TextView folderPath = view.findViewById(R.id.folderPath);
        TextView folderFiles = view.findViewById(R.id.folderFiles);

        int indexPath = folderPaths.get(position).lastIndexOf("/");
        String nameOFFolder = folderPaths.get(position).substring(indexPath+1);
        folderName.setText(nameOFFolder);
        folderPath.setText(folderPaths.get(position));
        folderFiles.setText("5 video");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoFilesActivity.class);
                intent.putExtra("folderName",nameOFFolder);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
