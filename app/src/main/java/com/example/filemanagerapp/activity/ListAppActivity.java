package com.example.filemanagerapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import com.example.filemanagerapp.R;
import com.example.filemanagerapp.databinding.ActivityListAppBinding;

import java.util.List;

public class ListAppActivity extends AppCompatActivity {

    private ActivityListAppBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_list_app);
        InstalledApps();
    }
    private void InstalledApps() {
        @SuppressLint("QueryPermissionsNeeded") List<PackageInfo> list = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i<list.size(); i++){
            PackageInfo packageInfo = list.get(i);
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                ArrayAdapter<PackageInfo> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
                binding.lvApp.setAdapter(arrayAdapter);
            }
        }
    }
}