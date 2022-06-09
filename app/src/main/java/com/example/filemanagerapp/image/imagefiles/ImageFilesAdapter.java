package com.example.filemanagerapp.image.imagefiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.databinding.ItemListFilesBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;

public class ImageFilesAdapter extends RecyclerView.Adapter<ImageFilesAdapter.ViewHolder> {

    private final ImageFilesInterface imageFilesInterface;

    public ImageFilesAdapter(ImageFilesInterface imageFilesInterface) {
        this.imageFilesInterface = imageFilesInterface;
    }

    @NonNull
    @Override
    public ImageFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemListFilesBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.item_list_files,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileItem image = imageFilesInterface.image(position);
        holder.binding.setFileitem(image);
        holder.binding.executePendingBindings();
        holder.binding.btnMenu.setOnClickListener(v -> imageFilesInterface.onClickMenu(position));
        holder.itemView.setOnClickListener(v -> imageFilesInterface.onClickItem(position));
        holder.itemView.setOnLongClickListener(v -> imageFilesInterface.onLongClickItem(position,v));
    }

    @Override
    public int getItemCount() {
        return imageFilesInterface.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemListFilesBinding binding;
        public ViewHolder(@NonNull ItemListFilesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ImageFilesInterface{
        int getCount();
        FileItem image (int position);
        void onClickItem(int position);
        boolean onLongClickItem(int position, View v);
        void onClickMenu(int position);
        Context context();
    }
}
