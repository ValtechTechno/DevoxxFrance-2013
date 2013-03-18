package com.valtech.androidtoolkit.view.listener;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public abstract class InfiniteOnPageChangeListener implements OnPageChangeListener
{

    private int mFocusedPage;
    private int mSize;
    private ViewPager mPager;


    public InfiniteOnPageChangeListener(int size, int initialPosition, ViewPager pager) {
        if (initialPosition == 0) {
            mFocusedPage = initialPosition + 1;
        } else {
            mFocusedPage = initialPosition;
        }
        mSize = size;
        mPager = pager;
    }

    @Override
    public void onPageSelected(int position) {
        mFocusedPage = position;
        whenPageIsSelected(position);
    }

    public abstract void whenPageIsSelected(int position);

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (mFocusedPage == 0) {
                mPager.setCurrentItem(mSize);
            } else if (mFocusedPage == mSize + 1) {
                mPager.setCurrentItem(1);
            }
        }
        whenPageScrollStateHasChanged(state);
    }

    public abstract void whenPageScrollStateHasChanged(int state);

    @Override
    public abstract void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
}
