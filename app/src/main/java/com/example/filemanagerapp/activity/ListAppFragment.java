package com.example.filemanagerapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.filemanagerapp.MainActivity;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.databinding.ActivityListAppBinding;

import java.util.List;

public class ListAppFragment extends Fragment {

    private ActivityListAppBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_list_app,container,false);
        view = binding.getRoot();
        InstalledApps();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void InstalledApps() {
        @SuppressLint("QueryPermissionsNeeded") List<PackageInfo> list = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i<list.size(); i++){
            PackageInfo packageInfo = list.get(i);
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                ArrayAdapter<PackageInfo> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, list);
                binding.lvApp.setAdapter(arrayAdapter);
            }
        }
    }
}