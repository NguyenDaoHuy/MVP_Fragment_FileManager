package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.example.filemanagerapp.Adapter.DocumentsFilesAdapter;
import com.example.filemanagerapp.Model.Item;
import com.example.filemanagerapp.R;
import java.io.File;
import java.util.ArrayList;

public class DocumentsFileActivity extends AppCompatActivity implements DocumentsFilesAdapter.DocumentFileInterface {
    ArrayList<Item> folderDocumentsList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_file);
        recyclerView = findViewById(R.id.rvDocuments);
        folderDocumentsList = getIntent().getExtras().getParcelableArrayList("folderDocumentsList");
        showDocuments();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showDocuments(){
        DocumentsFilesAdapter adapter = new DocumentsFilesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(folderDocumentsList == null){
            return 0;
        } else {
            folderDocumentsList.size();
        }
        return folderDocumentsList.size();
    }

    @Override
    public Item item(int position) {
        return folderDocumentsList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(folderDocumentsList.get(position).getPath());
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