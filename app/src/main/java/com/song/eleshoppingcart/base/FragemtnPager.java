package com.song.eleshoppingcart.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/10/5 0005.
 */
public class FragemtnPager extends FragmentPagerAdapter {
    private final List<? extends Fragment> mFragments;
    private final String[] mFragmentTitles;

    public FragemtnPager(FragmentManager fm, List<? extends Fragment> fragments, String[] fragmentTitles) {
        super(fm);
        mFragments = fragments;
        mFragmentTitles = fragmentTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles[position];
    }
}
