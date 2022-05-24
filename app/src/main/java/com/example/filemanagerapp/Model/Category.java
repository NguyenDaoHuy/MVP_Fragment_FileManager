package com.example.filemanagerapp.Model;

import java.io.Serializable;

public class Category implements Serializable{
    private int id;
    private String name;
    private int icon;
    private int storage;
    public Category(){}

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

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "DanhMuc{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", storage=" + storage +
                '}';
    }
}
