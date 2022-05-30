package com.example.filemanagerapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.databinding.FolderItemBinding;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder> {

    private final FolderInterface imageFolderInterface;

    public FolderRecyclerViewAdapter(FolderInterface imageFolderInterface) {
        this.imageFolderInterface = imageFolderInterface;
    }

    @NonNull
    @Override
    public FolderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FolderItemBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.folder_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int indexPath = imageFolderInterface.file(position).lastIndexOf("/");
        String nameOFFolder = imageFolderInterface.file(position).substring(indexPath+1);
        holder.binding.folderName.setText(nameOFFolder);
        holder.binding.folderPath.setText(imageFolderInterface.file(position));

        holder.itemView.setOnClickListener(v -> imageFolderInterface.onClickItem(position));
    }
    @Override
    public int getItemCount() {
        return imageFolderInterface.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        FolderItemBinding binding;
        public ViewHolder(@NonNull FolderItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public interface FolderInterface{
        int getCount();
        String file(int position);
        void onClickItem(int position);
    }
}