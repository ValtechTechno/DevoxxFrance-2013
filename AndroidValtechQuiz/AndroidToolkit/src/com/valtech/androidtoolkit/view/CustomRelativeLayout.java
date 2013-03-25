package com.valtech.androidtoolkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.valtech.androidtoolkit.animation.IAnimEndListener;

public class CustomRelativeLayout extends RelativeLayout
{

    private IAnimEndListener animEndListener;


    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void attachAnimEndListener(IAnimEndListener listener) {
        animEndListener = listener;
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        if (animEndListener != null) animEndListener.animEnded();
    }
}
