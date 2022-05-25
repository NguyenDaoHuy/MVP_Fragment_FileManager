package com.example.filemanagerapp.Adapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import com.example.filemanagerapp.Activity.ImagePlayerActivity;
import com.example.filemanagerapp.Model.FileItem;
import com.example.filemanagerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;

public class ImagesAdapter extends BaseAdapter {
    ImageFilesInterface imageFilesInterface;
    private BottomSheetDialog bottomSheetDialog;

    public ImagesAdapter(ImageFilesInterface imageFilesInterface) {
        this.imageFilesInterface = imageFilesInterface;
    }

    @Override
    public int getCount() {
        return imageFilesInterface.getCount();
    }

    @Override
    public Object getItem(int position) {
        return imageFilesInterface.image(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_files,parent,false);
        RelativeLayout item_image = view.findViewById(R.id.item_image);
        ImageView btnMenu = view.findViewById(R.id.btnMenu);
        ImageView imgAnh =view.findViewById(R.id.imgAnh);
        TextView tenAnh = view.findViewById(R.id.tenAnh);
        TextView sizeAnh = view.findViewById(R.id.sizeAnh);
        FileItem file = imageFilesInterface.image(position);
        imgAnh.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        tenAnh.setText(file.getDisplayName());
        String size = file.getSize();
        sizeAnh.setText(android.text.format.Formatter.formatFileSize(parent.getContext(),
                Long.parseLong(size)));

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bottomSheetDialog = new BottomSheetDialog(parent.getContext(),R.style.BottomSheetTheme);
                 View bsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_layout,
                         v.findViewById(R.id.bottom_sheet));
                 bsView.findViewById(R.id.bs_language).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                           view.performClick();
                           bottomSheetDialog.dismiss();
                     }
                 });
                 bottomSheetDialog.setContentView(bsView);
                 bottomSheetDialog.show();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFilesInterface.onClickItem(position);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(parent.getContext(),v);
                popupMenu.getMenu().add("OPEN");
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("DETAIL");
                popupMenu.getMenu().add("SHARE");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("OPEN")){
                            Intent intent = new Intent(parent.getContext(), ImagePlayerActivity.class);
                            intent.putExtra("anh", (Serializable) imageFilesInterface.image(position));
                            parent.getContext().startActivity(intent);
                        }
                        if(item.getTitle().equals("DELETE")){

                        }
                        if(item.getTitle().equals("DETAIL")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                            builder.setTitle("Information");
                            FileItem file = imageFilesInterface.image(position);
                            long longTime = Long.parseLong(file.getDateAdded()) * 1000;
                            builder.setMessage("Name :" + file.getDisplayName() +
                                    "\nSize :" + android.text.format.Formatter.formatFileSize(parent.getContext(),
                                    Long.parseLong(size)) +
                                    "\nDate :" + VideoFilesAdapter.convertEpouch(longTime));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog al = builder.create();
                            al.show();
                        }
                        if(item.getTitle().equals("SHARE")){
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            String body = "Download this file";
                            String sub = "http://play.google.com";
                            intent.putExtra(Intent.EXTRA_TEXT,body);
                            intent.putExtra(Intent.EXTRA_TEXT,sub);
                            parent.getContext().startActivity(Intent.createChooser(intent,"Share using "));
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        return view;
    }
    public interface ImageFilesInterface{
        int getCount();
        FileItem image (int position);
        void onClickItem(int position);
        void onLongClickItem(int position);
        void onClickMenu(int position);
    }
}
