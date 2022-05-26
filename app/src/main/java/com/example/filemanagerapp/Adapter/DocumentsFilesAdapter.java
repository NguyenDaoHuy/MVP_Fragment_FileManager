package com.example.filemanagerapp.Adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Model.Item;
import com.example.filemanagerapp.R;


public class DocumentsFilesAdapter extends RecyclerView.Adapter<DocumentsFilesAdapter.ViewHolder> {

    private DocumentFileInterface documentFileInterface;

    public DocumentsFilesAdapter(DocumentFileInterface documentFileInterface) {
        this.documentFileInterface = documentFileInterface;
    }

    @NonNull
    @Override
    public DocumentsFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsFilesAdapter.ViewHolder holder, int position) {
        Item i = documentFileInterface.item(position);
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
        }else if(i.getName().endsWith(".mp3")){
            holder.imgFolder.setImageResource(R.drawable.icons8_sing);
        }else if(i.getName().endsWith(".jpg")){
            holder.imgFolder.setImageBitmap(BitmapFactory.decodeFile(i.getPath()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    documentFileInterface.onClickItem(position);
                }
        });
    }

    @Override
    public int getItemCount() {
        return documentFileInterface.getCount();
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
    public interface DocumentFileInterface{
        int getCount();
        Item item (int position);
        void onClickItem(int position);
    }
}
