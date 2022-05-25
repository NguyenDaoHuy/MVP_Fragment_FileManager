package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import com.example.filemanagerapp.Adapter.DocumentsFilesAdapter;
import com.example.filemanagerapp.Model.Item;
import com.example.filemanagerapp.R;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewFilesActivity extends AppCompatActivity implements DocumentsFilesAdapter.DocumentFileInterface {
    private RecyclerView recyclerView;
    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private File dir;
    private DocumentsFilesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_files);
        recyclerView = findViewById(R.id.rvNewFile);
        dir = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory()));
        walkdir(dir);
        adapter = new DocumentsFilesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }
    public void walkdir(File dir) {
        int monthNow = Calendar.getInstance().get(Calendar.MONTH) +1;
        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        File listFile[] = dir.listFiles();
        try{
            if (listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].isDirectory() == true) {
                        walkdir(listFile[i]);
                    }else{
                            Date date = new Date(listFile[i].lastModified());
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            int month = cal.get(Calendar.MONTH) +1;
                            int year = cal.get(Calendar.YEAR);
                            String name = listFile[i].getName();
                            String path = listFile[i].getPath();
                        if (listFile[i].getName().endsWith(".docx") ||
                                listFile[i].getName().endsWith(".pdf") ||
                                listFile[i].getName().endsWith(".txt") ||
                                listFile[i].getName().endsWith(".pptx") ||
                                listFile[i].getName().endsWith(".xls")) {
                            if(month == monthNow && year == yearNow){
                                itemArrayList.add(new Item(name,path));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        if(itemArrayList==null || itemArrayList.size()<0){
            return 0;
        }
        return itemArrayList.size();
    }

    @Override
    public Item item(int position) {
        return itemArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(itemArrayList.get(position).getPath());
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            intent.setDataAndType(photoURI, "text/*");
        } else {
            intent.setDataAndType(photoURI, mimetype);
        }
        startActivity(intent);
    }
}