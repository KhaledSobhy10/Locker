package com.example.locker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppsInfoAdapter extends ArrayAdapter<ApplicationInfo> {

    Context context;
    private Set<String> blockedAppsName;


    public AppsInfoAdapter(@NonNull Context context, @NonNull List<ApplicationInfo> applicationInfos) {
        super(context, 0, applicationInfos);
        this.context = context;
        blockedAppsName = getBlockedAppsNameFromShared();

        Log.i("s", "AppsInfoAdapter: current set : " + getApps());
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public ApplicationInfo getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_info_item, parent, false);
        }
        ApplicationInfo appInfo = getItem(position);

        ImageView appIcon = view.findViewById(R.id.iv_app_icon);
        TextView appName = view.findViewById(R.id.tv_app_name);
        final TextView appStatus = view.findViewById(R.id.tv_app_status);

        SwitchCompat switchStatus = view.findViewById(R.id.switch_app_status);


        PackageManager packageManager = context.getPackageManager();

        final String appLabel = (String) appInfo.loadLabel(packageManager);
        if (blockedAppsName != null && blockedAppsName.contains(appLabel)) {
            appStatus.setText(context.getString(R.string.app_status_blocked));
            switchStatus.setChecked(true);
        } else {
            appStatus.setText(context.getString(R.string.app_status_allowed));
            switchStatus.setChecked(false);
        }

        appIcon.setImageDrawable(appInfo.loadIcon(packageManager));
        appName.setText(appLabel);

        switchStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppsStatusInShared(appLabel);
                Log.i("S", "onClick: clicked");
            }
        });


        return view;
    }




    private void updateAppsStatusInShared(String appName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("blocking", Context.MODE_PRIVATE);

        String sharedBlockedListKey = context.getString(R.string.pref_key_blocked_apps_name);

        Set<String> apps = new HashSet<>();

        Log.i("s", "updateAppsStatusInShared: before  " + getApps(apps));


        if (blockedAppsName != null) {
            apps.addAll(blockedAppsName);
        }

        if (!apps.contains(appName)) {
            apps.add(appName);
        } else {
            apps.remove(appName);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(sharedBlockedListKey,apps);
        editor.apply();


}

    private Set<String> getBlockedAppsNameFromShared() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("blocking", Context.MODE_PRIVATE);

        String sharedBlockedListKey = context.getString(R.string.pref_key_blocked_apps_name);
        Set<String> stringSet = sharedPreferences.getStringSet(sharedBlockedListKey, null);

        Log.i("s", "getBlockedAppsNameFromShared: " + stringSet);
        return stringSet;
    }


    //for debugging
    public String getApps() {
        if (blockedAppsName == null) return null;

        StringBuilder allApps = new StringBuilder();
        for (String appName : blockedAppsName)
            allApps.append(appName).append(",");

        return allApps.toString();
    }

    //for debugging
    public String getApps(Set<String> stringSet) {
        if (stringSet == null) return null;

        StringBuilder allApps = new StringBuilder();
        for (String appName : stringSet)
            allApps.append(appName).append(",");

        return allApps.toString();
    }
}
