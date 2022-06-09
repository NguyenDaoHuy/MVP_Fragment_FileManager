package com.example.filemanagerapp.image.imagefiles;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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
import com.example.filemanagerapp.Interface.InterfaceContract;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.image.imageplayer.ImagePlayerFragment;
import com.example.filemanagerapp.video.videofiles.VideoFilesAdapter;
import com.example.filemanagerapp.model.FileItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.io.File;

public class ImageFilesFragment extends Fragment implements ImageFilesAdapter.ImageFilesInterface, InterfaceContract.setFileView {
    public static final String TAG = ImageFilesFragment.class.getName();
    private ImageFilesAdapter imagesAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private ImageFilesPresenter imageFilesPresenter;
    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.example.filemanagerapp.databinding.ActivityImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_image, container, false);
        View view = binding.getRoot();
        String folder_name = getArguments().getString("nameOFFolder");
        imageFilesPresenter = new ImageFilesPresenter(folder_name,getActivity(),this);
        imagesAdapter = new ImageFilesAdapter(this);
        binding.tvImageFolder.setText(folder_name);
        binding.lvListItem.setAdapter(imagesAdapter);
        binding.lvListItem.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,false));
        imagesAdapter.notifyDataSetChanged();
        binding.btnBack.setOnClickListener(v -> {
            if(getFragmentManager() != null){
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }


    @Override
    public int getCount() {
        if(imageFilesPresenter.getFileItemArrayList() == null){
            return 0;
        } else {
            imageFilesPresenter.getFileItemArrayList().size();
        }
        return imageFilesPresenter.getFileItemArrayList().size();
    }

    @Override
    public FileItem image(int position) {
        return imageFilesPresenter.getFileItemArrayList().get(position);
    }

    @Override
    public void onClickItem(int position) {
        ImagePlayerFragment imagePlayerFragment = new ImagePlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("anh",imageFilesPresenter.getFileItemArrayList().get(position));
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
                ImagePlayerFragment imagePlayerFragment = new ImagePlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("anh",imageFilesPresenter.getFileItemArrayList().get(position));
                imagePlayerFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentMain, imagePlayerFragment);
                fragmentTransaction.addToBackStack(ImagePlayerFragment.TAG);
                fragmentTransaction.commit();
            }
            if(item.getTitle().equals("DELETE")){
                AlertDialog.Builder alerDialog = new AlertDialog.Builder(getContext());
                alerDialog.setTitle("Deleta");
                alerDialog.setMessage("Do you want to delete this video ?");
                alerDialog.setPositiveButton("Delete", (dialog, which) -> {
                    Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            Long.parseLong(imageFilesPresenter.getFileItemArrayList().get(position).getId()));
                    File file = new File(imageFilesPresenter.getFileItemArrayList().get(position).getPath());
                    boolean delete = file.delete();
                    if(delete){
                        getActivity().getContentResolver().delete(uri,null,null);
                        imageFilesPresenter.getFileItemArrayList().remove(position);
                        imagesAdapter.notifyItemRemoved(position);
                        imagesAdapter.notifyItemRangeChanged(position,imageFilesPresenter.getFileItemArrayList().size());
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
                FileItem file = imageFilesPresenter.getFileItemArrayList().get(position);
                long longTime = Long.parseLong(file.getDateAdded()) * 1000;
                builder.setMessage("Name :" + file.getDisplayName() +
                        "\nSize :" + android.text.format.Formatter.formatFileSize(getContext(),
                        Long.parseLong(imageFilesPresenter.getFileItemArrayList() .get(position).getSize())) +
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

    @Override
    public void setSuccess(String str) {

    }

    @Override
    public void setError(String str) {
         Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
}