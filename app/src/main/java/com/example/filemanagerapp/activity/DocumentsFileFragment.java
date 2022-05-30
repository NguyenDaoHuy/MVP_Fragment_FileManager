package com.example.filemanagerapp.activity;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.filemanagerapp.adapter.DocumentsFilesAdapter;
import com.example.filemanagerapp.databinding.ActivityDocumentsFileBinding;
import com.example.filemanagerapp.model.Item;
import com.example.filemanagerapp.R;
import java.io.File;
import java.util.ArrayList;

public class DocumentsFileFragment extends Fragment implements DocumentsFilesAdapter.DocumentFileInterface {
    private ArrayList<Item> folderDocumentsList;
    private ActivityDocumentsFileBinding binding;
    private View view;


    public static DocumentsFileFragment newInstance() {
        Bundle args = new Bundle();
        DocumentsFileFragment fragment = new DocumentsFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_documents_file,container,false);
        view = binding.getRoot();
     //   folderDocumentsList = getIntent().getExtras().getParcelableArrayList("folderDocumentsList");
        folderDocumentsList = (ArrayList<Item>) getArguments().getSerializable("folderDocumentsList");
        showDocuments();
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
        Uri photoURI = FileProvider.getUriForFile(view.getContext(), view.getContext().getPackageName() + ".provider", file);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            intent.setDataAndType(photoURI, "text/*");
        } else {
            intent.setDataAndType(photoURI, mimetype);
        }
        startActivity(intent);
    }
}