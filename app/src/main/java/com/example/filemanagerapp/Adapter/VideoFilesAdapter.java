package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class VideoFilesAdapter extends RecyclerView.Adapter<VideoFilesAdapter.ViewHolder> {

    private VideoFilesInterface videoFilesInterface;

    public VideoFilesAdapter(VideoFilesInterface videoFilesInterface) {
        this.videoFilesInterface = videoFilesInterface;
    }

    @NonNull
    @Override
    public VideoFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFilesAdapter.ViewHolder holder, int position) {
            FileItem video = videoFilesInterface.file(position);
            holder.videoName.setText(video.getDisplayName());
            String size = video.getSize();
            holder.video_size.setText(android.text.format.Formatter.formatFileSize(videoFilesInterface.context(),
                    Long.parseLong(size)));
            double milliSeconds = Double.parseDouble(video.getDuration());
            holder.video_duration.setText(timeConversion((long) milliSeconds));

            Glide.with(videoFilesInterface.context()).load(new File(video.getPath())).into(holder.thumbnail);

            holder.video_menu_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     videoFilesInterface.onMenuClick(position);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoFilesInterface.onClickItem(position);
                }
            });
          holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                     return videoFilesInterface.onLongClick(position,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoFilesInterface.getCount();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail,video_menu_more;
        TextView video_duration,videoName,video_size;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail =itemView.findViewById(R.id.thumbnail);
            video_menu_more =itemView.findViewById(R.id.video_menu_more);
            video_duration =itemView.findViewById(R.id.video_duration);
            videoName =itemView.findViewById(R.id.videoName);
            video_size =itemView.findViewById(R.id.video_size);
        }
    }
    public String timeConversion(long value){
        String videoTime;
        int duration = (int) value;
        int hrs = (duration/3600000);
        int mns = (duration/60000) % 60000;
        int scs = duration%60000/1000;
        if(hrs > 0){
            videoTime = String.format("%02d:%02d:%02d",hrs,mns,scs);
        }else {
            videoTime = String.format("%02d:%02d",mns,scs);
        }
        return videoTime;
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
        String str = ld.toString();
        return str;
    }
}
