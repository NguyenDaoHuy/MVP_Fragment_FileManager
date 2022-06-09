package com.example.filemanagerapp.documents;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.databinding.FolderItemDocumentsBinding;
import com.example.filemanagerapp.model.Item;
import com.example.filemanagerapp.R;


public class DocumentsFilesAdapter extends RecyclerView.Adapter<DocumentsFilesAdapter.ViewHolder> {

    private final DocumentFileInterface documentFileInterface;

    public DocumentsFilesAdapter(DocumentFileInterface documentFileInterface) {
        this.documentFileInterface = documentFileInterface;
    }

    @NonNull
    @Override
    public DocumentsFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
          FolderItemDocumentsBinding folderItemDocumentsBinding = DataBindingUtil.inflate(layoutInflater,R.layout.folder_item_documents,parent,false);
          return new ViewHolder(folderItemDocumentsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item i = documentFileInterface.item(position);
        holder.folderItemDocumentsBinding.setItem(i);
        holder.folderItemDocumentsBinding.executePendingBindings();
        if(i.getName().endsWith(".docx")){
            holder.folderItemDocumentsBinding.imgDocuments.setImageResource(R.drawable.icon_word);
        }else if(i.getName().endsWith(".txt")){
            holder.folderItemDocumentsBinding.imgDocuments.setImageResource(R.drawable.icon_txt);
        }else if(i.getName().endsWith(".xls")){
            holder.folderItemDocumentsBinding.imgDocuments.setImageResource(R.drawable.icon_exel);
        }else if(i.getName().endsWith(".pdf")){
            holder.folderItemDocumentsBinding.imgDocuments.setImageResource(R.drawable.icon_pdf);
        }else if(i.getName().endsWith(".pptx")){
            holder.folderItemDocumentsBinding.imgDocuments.setImageResource(R.drawable.icon_ppt);
        }else if(i.getName().endsWith(".mp3")){
            holder.folderItemDocumentsBinding.imgDocuments.setImageResource(R.drawable.icons8_sing);
        }else if(i.getName().endsWith(".jpg")){
            holder.folderItemDocumentsBinding.imgDocuments.setImageBitmap(BitmapFactory.decodeFile(i.getPath()));
        }

        holder.itemView.setOnClickListener(v -> documentFileInterface.onClickItem(position));
    }

    @Override
    public int getItemCount() {
        return documentFileInterface.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        FolderItemDocumentsBinding folderItemDocumentsBinding;
        public ViewHolder(@NonNull FolderItemDocumentsBinding folderItemDocumentsBinding) {
            super(folderItemDocumentsBinding.getRoot());
            this.folderItemDocumentsBinding = folderItemDocumentsBinding;
        }
    }
    public interface DocumentFileInterface{
        int getCount();
        Item item (int position);
        void onClickItem(int position);
    }
}
