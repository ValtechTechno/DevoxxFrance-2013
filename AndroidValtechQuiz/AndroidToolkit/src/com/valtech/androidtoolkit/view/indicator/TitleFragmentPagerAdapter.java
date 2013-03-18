package com.valtech.androidtoolkit.view.indicator;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Helper class that implements FragmentPagerAdapter and TitleProvider.
 */
public abstract class TitleFragmentPagerAdapter extends FragmentPagerAdapter implements TitleProvider
{
    public TitleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
}
