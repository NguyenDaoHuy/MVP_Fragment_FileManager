package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;

public class ImageFilesAdapter extends RecyclerView.Adapter<ImageFilesAdapter.ViewHolder> {

    private ImageFilesInterface imageFilesInterface;

    public ImageFilesAdapter(ImageFilesInterface imageFilesInterface) {
        this.imageFilesInterface = imageFilesInterface;
    }

    @NonNull
    @Override
    public ImageFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_files,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFilesAdapter.ViewHolder holder, int position) {
        FileItem image = imageFilesInterface.image(position);
        holder.imgAnh.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
        holder.tenAnh.setText(image.getDisplayName());
        String size = image.getSize();
        holder.sizeAnh.setText(android.text.format.Formatter.formatFileSize(imageFilesInterface.context(),
                Long.parseLong(size)));
        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFilesInterface.onClickMenu(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFilesInterface.onClickItem(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return imageFilesInterface.onLongClickItem(position,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageFilesInterface.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_image;
        ImageView btnMenu,imgAnh;
        TextView tenAnh,sizeAnh;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            btnMenu = itemView.findViewById(R.id.btnMenu);
            imgAnh =itemView.findViewById(R.id.imgAnh);
            tenAnh = itemView.findViewById(R.id.tenAnh);
            sizeAnh = itemView.findViewById(R.id.sizeAnh);
        }
    }
    public interface ImageFilesInterface{
        int getCount();
        FileItem image (int position);
        void onClickItem(int position);
        boolean onLongClickItem(int position, View v);
        void onClickMenu(int position);
        Context context();
    }
}
