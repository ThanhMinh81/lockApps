package com.example.applock.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.applock.fragment.AppListFragment;
import com.example.applock.fragment.SettingFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case  0 :
                return  new AppListFragment();

            case  1 :
                return new SettingFragment();
            default:
                return  new AppListFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = "";
        switch (position)
        {
            case 0 :
                title = "Home";
                break;

            case 1 :
                title = "Setting";
                break;

        }

        return super.getPageTitle(position);
    }
}
