package com.example.applock;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class thread extends Thread {

    Context context;

    public thread(Context context) {
        this.context = context;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return;
            }

            try {

                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> alltasks = am.getRunningTasks(1);

                for (int i1 = 0; i1 < alltasks.size(); i1++) {
                    ActivityManager.RunningTaskInfo aTask = alltasks.get(i1);


                    Log.i("TASK", "aTask.topActivity: " + aTask.topActivity.getClassName());
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
