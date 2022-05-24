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
    private Context context;
    private ArrayList<FileItem> ds = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;

    public ImagesAdapter(Context context, ArrayList<FileItem> ds){
        this.context = context;
        this.ds = ds;
    }
    @Override
    public int getCount() {
        return ds.size();
    }

    @Override
    public Object getItem(int position) {
        return ds.get(position);
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
        FileItem file = ds.get(position);
        imgAnh.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        tenAnh.setText(file.getDisplayName());
        String size = file.getSize();
        sizeAnh.setText(android.text.format.Formatter.formatFileSize(context,
                Long.parseLong(size)));

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetTheme);
                 View bsView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout,
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
                Intent intent = new Intent(context, ImagePlayerActivity.class);
                intent.putExtra("anh", (Serializable) ds.get(position));
                context.startActivity(intent);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().add("OPEN");
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("DETAIL");
                popupMenu.getMenu().add("SHARE");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("OPEN")){
                            Intent intent = new Intent(context, ImagePlayerActivity.class);
                            intent.putExtra("anh", (Serializable) ds.get(position));
                            context.startActivity(intent);
                        }
                        if(item.getTitle().equals("DELETE")){
                         
                        }
                        if(item.getTitle().equals("DETAIL")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Information");
                            FileItem file = ds.get(position);
                            builder.setMessage("Name :" + file.getDisplayName() +
                                    "\nSize :" + android.text.format.Formatter.formatFileSize(context,
                                    Long.parseLong(size)) +
                                    "\nDate :" + file.getDateAdded());
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
                            context.startActivity(Intent.createChooser(intent,"Share using "));
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

}
