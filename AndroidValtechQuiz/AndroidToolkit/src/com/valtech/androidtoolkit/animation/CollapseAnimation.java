package com.valtech.androidtoolkit.animation;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public class CollapseAnimation extends Animation implements Animation.AnimationListener
{
    private static int ANIMATION_DURATION = 1;
    private View mView;
    private int mTargetedWidth;
    private int mDuration;

    private Animation.AnimationListener mListener;


    public CollapseAnimation(View v, int fromWidth, int toWidth, int duration) {
        mView = v;
        mListener = null;

        LayoutParams lyp = mView.getLayoutParams();

        mTargetedWidth = lyp.width;
        mDuration = duration;

        setDuration(ANIMATION_DURATION);
        setRepeatCount(duration + 1);
        setFillAfter(false);
        setInterpolator(new AccelerateInterpolator());
        super.setAnimationListener(this);
    }

    @Override
    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    @Override
    public void onAnimationEnd(Animation anim) {
        if (mListener != null) {
            mListener.onAnimationEnd(anim);
        }
    }

    @Override
    public void onAnimationRepeat(Animation anim) {
        LayoutParams lyp = mView.getLayoutParams();
        lyp.width = lyp.width - mTargetedWidth / mDuration;
        mView.setLayoutParams(lyp);

        if (mListener != null) {
            mListener.onAnimationRepeat(anim);
        }
    }

    @Override
    public void onAnimationStart(Animation anim) {
        if (mListener != null) {
            mListener.onAnimationStart(anim);
        }
    }
}
