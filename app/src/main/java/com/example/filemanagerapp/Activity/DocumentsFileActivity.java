package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.filemanagerapp.Adapter.DocumentsFilesAdapter;
import com.example.filemanagerapp.Model.Item;
import com.example.filemanagerapp.R;
import java.io.File;
import java.util.ArrayList;
public class DocumentsFileActivity extends AppCompatActivity {

    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private File dir;
    private DocumentsFilesAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_file);
        recyclerView = findViewById(R.id.rvDocuments);
        dir = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory()));
        walkdir(dir);
        showDocuments();
    }
    private void showDocuments(){
        adapter = new DocumentsFilesAdapter(this,itemArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    public void walkdir(File dir) {
        File listFile[] = dir.listFiles();
        try{
            if (listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].isDirectory() == true) {
                        walkdir(listFile[i]);
                    }else{
                        if (listFile[i].getName().endsWith(".docx") ||
                                listFile[i].getName().endsWith(".pdf") ||
                                listFile[i].getName().endsWith(".txt") ||
                                listFile[i].getName().endsWith(".pptx") ||
                                listFile[i].getName().endsWith(".xls")) {
                            String name = listFile[i].getName();
                            String path = listFile[i].getPath();
                            itemArrayList.add(new Item(name,path));
                        }
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}