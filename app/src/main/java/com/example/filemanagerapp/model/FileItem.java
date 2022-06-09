package com.example.filemanagerapp.model;

import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;
import com.example.filemanagerapp.audio.audioplayer.AudioPlayerFragment;
import java.io.File;
import java.io.Serializable;

public class FileItem implements Serializable, Parcelable {
    private String id;
    private String title;
    private String displayName;
    private String size;
    private String duration;
    private String path;
    private String dateAdded;

    public FileItem(){

    }

    public FileItem(String id, String title, String displayName, String size, String duration, String path, String dateAdded) {
        this.id = id;
        this.title = title;
        this.displayName = displayName;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.dateAdded = dateAdded;
    }

    protected FileItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        displayName = in.readString();
        size = in.readString();
        duration = in.readString();
        path = in.readString();
        dateAdded = in.readString();
    }

    public static final Creator<FileItem> CREATOR = new Creator<FileItem>() {
        @Override
        public FileItem createFromParcel(Parcel in) {
            return new FileItem(in);
        }

        @Override
        public FileItem[] newArray(int size) {
            return new FileItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(displayName);
        dest.writeString(size);
        dest.writeString(duration);
        dest.writeString(path);
        dest.writeString(dateAdded);
    }
    @BindingAdapter("android:loadImageFile")
    public static void loadImageFile(ImageView imgAnh,String path){
        imgAnh.setImageBitmap(BitmapFactory.decodeFile(path));
    }
    @BindingAdapter("android:loadSizeFile")
    public static void loadSizeFile(TextView sizeAnh, String size){
        sizeAnh.setText(android.text.format.Formatter.formatFileSize(sizeAnh.getContext(),
                Long.parseLong(size)));
    }
    @BindingAdapter("android:loadVideoDuration")
    public static void loadVideoDuration(TextView video_duration, String duration){
        double milliSeconds = Double.parseDouble(duration);
        video_duration.setText(timeConversion((long) milliSeconds));
    }
    public static String timeConversion(long value){
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
    @BindingAdapter("android:loadVideoImage")
    public static void loadVideoImage(ImageView thumbnail, String path){
        Glide.with(thumbnail.getContext()).load(new File(path)).into(thumbnail);
    }
    @BindingAdapter("android:loadAudioTime")
    public static void loadAudioTime(TextView tvTimeAudio, String duration){
        tvTimeAudio.setText(AudioPlayerFragment.convertToMMSS(duration));
    }
}
