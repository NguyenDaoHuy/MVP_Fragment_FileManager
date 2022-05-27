package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Activity.AudioPlayerActivity;
import com.example.filemanagerapp.databinding.AudioItemBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;

public class AudioFilesAdapter extends RecyclerView.Adapter<AudioFilesAdapter.ViewHolder> {

    private final AudioFileInterface audioFileInterface;

    public AudioFilesAdapter(AudioFileInterface audioFileInterface) {
        this.audioFileInterface = audioFileInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
         AudioItemBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.audio_item,parent,false);
         return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileItem v = audioFileInterface.audio(position);
        holder.binding.setFileitem(v);
        holder.binding.executePendingBindings();
        holder.itemView.setOnClickListener(v1 -> audioFileInterface.onClickItem(position));
    }

    @Override
    public int getItemCount() {
        return audioFileInterface.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AudioItemBinding binding;
        public ViewHolder(@NonNull AudioItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    public interface AudioFileInterface{
        int getCount();
        FileItem audio (int position);
        void onClickItem(int position);
        Context context();
    }
}
