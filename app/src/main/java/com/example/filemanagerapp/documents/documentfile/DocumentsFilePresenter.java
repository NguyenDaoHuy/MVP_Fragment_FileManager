package com.example.filemanagerapp.documents.documentfile;

import android.os.Environment;

import com.example.filemanagerapp.model.Item;

import java.io.File;
import java.util.ArrayList;

public class DocumentsFilePresenter implements IDocumentsFilePresenter{
    private final ArrayList<Item> itemDocumentsArrayList = new ArrayList<>();
    private IDocumentsFileView view;

    public DocumentsFilePresenter(IDocumentsFileView view) {
        this.view = view;
        inti();
    }

    @Override
    public void inti(){
        File dir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        walkdir(dir);
        if(itemDocumentsArrayList!=null){

        }else {
            view.setError("Không có dữ liệu ảnh");
        }
    }

    private void walkdir(File dir) {
        File[] listFile = dir.listFiles();
        try{
            if (listFile.length > 0) {
                for (File file : listFile) {
                    if (file.isDirectory()) {
                        walkdir(file);
                    } else {
                        String name = file.getName();
                        String path = file.getPath();
                        if (file.getName().endsWith(".docx") ||
                                file.getName().endsWith(".pdf") ||
                                file.getName().endsWith(".txt") ||
                                file.getName().endsWith(".pptx") ||
                                file.getName().endsWith(".xls")) {
                            itemDocumentsArrayList.add(new Item(name, path));
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Item> getItemDocumentsArrayList() {
        return itemDocumentsArrayList;
    }
}
