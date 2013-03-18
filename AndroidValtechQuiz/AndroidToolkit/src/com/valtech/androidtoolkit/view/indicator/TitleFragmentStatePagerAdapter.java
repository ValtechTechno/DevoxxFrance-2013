package com.valtech.androidtoolkit.view.indicator;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Helper class that implements FragmentStatePagerAdapter and TitleProvider.
 */
public abstract class TitleFragmentStatePagerAdapter extends FragmentStatePagerAdapter implements TitleProvider
{
    public TitleFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }
}
