package com.example.filemanagerapp.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.filemanagerapp.Activity.VideoPlayerActivity;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VideoFilesAdapter extends RecyclerView.Adapter<VideoFilesAdapter.ViewHolder> {

    private BottomSheetDialog bottomSheetDialog;
    private ArrayList<FileItem> fileItemArrayList;
    private Context context;

    public VideoFilesAdapter(ArrayList<FileItem> fileItemArrayList, Context context) {
        this.fileItemArrayList = fileItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFilesAdapter.ViewHolder holder, int position) {
            FileItem v = fileItemArrayList.get(position);
            holder.videoName.setText(v.getDisplayName());
            String size = v.getSize();
            holder.video_size.setText(android.text.format.Formatter.formatFileSize(context,
                    Long.parseLong(size)));
            double milliSeconds = Double.parseDouble(v.getDuration());
            holder.video_duration.setText(timeConversion((long) milliSeconds));

            Glide.with(context).load(new File(v.getPath())).into(holder.thumbnail);

            holder.video_menu_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("video_title", fileItemArrayList.get(position).getDisplayName());
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("videoArrayList", fileItemArrayList);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
          holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().add("OPEN");
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("DETAIL");
                popupMenu.getMenu().add("SHARE");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("OPEN")){

                        }
                        if(item.getTitle().equals("DELETE")){
                            AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
                            alerDialog.setTitle("Deleta");
                            alerDialog.setMessage("Do you want to delete this video ?");
                            alerDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                            Long.parseLong(fileItemArrayList.get(position).getId()));
                                    File file = new File(fileItemArrayList.get(position).getPath());
                                    boolean delete = file.delete();
                                    if(delete){
                                        context.getContentResolver().delete(uri,null,null);
                                        fileItemArrayList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, fileItemArrayList.size());
                                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(context,"Can't deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            alerDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alerDialog.show();
                        }
                        if(item.getTitle().equals("DETAIL")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Information");
                            FileItem fileItem = fileItemArrayList.get(position);
                            long longTime = Long.parseLong(fileItem.getDateAdded());
                            builder.setMessage("Name :" + fileItem.getDisplayName() +
                                    "\nSize :" +android.text.format.Formatter.formatFileSize(context,
                                    Long.parseLong(size))+"MB" +
                                    "\nDate :" + convertEpouch(longTime));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog al = builder.create();
                            al.show();
                        }
                        if(item.getTitle().equals("SHARE")){
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            String body = "Download this file";
                            String sub = "http://play.google.com";
                            intent.putExtra(Intent.EXTRA_TEXT,body);
                            intent.putExtra(Intent.EXTRA_TEXT,sub);
                            context.startActivity(Intent.createChooser(intent,"Share using "));
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
          holder.video_menu_more.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                          bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetTheme);
                          View bsView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout,
                                  v.findViewById(R.id.bottom_sheet));
                          bsView.findViewById(R.id.bs_language).setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  holder.itemView.performClick();
                                  bottomSheetDialog.dismiss();
                              }
                          });
                          bottomSheetDialog.setContentView(bsView);
                          bottomSheetDialog.show();
                      }
                  });
    }

    @Override
    public int getItemCount() {
        return fileItemArrayList.size();
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
    }
    public static String convertEpouch(long epouch){
        LocalDate ld = Instant.ofEpochMilli(epouch).atZone(ZoneId.systemDefault()).toLocalDate();
        String str = ld.toString();
        return str;
    }
}
