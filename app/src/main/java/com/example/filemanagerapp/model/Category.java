package com.example.filemanagerapp.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import java.io.Serializable;

public class Category implements Serializable{
    private int id;
    private String name;
    private final int icon;
    private final int storage;

    public Category(int id, String name, int icon, int storage) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.storage = storage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public int getStorage() {
        return storage;
    }

    @NonNull
    @Override
    public String toString() {
        return "DanhMuc{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", storage=" + storage +
                '}';
    }
    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView icon_logo, int icon){
        icon_logo.setImageResource(icon);
    }
}
