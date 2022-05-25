package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.Model.Item;
import com.example.filemanagerapp.R;

import java.io.File;
import java.util.ArrayList;

public class DocumentsFilesAdapter extends RecyclerView.Adapter<DocumentsFilesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Item> fileItems = new ArrayList<>();

    public DocumentsFilesAdapter(Context context, ArrayList<Item> fileItems) {
        this.context = context;
        this.fileItems = fileItems;
    }

    @NonNull
    @Override
    public DocumentsFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsFilesAdapter.ViewHolder holder, int position) {
        Item i = fileItems.get(position);
        holder.documentsName.setText(i.getName());
        holder.documentsPath.setText(i.getPath());
        if(i.getName().endsWith(".docx")){
            holder.imgFolder.setImageResource(R.drawable.icon_word);
        }else if(i.getName().endsWith(".txt")){
            holder.imgFolder.setImageResource(R.drawable.icon_txt);
        }else if(i.getName().endsWith(".xls")){
            holder.imgFolder.setImageResource(R.drawable.icon_exel);
        }else if(i.getName().endsWith(".pdf")){
            holder.imgFolder.setImageResource(R.drawable.icon_pdf);
        }else if(i.getName().endsWith(".pptx")){
            holder.imgFolder.setImageResource(R.drawable.icon_ppt);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        File file = new File(Environment.getExternalStorageDirectory(), i.getName());
                        Uri uri = FileProvider.getUriForFile(context, "com.example.mayzom", file);
                        intent.setData(uri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        try {
                            context.startActivity(Intent.createChooser(intent, "Open Document"));
                        } catch (Exception e) {
                            Toast.makeText(context, "Error: No app found to open Word document.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Unable to open", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }

    @Override
    public int getItemCount() {
        return fileItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView documentsName,documentsPath;
        private ImageView imgFolder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            documentsName = itemView.findViewById(R.id.folderName);
            documentsPath = itemView.findViewById(R.id.folderPath);
            imgFolder = itemView.findViewById(R.id.imgFolder);
        }
    }
}
