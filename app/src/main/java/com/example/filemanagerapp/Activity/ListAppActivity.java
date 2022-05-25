package com.example.filemanagerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.filemanagerapp.R;
import java.util.List;

public class ListAppActivity extends AppCompatActivity {

    private ListView lvApp;
    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_app);
        lvApp = findViewById(R.id.lvApp);
        InstalledApps();
    }
    private void InstalledApps() {
        List<PackageInfo> list = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i<list.size(); i++){
            PackageInfo packageInfo = list.get(i);
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
                lvApp.setAdapter(arrayAdapter);
            }
        }
    }
}