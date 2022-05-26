package com.example.filemanagerapp.Model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private String path;

    public Item(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
