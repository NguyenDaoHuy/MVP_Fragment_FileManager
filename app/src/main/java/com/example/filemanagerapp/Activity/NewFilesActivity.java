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
import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.Model.Item;
import com.example.filemanagerapp.R;
import java.io.File;


public class NewFilesActivity extends AppCompatActivity implements DocumentsFilesAdapter.DocumentFileInterface {

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_files);
        RecyclerView recyclerView = findViewById(R.id.rvNewFile);
        DocumentsFilesAdapter adapter = new DocumentsFilesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(MainActivity.getItemNewFileArrayList() == null){
            return 0;
        } else {
            MainActivity.getItemNewFileArrayList().size();
        }
        return MainActivity.getItemNewFileArrayList().size();
    }

    @Override
    public Item item(int position) {
        return MainActivity.getItemNewFileArrayList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(MainActivity.getItemNewFileArrayList().get(position).getPath());
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