package com.valtech.androidtoolkit.gesture;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

// TODO Comments
public abstract class FlingGestureDetector extends SimpleOnGestureListener
{
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // If moving up or down, fling movement is not accepted.
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
            return false;
        }

        // TODO Manage pixel density and see if there is not a simpler solution!
        if ((e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {
            onFlingLeft();
        } else if ((e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {
            onFlingRight();
        }
        return false;
    }

    protected void onFlingLeft() {}

    protected void onFlingRight() {}
}
