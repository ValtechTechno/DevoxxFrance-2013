package com.valtech.androidtoolkit.animation;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public class ExpandAnimation extends Animation implements Animation.AnimationListener
{
    private static int ANIMATION_DURATION = 1;

    private View mView;
    private int mLastWidth;
    private int mTargetedWidth;
    private int mDuration;


    public ExpandAnimation(View v, int fromWidth, int toWidth, int duration) {

        mView = v;
        mTargetedWidth = toWidth;
        mDuration = duration;

        setDuration(ANIMATION_DURATION);
        setRepeatCount(mDuration);
        setFillAfter(false);
        setInterpolator(new AccelerateInterpolator());
        super.setAnimationListener(this);
    }

    @Override
    public void onAnimationRepeat(Animation anim) {
        LayoutParams lyp = mView.getLayoutParams();
        lyp.width = mLastWidth += mTargetedWidth / mDuration;
        mView.setLayoutParams(lyp);
    }

    @Override
    public void onAnimationStart(Animation anim) {
        LayoutParams lyp = mView.getLayoutParams();
        lyp.width = 0;
        mView.setLayoutParams(lyp);
        mLastWidth = 0;
    }

    @Override
    public void onAnimationEnd(Animation animation) {}

}
