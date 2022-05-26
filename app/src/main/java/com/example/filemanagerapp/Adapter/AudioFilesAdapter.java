package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Activity.AudioPlayerActivity;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;

public class AudioFilesAdapter extends RecyclerView.Adapter<AudioFilesAdapter.ViewHolder> {

    private AudioFileInterface audioFileInterface;

    public AudioFilesAdapter(AudioFileInterface audioFileInterface) {
        this.audioFileInterface = audioFileInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileItem v = audioFileInterface.audio(position);
        holder.audioName.setText(v.getDisplayName());
        String size = v.getSize();
        holder.audio_size.setText(android.text.format.Formatter.formatFileSize(audioFileInterface.context(),
                Long.parseLong(size)));
        holder.tvTimeAudio.setText(AudioPlayerActivity.convertToMMSS(v.getDuration()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioFileInterface.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioFileInterface.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView audioName,audio_size,tvTimeAudio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audioName);
            audio_size = itemView.findViewById(R.id.audio_size);
            tvTimeAudio = itemView.findViewById(R.id.tvTimeAudio);
        }
    }

    public interface AudioFileInterface{
        int getCount();
        FileItem audio (int position);
        void onClickItem(int position);
        Context context();
    }
}
