package com.song.eleshoppingcart.listener;

import android.support.v4.view.ViewPager;

/**
 * Created by Administrator on 2016/10/7 0007.
 */
public abstract class OnPageChangeListener implements ViewPager.OnPageChangeListener {


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onViewPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    protected abstract void onViewPageSelected(int position);
}
