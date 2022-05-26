package com.example.filemanagerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

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
}
