package com.example.filemanagerapp.documents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.adapter.DocumentsFilesAdapter;
import com.example.filemanagerapp.model.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NewFilesFragment extends Fragment implements DocumentsFilesAdapter.DocumentFileInterface {

    private final ArrayList<Item> itemNewFileArrayList = new ArrayList<>();
    private View view;

    public NewFilesFragment(){}
    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.ActivityNewFilesBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_new_files, container, false);
        view = binding.getRoot();
        File dir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        walkdir(dir);
        DocumentsFilesAdapter adapter = new DocumentsFilesAdapter(this);
        binding.rvNewFile.setAdapter(adapter);
        binding.rvNewFile.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        adapter.notifyDataSetChanged();
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public int getCount() {
        if(itemNewFileArrayList == null){
            return 0;
        } else {
            itemNewFileArrayList.size();
        }
        return itemNewFileArrayList.size();
    }

    @Override
    public Item item(int position) {
        return itemNewFileArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(itemNewFileArrayList.get(position).getPath());
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
        int monthNow = Calendar.getInstance().get(Calendar.MONTH) +1;
        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        try{
            if (listFile.length > 0) {
                for (File file : listFile) {
                    if (file.isDirectory()) {
                        walkdir(file);
                    } else {
                        String name = file.getName();
                        String path = file.getPath();
                        Date date = new Date(file.lastModified());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);
                        if (file.getName().endsWith(".docx") ||
                                file.getName().endsWith(".pdf") ||
                                file.getName().endsWith(".txt") ||
                                file.getName().endsWith(".pptx") ||
                                file.getName().endsWith(".xls")) {
                            if (month == monthNow && year == yearNow) {
                                itemNewFileArrayList.add(new Item(name, path));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}