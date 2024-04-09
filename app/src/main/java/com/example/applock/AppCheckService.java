package com.example.applock;


import static com.example.applock.MyApplication.CHAINNEL_ID;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppCheckService extends Service {

    private static final String TAG = "AppCheckService";
    private static final int CHANNEL_DEFAULT_IMPORTANCE_SERVICE = 1;

    public static Notification notification;


    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");


        Intent intent1 = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_MUTABLE);

        Notification notification1 = new NotificationCompat.Builder(
                this,
                CHAINNEL_ID).setContentTitle("Title notification service")
                .setContentText("ok")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification1);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                checkRunningApps();
            }
        });
        thread.start();


        return START_STICKY;
    }


    // get current apps is openning
    private void checkRunningApps() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }

            UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long currentTime = System.currentTimeMillis();
            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 10, currentTime);

            if (stats != null) {
                SortedMap<Long, UsageStats> runningTasks = new TreeMap<>();
                for (UsageStats usageStats : stats) {
                    runningTasks.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!runningTasks.isEmpty()) {
                    String packageName = runningTasks.get(runningTasks.lastKey()).getPackageName();
                    PackageManager packageManager = getPackageManager();
                    try {
                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                        String appName = (String) packageManager.getApplicationLabel(applicationInfo);
                        Log.d("Current App", "Name: " + appName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}