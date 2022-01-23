package com.example.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {
    private Context mc;
    int totalTabs;

    public MyAdapter(Context context,FragmentManager fm,int totalTabs) {
        super(fm);
        mc=context;
        this.totalTabs=totalTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                EditFragment editFragment = new EditFragment();
                return editFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
