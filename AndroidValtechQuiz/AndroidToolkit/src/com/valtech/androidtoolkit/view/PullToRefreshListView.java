package com.valtech.androidtoolkit.view;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valtech.androidtoolkit.R;

public class PullToRefreshListView extends ListView implements OnScrollListener
{
    // TODO Put this information in component attributes
    public static final long REQUEST_TIMEOUT_IN_MS = 60 * 1000;
    private static final int TAP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;

    @SuppressWarnings("unused")
    private static final String TAG = "PullToRefreshListView";

    private OnRefreshListener mOnRefreshListener;

    /**
     * Listener that will receive notifications every time the list scrolls.
     */
    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;

    private LinearLayout mRefreshView;
    private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;

    private int mCurrentScrollState;
    private int mRefreshState;

    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;

    private int mRefreshViewHeight;
    private int mRefreshOriginalTopPadding;
    private int mLastMotionY;

    private boolean mBounceHack;
    private RelativeLayout mRefreshViewProgressLayout;
    private boolean removed = false;


    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // Load all of the animations we need in code rather than through XML
        mFlipAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mRefreshView = (LinearLayout) mInflater.inflate(R.layout.pull_to_refresh_header, this, false);
        mRefreshViewText = (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
        mRefreshViewProgressLayout = (RelativeLayout) mRefreshView.findViewById(R.id.pull_to_refresh_progress_layout);
        mRefreshViewImage = (ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
        mRefreshViewProgress = (ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
        mRefreshViewLastUpdated = (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);

        mRefreshViewImage.setMinimumHeight(50);
        mRefreshView.setOnClickListener(new OnClickRefreshListener());
        mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();

        mRefreshState = TAP_TO_REFRESH; // TODO
        new Handler().postDelayed(new CheckResultDelayedTask(this), REQUEST_TIMEOUT_IN_MS);

        addHeaderView(mRefreshView);

        super.setOnScrollListener(this);

        measureView(mRefreshView);
        mRefreshViewHeight = mRefreshView.getMeasuredHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        setSelection(1);
        super.onAttachedToWindow();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        setSelection(1);
    }

    public void setAdapterWhileRefreshing(ListAdapter adapter) {
        super.setAdapter(adapter);

        setSelection(0);
    }

    /**
     * Set the listener that will receive notifications every time the list scrolls.
     * 
     * @param l The scroll listener.
     */
    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        mOnScrollListener = l;
    }

    /**
     * Register a callback to be invoked when this list should be refreshed.
     * 
     * @param onRefreshListener The callback to run.
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    /**
     * Set a text to represent when the list was last updated.
     * 
     * @param lastUpdated Last updated at.
     */
    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
            mRefreshViewLastUpdated.setText(lastUpdated);
        } else {
            mRefreshViewLastUpdated.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();
        mBounceHack = false;

        switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
            if (!isVerticalScrollBarEnabled()) {
                setVerticalScrollBarEnabled(true);
            }
            if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
                if ((mRefreshView.getBottom() >= mRefreshViewHeight || mRefreshView.getTop() >= 0)
                    && mRefreshState == RELEASE_TO_REFRESH) {
                    // Initiate the refresh
                    mRefreshState = REFRESHING;
                    prepareForRefresh();
                    onRefresh();
                } else if (mRefreshView.getBottom() < mRefreshViewHeight || mRefreshView.getTop() <= 0) {
                    // Abort refresh and scroll down below the refresh view
                    resetHeader();
                    setSelection(1);
                }
            }
            break;
        case MotionEvent.ACTION_DOWN:
            mLastMotionY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            applyHeaderPadding(event);
            break;
        }
        return super.onTouchEvent(event);
    }

    private void applyHeaderPadding(MotionEvent ev) {
        // getHistorySize has been available since API 1
        int pointerCount = ev.getHistorySize();

        for (int p = 0; p < pointerCount; p++) {
            if (mRefreshState == RELEASE_TO_REFRESH) {
                if (isVerticalFadingEdgeEnabled()) {
                    setVerticalScrollBarEnabled(false);
                }

                int historicalY = (int) ev.getHistoricalY(p);

                // Calculate the padding to apply, we divide by 1.7 to
                // simulate a more resistant effect during pull.
                int topPadding = (int) (((historicalY - mLastMotionY) - mRefreshViewHeight) / 1.7);

                mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
                                        topPadding,
                                        mRefreshView.getPaddingRight(),
                                        mRefreshView.getPaddingBottom());
            }
        }
    }

    /**
     * Sets the header padding back to original size.
     */
    private void resetHeaderPadding() {
        mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
                                mRefreshOriginalTopPadding,
                                mRefreshView.getPaddingRight(),
                                mRefreshView.getPaddingBottom());
    }

    /**
     * Resets the header to the original state.
     */
    private void resetHeader() {
        if (mRefreshState != TAP_TO_REFRESH) {
            mRefreshState = TAP_TO_REFRESH;

            resetHeaderPadding();

            // Set refresh view text to the pull label
            mRefreshViewText.setText(R.string.pull_to_refresh_tap_label);
            // Replace refresh drawable with arrow drawable
            mRefreshViewImage.setImageResource(R.drawable.ic_pulltorefresh_arrow);
            // Clear the full rotation animation
            mRefreshViewImage.clearAnimation();
            // Hide progress bar and arrow.
            mRefreshViewImage.setVisibility(View.GONE);
            mRefreshViewProgress.setVisibility(View.GONE);
            if (removed) mRefreshViewProgressLayout.setVisibility(View.VISIBLE);
        }
    }

    public void removeHeaderView() {
        // removeHeaderView(mRefreshView); // (appeler après avoir fixé
        // l'adapteur)
        // addHeaderView(new View(getContext())); // (appeler AVANT avoir fixé
        // l'adapteur)
        removed = true;
        mRefreshViewText.setVisibility(View.GONE);
        mRefreshViewImage.setVisibility(View.GONE);
        mRefreshViewProgress.setVisibility(View.GONE);
        mRefreshViewProgressLayout.setVisibility(View.GONE);
        mRefreshViewLastUpdated.setVisibility(View.GONE);
        mRefreshView.setVisibility(View.GONE);
        mRefreshView.setPadding(0, 0, 0, 0);
        measureView(mRefreshView);
        setPadding(0, -3, 0, 0);// Remove the top divider
    }

    public void showHeaderView() {
        mRefreshViewText.setVisibility(View.VISIBLE);
        // mRefreshViewImage.setVisibility(View.VISIBLE);
        // mRefreshViewProgress.setVisibility(View.VISIBLE);
        mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
        mRefreshView.setVisibility(View.VISIBLE);
        resetHeaderPadding();
        measureView(mRefreshView);
        resetHeader();
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // When the refresh view is completely visible, change the text to say
        // "Release to refresh..." and flip the arrow drawable.
        if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL && mRefreshState != REFRESHING) {
            if (firstVisibleItem == 0) {
                mRefreshViewImage.setVisibility(View.VISIBLE);
                mRefreshViewProgressLayout.setVisibility(View.VISIBLE);
                mRefreshViewProgress.setVisibility(View.INVISIBLE);// TODO
                if ((mRefreshView.getBottom() >= mRefreshViewHeight + 20 || mRefreshView.getTop() >= 0)
                    && mRefreshState != RELEASE_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.pull_to_refresh_release_label);
                    mRefreshViewImage.clearAnimation();
                    mRefreshViewImage.startAnimation(mFlipAnimation);
                    mRefreshState = RELEASE_TO_REFRESH;
                } else if (mRefreshView.getBottom() < mRefreshViewHeight + 20 && mRefreshState != PULL_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
                    if (mRefreshState != TAP_TO_REFRESH) {
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                    }
                    mRefreshState = PULL_TO_REFRESH;
                }
            } else {
                mRefreshViewImage.setVisibility(View.GONE);
                resetHeader();
            }
        } else if (mCurrentScrollState == SCROLL_STATE_FLING && firstVisibleItem == 0 && mRefreshState != REFRESHING) {
            setSelection(1);
            mBounceHack = true;
        } else if (mBounceHack && mCurrentScrollState == SCROLL_STATE_FLING) {
            setSelection(1);
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;

        if (mCurrentScrollState == SCROLL_STATE_IDLE) {
            mBounceHack = false;
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void prepareForRefresh() {
        resetHeaderPadding();

        mRefreshViewImage.setVisibility(View.GONE);
        // We need this hack, otherwise it will keep the previous drawable.
        mRefreshViewImage.setImageDrawable(null);
        mRefreshViewProgress.setVisibility(View.VISIBLE);
        mRefreshViewProgressLayout.setVisibility(View.VISIBLE);

        // Set refresh view text to the refreshing label
        mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);

        mRefreshState = REFRESHING;
        new Handler().postDelayed(new CheckResultDelayedTask(this), REQUEST_TIMEOUT_IN_MS);
    }

    public void onRefresh() {
        // Log.d(TAG, "onRefresh");
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    /**
     * Resets the list to a normal state after a refresh.
     * 
     * @param lastUpdated Last updated at.
     */
    public void onRefreshComplete(CharSequence lastUpdated, boolean reset) {
        setLastUpdated(lastUpdated);
        onRefreshComplete(reset);
    }

    /**
     * Resets the list to a normal state after a refresh.
     */
    public void onRefreshComplete(boolean reset) {
        // Logger.logDebug(TAG, "onRefreshComplete");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                resetHeader();
                // If refresh view is visible when loading completes, scroll down to
                // the next item.
                if ((mRefreshView.getBottom() > 0) && (getFirstVisiblePosition() <= 1)) {
                    // Abort refresh and scroll down below the refresh view
                    invalidateViews();
                    setSelection(1);
                }
            }
        }, 50);
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        resetHeader();
        // If refresh view is visible when loading completes, scroll down to
        // the next item.
        if ((mRefreshView.getBottom() > 0) && (getFirstVisiblePosition() <= 1)) {
            // Abort refresh and scroll down below the refresh view
            invalidateViews();
            setSelection(1);
        }
    }


    /**
     * Invoked when the refresh view is clicked on. This is mainly used when there's only a few
     * items in the list and it's not possible to drag the list.
     */
    private class OnClickRefreshListener implements OnClickListener
    {

        @Override
        public void onClick(View v) {
            if (mRefreshState != REFRESHING) {
                prepareForRefresh();
                onRefresh();
            }
        }

    }


    /**
     * Interface definition for a callback to be invoked when list should be refreshed.
     */
    public interface OnRefreshListener
    {
        /**
         * Called when the list should be refreshed.
         * <p>
         * A call to {@link PullToRefreshListView #onRefreshComplete()} is expected to indicate that
         * the refresh has completed.
         */
        public void onRefresh();
    }


    private static class CheckResultDelayedTask implements Runnable
    {
        private WeakReference<PullToRefreshListView> containerRef;


        public CheckResultDelayedTask(PullToRefreshListView container) {
            containerRef = new WeakReference<PullToRefreshListView>(container);
        }

        @Override
        public void run() {
            PullToRefreshListView container = containerRef.get();
            if ((container != null) && (container.mRefreshState == TAP_TO_REFRESH)) {
                container.mRefreshState = REFRESHING;
                container.onRefreshComplete(true);// TODO
            }
        }
    }
}
