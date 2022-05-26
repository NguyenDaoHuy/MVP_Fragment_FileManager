package com.example.filemanagerapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.R;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder> {

    private final FolderInterface imageFolderInterface;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int indexPath = imageFolderInterface.file(position).lastIndexOf("/");
        String nameOFFolder = imageFolderInterface.file(position).substring(indexPath+1);
        holder.folderName.setText(nameOFFolder);
        holder.folderPath.setText(imageFolderInterface.file(position));

        holder.itemView.setOnClickListener(v -> imageFolderInterface.onClickItem(position));
    }
    @Override
    public int getItemCount() {
        return imageFolderInterface.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView folderName;
        private final TextView folderPath;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
            folderPath = itemView.findViewById(R.id.folderPath);
        }
    }
    public interface FolderInterface{
        int getCount();
        String file(int position);
        void onClickItem(int position);
    }
}