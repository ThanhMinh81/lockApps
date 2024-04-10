package com.example.applock;

import static android.content.ContentValues.TAG;

import static androidx.core.view.MenuCompat.setShowAsAction;

import android.app.ActionBar;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Service;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.applock.adapter.ViewPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_DEFAULT_IMPORTANCE = "Service";
    ListView listView;
    TextView text;

    private String TAG = "MainActivity";

    private static final int REQUEST_USAGE_STATS_PERMISSION = 1;

    private static final int NOTIFICATION_ID = 1;

    Button btnStop;

    TabLayout mTabLayout;
    ViewPager mViewPager;

    Spinner spinnerSelected;

    MaterialToolbar materialToolbar;

    android.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        materialToolbar = findViewById(R.id.toolbar);
        materialToolbar.setTitleTextColor(Color.WHITE);

        // phải có cái này thì searchview , title của toolbar mới được hiển thị
        setSupportActionBar(materialToolbar);

        // Ẩn tiêu đề của toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        spinnerSelected = this.<Spinner>findViewById(R.id.spinner_nav);

        mTabLayout = this.<TabLayout>findViewById(R.id.tab_layout_activity);

        mViewPager = this.<ViewPager>findViewById(R.id.viewpagerHome);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSelected.setAdapter(adapterSpinner);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("Tab1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab2"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        spinnerSelected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTextSize(20);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.nav_search_app);
        if (searchMenuItem == null) {
            return true;
        }

        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        int appBarPadding = getResources().getDimensionPixelSize(R.dimen.app_bar_padding);
        int menuItemSize = getResources().getDimensionPixelSize(R.dimen.app_bar_size_menu_item);
        searchView.setMaxWidth(materialToolbar.getWidth() * appBarPadding - menuItemSize);
  
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) searchView.getLayoutParams();
//        android.view.ViewGroup.LayoutParams params = searchview.getLayoutParams();

//        params.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        searchView.setLayoutParams(params);



        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                setItemsVisibility(menu, item, false);
                // Xử lý khi MenuItem được mở rộng (expanded)
                if (getSupportActionBar() != null) {
                    int searchIconId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
                    ImageView searchIcon = searchView.findViewById(searchIconId);
                    if (searchIcon != null) {
                        searchIcon.setVisibility(View.GONE);
                    }

                    searchView.setBackgroundColor(Color.TRANSPARENT);

//                    searchView.setBackgroundColor(getResources().getColor(R.color.white));
                }
                return true; // Trả về true để xác nhận đã xử lý sự kiện mở rộng
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Xử lý khi MenuItem được thu gọn (collapsed)
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                }
                return true; // Trả về true để xác nhận đã xử lý sự kiện thu gọn
            }
        });


        return true;

    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }



    private void getListAppSystem() {


//        btnStop = this.<Button>findViewById(R.id.stopService);
//        btnStop.setOnClickListener(v -> {
//
//            Intent intent = new Intent(MainActivity.this, AppCheckService.class);
//
//            startService(intent);
//
//        });


        // get list of all the apps installed
        List<ApplicationInfo> installedApps = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        // create a list with size of total number of apps
        String[] apps = new String[installedApps.size()];
        ArrayList<String> strings = new ArrayList<>();

        int i = 0;

        // add all the app name in string list

        for (ApplicationInfo appInfo : installedApps) {
//             ung dung dc su dung
            if (getPackageManager().getLaunchIntentForPackage(appInfo.packageName) != null) {
                strings.add(appInfo.packageName + " -- " + appInfo.loadLabel(getPackageManager()));
            }
        }

        requestUsageStatsPermission();

        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strings));

        Intent intent = new Intent(MainActivity.this, AppCheckService.class);

        startService(intent);

    }

    private void requestUsageStatsPermission() {
        if (!hasUsageStatsPermission()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, REQUEST_USAGE_STATS_PERMISSION);
        }
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }


    private boolean isAccessGranted() {

        try {

            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);

            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }


}