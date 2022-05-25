package com.example.filemanagerapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.R;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder> {

    FolderInterface imageFolderInterface;

    public FolderRecyclerViewAdapter(FolderInterface imageFolderInterface) {
        this.imageFolderInterface = imageFolderInterface;
    }

    @NonNull
    @Override
    public FolderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderRecyclerViewAdapter.ViewHolder holder, int position) {
        int indexPath = imageFolderInterface.file(position).lastIndexOf("/");
        String nameOFFolder = imageFolderInterface.file(position).substring(indexPath+1);
        holder.folderName.setText(nameOFFolder);
        holder.folderPath.setText(imageFolderInterface.file(position));
        holder.folderFiles.setText("5 Image");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFolderInterface.onClickItem(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return imageFolderInterface.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView folderName,folderPath,folderFiles;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
            folderPath = itemView.findViewById(R.id.folderPath);
            folderFiles = itemView.findViewById(R.id.folderFiles);
        }
    }
    public interface FolderInterface{
        int getCount();
        String file(int position);
        void onClickItem(int position);
    }
}