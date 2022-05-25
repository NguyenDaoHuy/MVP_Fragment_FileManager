package com.example.filemanagerapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.Model.Category;
import com.example.filemanagerapp.R;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHoder> {

    CategoryInterFace categoryInterFace;
    private ImageView icon_logo;
    private TextView tvTenDanhMuc,tvDungLuong;

    public CategoryAdapter(CategoryInterFace categoryInterFace) {
        this.categoryInterFace = categoryInterFace;
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
       Category category = categoryInterFace.category(position);
       icon_logo.setImageResource(category.getIcon());
       tvTenDanhMuc.setText(category.getName());
       String dungLuong = String.valueOf(category.getStorage());
       tvDungLuong.setText(dungLuong);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryInterFace.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryInterFace.getCount();
    }

    public class ViewHoder extends RecyclerView.ViewHolder{
           public ViewHoder(@NonNull View view){
                super(view);
                icon_logo = view.findViewById(R.id.icon_logo);
                tvTenDanhMuc = view.findViewById(R.id.tvTenDanhMuc);
                tvDungLuong = view.findViewById(R.id.tvDungLuong);
           }
    }
    public interface CategoryInterFace {
         int getCount();
         Category category (int position);
         void onClickItem(int position);
    }
}
