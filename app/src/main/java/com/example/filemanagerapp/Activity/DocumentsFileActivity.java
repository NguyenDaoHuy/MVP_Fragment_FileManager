package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.example.filemanagerapp.Adapter.DocumentsFilesAdapter;
import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.Model.Item;
import com.example.filemanagerapp.R;
import java.io.File;

public class DocumentsFileActivity extends AppCompatActivity implements DocumentsFilesAdapter.DocumentFileInterface {
 //   private ArrayList<Item> itemArrayList = new ArrayList<>();
    private File dir;
    private DocumentsFilesAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_file);
        recyclerView = findViewById(R.id.rvDocuments);
     /*   dir = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory()));
        walkdir(dir); */
        showDocuments();
    }
    private void showDocuments(){
        adapter = new DocumentsFilesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

 /*   public void walkdir(File dir) {
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
                            String date = new Date(listFile[i].lastModified()).toString();
                            String name = listFile[i].getName();
                            String path = listFile[i].getPath();
                            itemArrayList.add(new Item(name,path));
                            System.out.println(date);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }  */

    @Override
    public int getCount() {
        if(MainActivity.itemDocumentsArrayList ==null || MainActivity.itemDocumentsArrayList.size()<0){
            return 0;
        }
        return MainActivity.itemDocumentsArrayList.size();
    }

    @Override
    public Item item(int position) {
        return MainActivity.itemDocumentsArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(MainActivity.itemDocumentsArrayList.get(position).getPath());
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