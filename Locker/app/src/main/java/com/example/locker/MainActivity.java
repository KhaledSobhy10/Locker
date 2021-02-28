package com.example.locker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    AppsInfoAdapter appsInfoAdapter;
    ProgressBar progressBar;
    ListView apps_list;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = findViewById(R.id.progressBar);


        apps_list = findViewById(R.id.apps_list);
        new MyAsyncTask().execute("");



//        Intent mIntent = new Intent(this, MyService.class);
//      MyService.enqueueWork(MainActivity.this, mIntent);


//        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Service.USAGE_STATS_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.i("pp",  "inactive ? "+usageStatsManager.isAppInactive("com.example.testapp"));
//            Toast.makeText(this, "ss", Toast.LENGTH_SHORT).show();
//        }

//Toast.makeText(this, processesRun().get(0).processName,Toast.LENGTH_LONG).show();

    }

//    private ArrayList<String> getAppsPackageName() {
//        ArrayList<String> arrayListAppsPackageName = new ArrayList<>();
//        List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(0);
//        for (ApplicationInfo app : apps) {
//            if (!((app.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0)) {
//                arrayListAppsPackageName.add(app.packageName);
//            }
//        }
//        return arrayListAppsPackageName;
//    }

//    private String getPackageName1() {
//        ActivityManager am = (ActivityManager) MainActivity.this.getSystemService(Activity.ACTIVITY_SERVICE);
//        String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
//        return packageName;
//    }


    private List<ActivityManager.RunningAppProcessInfo> processesRun() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager.getRunningAppProcesses();
        return listOfProcesses;
    }


    public class MyAsyncTask extends AsyncTask<String, Void, ArrayList<ApplicationInfo>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ApplicationInfo> doInBackground(String... strings) {
            ArrayList<ApplicationInfo> applicationInfoArrayList = new ArrayList<>();
            List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(0);
            for (ApplicationInfo app : apps) {
                if (!((app.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0)) {
                    applicationInfoArrayList.add(app);
                }
            }
            return applicationInfoArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<ApplicationInfo> apps) {
            progressBar.setVisibility(View.GONE);
            appsInfoAdapter = new AppsInfoAdapter(MainActivity.this, apps);
            apps_list.setAdapter(appsInfoAdapter);
            appsInfoAdapter.notifyDataSetChanged();
        }
    }

}