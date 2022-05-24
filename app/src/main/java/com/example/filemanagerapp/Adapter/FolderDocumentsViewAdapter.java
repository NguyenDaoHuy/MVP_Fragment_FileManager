package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.Activity.DocumentsFileActivity;
import com.example.filemanagerapp.Activity.VideoFilesActivity;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;

import java.util.ArrayList;

public class FolderDocumentsViewAdapter extends RecyclerView.Adapter<FolderDocumentsViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<FileItem> fileItems = new ArrayList<>();
    private ArrayList<String> folderPaths = new ArrayList<>();

    public FolderDocumentsViewAdapter(Context context, ArrayList<FileItem> fileItems, ArrayList<String> folderPaths) {
        this.context = context;
        this.fileItems = fileItems;
        this.folderPaths = folderPaths;
    }

    @NonNull
    @Override
    public FolderDocumentsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderDocumentsViewAdapter.ViewHolder holder, int position) {
        int indexPath = folderPaths.get(position).lastIndexOf("/");
        String nameOFFolder = folderPaths.get(position).substring(indexPath+1);
        holder.folderName.setText(nameOFFolder);
        holder.folderPath.setText(folderPaths.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocumentsFileActivity.class);
                intent.putExtra("folderName",nameOFFolder);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderPaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView folderName,folderPath,folderFiles;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
            folderPath = itemView.findViewById(R.id.folderPath);
            folderFiles = itemView.findViewById(R.id.folderFiles);
        }
    }
}
