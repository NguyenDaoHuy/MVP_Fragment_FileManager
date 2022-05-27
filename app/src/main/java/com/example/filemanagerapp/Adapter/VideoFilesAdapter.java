package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.databinding.VideoItemBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class VideoFilesAdapter extends RecyclerView.Adapter<VideoFilesAdapter.ViewHolder> {

    private final VideoFilesInterface videoFilesInterface;

    public VideoFilesAdapter(VideoFilesInterface videoFilesInterface) {
        this.videoFilesInterface = videoFilesInterface;
    }

    @NonNull
    @Override
    public VideoFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
         VideoItemBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.video_item,parent,false);
         return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FileItem video = videoFilesInterface.file(position);
            holder.binding.setFileitem(video);
            holder.binding.executePendingBindings();

            holder.binding.btnMenu2.setOnClickListener(v -> videoFilesInterface.onMenuClick(position));
            holder.itemView.setOnClickListener(v -> videoFilesInterface.onClickItem(position));
            holder.itemView.setOnLongClickListener(v -> videoFilesInterface.onLongClick(position,v));
    }

    @Override
    public int getItemCount() {
        return videoFilesInterface.getCount();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        VideoItemBinding binding;
        public ViewHolder(@NonNull VideoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface VideoFilesInterface{
         int getCount();
         FileItem file (int position);
         void onClickItem(int position);
         boolean onLongClick(int position,View v);
         void onMenuClick(int position);
         Context context();
    }
    public static String convertEpouch(long epouch){
        LocalDate ld = Instant.ofEpochMilli(epouch).atZone(ZoneId.systemDefault()).toLocalDate();
        return ld.toString();
    }
}
