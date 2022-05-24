package com.example.filemanagerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.Activity.AudioFolderActivity;
import com.example.filemanagerapp.Activity.DocumentsFolderActivity;
import com.example.filemanagerapp.Activity.ImageFolderActivity;
import com.example.filemanagerapp.Activity.VideoFolderActivity;
import com.example.filemanagerapp.Model.Category;
import com.example.filemanagerapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHoder> {

    private ArrayList<Category> categoryArrayList;
    private Context context;
    private ImageView icon_logo;
    private TextView tvTenDanhMuc,tvDungLuong;

    public CategoryAdapter(ArrayList<Category> categoryArrayList, Context context) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_danh_muc,parent,false);
        ViewHoder viewHoder = new ViewHoder(view);
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHoder holder, int position) {
       Category category = categoryArrayList.get(position);
       icon_logo.setImageResource(category.getIcon());
       tvTenDanhMuc.setText(category.getName());
       String dungLuong = String.valueOf(category.getStorage());
       tvDungLuong.setText(dungLuong);
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder{
           public ViewHoder(@NonNull View view){
                super(view);
                icon_logo = view.findViewById(R.id.icon_logo);
                tvTenDanhMuc = view.findViewById(R.id.tvTenDanhMuc);
                tvDungLuong = view.findViewById(R.id.tvDungLuong);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Category category = categoryArrayList.get(getPosition());
                        int id = category.getId();
                        if(id == 1){
                            Intent intent = new Intent(context, ImageFolderActivity.class);
                            context.startActivity(intent);
                        }else if(id == 2){
                            Intent intent = new Intent(context, AudioFolderActivity.class);
                            context.startActivity(intent);
                        }else if(id == 3){
                            Intent intent = new Intent(context, VideoFolderActivity.class);
                            context.startActivity(intent);
                        }else if(id == 4){
                            Intent intent = new Intent(context, DocumentsFolderActivity.class);
                            context.startActivity(intent);
                        }else if(id == 5){

                        }else if(id == 6){

                        }else if(id == 7){

                        }else if(id == 8){

                        }else if(id == 9){

                        }
                    }
                });
           }
    }
}
