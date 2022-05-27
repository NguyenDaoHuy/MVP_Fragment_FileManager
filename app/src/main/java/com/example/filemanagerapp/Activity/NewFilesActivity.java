package com.example.filemanagerapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Adapter.DocumentsFilesAdapter;
import com.example.filemanagerapp.databinding.ActivityNewFilesBinding;
import com.example.filemanagerapp.model.Item;
import com.example.filemanagerapp.R;
import java.io.File;
import java.util.ArrayList;


public class NewFilesActivity extends AppCompatActivity implements DocumentsFilesAdapter.DocumentFileInterface {

    private ArrayList<Item> folderNewFilesList;
    private ActivityNewFilesBinding binding;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_files);
        folderNewFilesList = getIntent().getExtras().getParcelableArrayList("folderNewFilesList");

        DocumentsFilesAdapter adapter = new DocumentsFilesAdapter(this);
        binding.rvNewFile.setAdapter(adapter);
        binding.rvNewFile.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(folderNewFilesList == null){
            return 0;
        } else {
            folderNewFilesList.size();
        }
        return folderNewFilesList.size();
    }

    @Override
    public Item item(int position) {
        return folderNewFilesList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(folderNewFilesList.get(position).getPath());
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