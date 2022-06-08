package com.example.filemanagerapp.documents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.adapter.DocumentsFilesAdapter;
import com.example.filemanagerapp.databinding.ActivityDocumentsFileBinding;
import com.example.filemanagerapp.model.Item;
import com.example.filemanagerapp.R;
import java.io.File;
import java.util.ArrayList;

public class DocumentsFileFragment extends Fragment implements DocumentsFilesAdapter.DocumentFileInterface {
    private final ArrayList<Item> itemDocumentsArrayList = new ArrayList<>();
    private ActivityDocumentsFileBinding binding;
    private View view;
    public DocumentsFileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_documents_file,container,false);
        view = binding.getRoot();
        File dir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        walkdir(dir);
        showDocuments();
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        });
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDocuments(){
        DocumentsFilesAdapter adapter = new DocumentsFilesAdapter(this);
        binding.rvDocuments.setAdapter(adapter);
        binding.rvDocuments.setLayoutManager(new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        itemDocumentsArrayList.size();
        return itemDocumentsArrayList.size();
    }

    @Override
    public Item item(int position) {
        return itemDocumentsArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(itemDocumentsArrayList.get(position).getPath());
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        Uri photoURI = FileProvider.getUriForFile(view.getContext(), view.getContext().getPackageName() + ".provider", file);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            intent.setDataAndType(photoURI, "text/*");
        } else {
            intent.setDataAndType(photoURI, mimetype);
        }
        startActivity(intent);
    }

    public void walkdir(File dir) {
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
}