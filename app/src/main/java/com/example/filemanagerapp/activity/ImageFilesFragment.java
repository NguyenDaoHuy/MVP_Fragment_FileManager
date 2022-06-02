package com.example.filemanagerapp.activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filemanagerapp.adapter.ImageFilesAdapter;
import com.example.filemanagerapp.adapter.VideoFilesAdapter;
import com.example.filemanagerapp.databinding.ActivityImageBinding;
import com.example.filemanagerapp.model.FileItem;
import com.example.filemanagerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class ImageFilesFragment extends Fragment implements ImageFilesAdapter.ImageFilesInterface {
    public static final String TAG = ImageFilesFragment.class.getName();
    private ArrayList<FileItem> fileItemArrayList = new ArrayList<>();
    private ImageFilesAdapter imagesAdapter;
    private String folder_name = "";
    private BottomSheetDialog bottomSheetDialog;
    private ActivityImageBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_image,container,false);
        view = binding.getRoot();
        folder_name = getArguments().getString("nameOFFolder");
        showImageFile();
        imagesAdapter = new ImageFilesAdapter(this);
        binding.tvImageFolder.setText(folder_name);
        binding.lvListItem.setAdapter(imagesAdapter);
        binding.lvListItem.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,false));
        imagesAdapter.notifyDataSetChanged();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager() != null){
                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }


    private void showImageFile() {
        fileItemArrayList = fetchMedia(folder_name);
    }

    private ArrayList<FileItem> fetchMedia(String folderName) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media.DATA+" like?";
        String[] selectionArg = new String[]{"%"+folderName+"%"};
        Cursor cursor = getActivity().getContentResolver().query(uri,null,
                selection,selectionArg,null);
        if(cursor!=null && cursor.moveToNext()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
                FileItem fileItem = new FileItem(id,title,displayName,size,duration,path,dateAdded);
                fileItems.add(fileItem);
            }while(cursor.moveToNext());
        }
        return fileItems;
    }

    @Override
    public int getCount() {
        if(fileItemArrayList == null){
            return 0;
        } else {
            fileItemArrayList.size();
        }
        return fileItemArrayList.size();
    }

    @Override
    public FileItem image(int position) {
        return fileItemArrayList.get(position);
    }

    @Override
    public void onClickItem(int position) {
        ImagePlayerFragment imagePlayerFragment = new ImagePlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("anh",fileItemArrayList.get(position));
        imagePlayerFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMain, imagePlayerFragment);
        fragmentTransaction.addToBackStack(ImagePlayerFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onLongClickItem(int position,View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(),v);
        popupMenu.getMenu().add("OPEN");
        popupMenu.getMenu().add("DELETE");
        popupMenu.getMenu().add("DETAIL");
        popupMenu.getMenu().add("SHARE");
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getTitle().equals("OPEN")){
                Intent intent = new Intent(this.getContext(), ImagePlayerFragment.class);
                intent.putExtra("anh", (Serializable) fileItemArrayList.get(position));
                startActivity(intent);
            }
            if(item.getTitle().equals("DELETE")){
                AlertDialog.Builder alerDialog = new AlertDialog.Builder(getContext());
                alerDialog.setTitle("Deleta");
                alerDialog.setMessage("Do you want to delete this video ?");
                alerDialog.setPositiveButton("Delete", (dialog, which) -> {
                    Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            Long.parseLong(fileItemArrayList.get(position).getId()));
                    File file = new File(fileItemArrayList.get(position).getPath());
                    boolean delete = file.delete();
                    if(delete){
                        getActivity().getContentResolver().delete(uri,null,null);
                        fileItemArrayList.remove(position);
                        imagesAdapter.notifyItemRemoved(position);
                        imagesAdapter.notifyItemRangeChanged(position,fileItemArrayList.size());
                        Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Can't deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                alerDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                alerDialog.show();
            }
            if(item.getTitle().equals("DETAIL")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Information");
                FileItem file = fileItemArrayList.get(position);
                long longTime = Long.parseLong(file.getDateAdded()) * 1000;
                builder.setMessage("Name :" + file.getDisplayName() +
                        "\nSize :" + android.text.format.Formatter.formatFileSize(getContext(),
                        Long.parseLong(fileItemArrayList.get(position).getSize())) +
                        "\nDate :" + VideoFilesAdapter.convertEpouch(longTime));
                builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
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
                startActivity(Intent.createChooser(intent,"Share using "));
            }
            return true;
        });
        popupMenu.show();
        return true;
    }
    @Override
    public void onClickMenu(int position){
        bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetTheme);
        View bsView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout,
                getActivity().findViewById(R.id.bottom_sheet));
        bsView.findViewById(R.id.bs_language).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(bsView);
        bottomSheetDialog.show();
    }

    @Override
    public Context context() {
        return getContext();
    }
}