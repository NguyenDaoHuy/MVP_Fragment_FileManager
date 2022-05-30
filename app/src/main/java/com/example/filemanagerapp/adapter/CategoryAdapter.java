package com.example.filemanagerapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.model.Category;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.databinding.ItemDanhMucBinding;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHoder> {

    private final CategoryInterFace categoryInterFace;;

    public CategoryAdapter(CategoryInterFace categoryInterFace) {
        this.categoryInterFace = categoryInterFace;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemDanhMucBinding itemDanhMucBinding = DataBindingUtil.inflate(layoutInflater,R.layout.item_danh_muc,parent,false);
        return new ViewHoder(itemDanhMucBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        Category category = categoryInterFace.category(position);
        holder.itemDanhMucBinding.setCategory(category);
        holder.itemDanhMucBinding.executePendingBindings();
        holder.itemView.setOnClickListener(v -> categoryInterFace.onClickItem(position));
    }

    @Override
    public int getItemCount() {
        return categoryInterFace.getCount();
    }

    public class ViewHoder extends RecyclerView.ViewHolder{
           ItemDanhMucBinding itemDanhMucBinding;
           public ViewHoder(@NonNull ItemDanhMucBinding itemDanhMucBinding){
                super(itemDanhMucBinding.getRoot());
                this.itemDanhMucBinding = itemDanhMucBinding;
           }
    }
    public interface CategoryInterFace {
         int getCount();
         Category category (int position);
         void onClickItem(int position);
    }
}
