package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.Activity.AudioPlayerActivity;
import com.example.filemanagerapp.Activity.MyMediaPlayer;
import com.example.filemanagerapp.Activity.VideoPlayerActivity;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import java.util.ArrayList;

public class AudioFilesAdapter extends RecyclerView.Adapter<AudioFilesAdapter.ViewHolder> {

    private ArrayList<FileItem> fileItemArrayList;
    private Context context;

    public AudioFilesAdapter(ArrayList<FileItem> fileItemArrayList, Context context) {
        this.fileItemArrayList = fileItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileItem v = fileItemArrayList.get(position);
        holder.audioName.setText(v.getDisplayName());
        String size = v.getSize();
        holder.audio_size.setText(android.text.format.Formatter.formatFileSize(context,
                Long.parseLong(size)));
        holder.tvTimeAudio.setText(AudioPlayerActivity.convertToMMSS(v.getDuration()));

    /*    if(MyMediaPlayer.currentIndex==position){
            holder.audioName.setTextColor(Color.parseColor("#FF0000"));
        }else {
            holder.audioName.setTextColor(Color.parseColor("#000000"));
        }  */

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context, AudioPlayerActivity.class);
                intent.putExtra("LIST",fileItemArrayList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView audioName,audio_size,tvTimeAudio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audioName);
            audio_size = itemView.findViewById(R.id.audio_size);
            tvTimeAudio = itemView.findViewById(R.id.tvTimeAudio);
        }
    }
}
