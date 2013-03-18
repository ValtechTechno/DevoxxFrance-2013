package com.valtech.androidtoolkit.view.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import com.valtech.androidtoolkit.R;

/**
 * A TitlePageIndicator is a PageIndicator which displays the title of the current page and the
 * titles of side-pages (which may or may not be visible). An option allows side-titles to be
 * clipped: if they are not visible, they are shifted so that a small part is visible on the side.
 * When the user scrolls the ViewPager then titles are also scrolled.
 * 
 * TODO Check infinite mode is working. Not tested.
 */
public class TitlePageIndicator extends View implements PageIndicator
{
    /**
     * Range in percent (of screen width) from screen center inside which text is displayed as
     * selected.
     */
    private static final float SELECTED_RANGE_IN_PERCENT = 0.25f;

    /**
     * Range in percent (of screen width) from screen center inside which text is highlighted.
     */
    private static final float HIGHLIGHTED_RANGE_N_PERCENT = 0.05f;

    // TitlePageIdnicator is synchronized with the following pager.
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    // Gives the titles to display according to the current ViewPager page displayed.
    private TitleProvider mTitleProvider;

    // ViewPager state is stored here.
    private int mCurrentPage;
    private int mFirstVisiblePage;
    private int mOffsetOfFirstVisiblePage;
    private float mCurrentOffsetInPercent;
    private int mCurrentScrollState;
    private float mPageWidth;
    // Page title properties.
    private PageTitleInfo mCurrentPageTitleInfo;
    private PageTitleInfo[] mPageTitleInfos;
    private int mPageTitleInfosSize;

    // Rendering properties
    private RectF mVisibleRect;
    // Text rendering properties.
    private Paint mPaintText;
    private boolean mHighlightedTextIsBold;
    private int mColorSideText;
    private int mColorSelectedText;

    // Padding properties.
    private float mTopPadding;
    private float mFooterPadding;
    // Title padding is the minimum space between two titles.
    private float mTitlePadding;
    // Minimum space that side titles will occupy on the sides (after shifting them) if they are not
    // visible.
    private float mClipPadding;


    public TitlePageIndicator(Context context) {
        this(context, null);
    }

    public TitlePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiTitlePageIndicatorStyle);
    }

    public TitlePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // Load defaults from resources
        final Resources resources = getResources();
        // Retrieve styles attributes
        TypedArray properties = context.obtainStyledAttributes(attrs,
                                                               R.styleable.TitlePageIndicator,
                                                               defStyle,
                                                               R.style.Widget_TitlePageIndicator);

        // ViewPager properties.
        // Evol: We could retrieve current page from attributes.
        mFirstVisiblePage = 0;
        mOffsetOfFirstVisiblePage = 0;
        mCurrentOffsetInPercent = 0.0f;
        mCurrentScrollState = ViewPager.SCROLL_STATE_IDLE;
        // Page title properties.
        // We can process until 4 titles at the same time (first visible page, page before the first
        // visible one,
        // page after the first visible page and the one after).
        mCurrentPageTitleInfo = null;
        mPageTitleInfos = new PageTitleInfo[4];
        mPageTitleInfosSize = 0;
        // Cache PageTitleInfo to avoid allocation during draw calls. Indeed, the information they
        // contain is updated when drawing.
        for (int i = 0; i < mPageTitleInfos.length; ++i) {
            mPageTitleInfos[i] = new PageTitleInfo();
        }

        // Text rendering properties
        final int defaultSelectedColor = resources.getColor(R.color.default_title_indicator_selected_color);
        final boolean defaultSelectedBold = resources.getBoolean(R.bool.default_title_indicator_selected_bold);
        final int defaultTextColor = resources.getColor(R.color.default_title_indicator_text_color);
        final float defaultTextSize = resources.getDimension(R.dimen.default_title_indicator_text_size);

        float textSize = properties.getDimension(R.styleable.TitlePageIndicator_textSize, defaultTextSize);
        mColorSelectedText = properties.getColor(R.styleable.TitlePageIndicator_selectedColor, defaultSelectedColor);
        mColorSideText = properties.getColor(R.styleable.TitlePageIndicator_textColor, defaultTextColor);
        mHighlightedTextIsBold = properties.getBoolean(R.styleable.TitlePageIndicator_selectedBold, defaultSelectedBold);

        // Padding properties.
        final float defaultTopPadding = resources.getDimension(R.dimen.default_title_indicator_top_padding);
        final float defaultFooterPadding = resources.getDimension(R.dimen.default_title_indicator_footer_padding);
        final float defaultTitlePadding = resources.getDimension(R.dimen.default_title_indicator_title_padding);
        final float defaultClipPadding = resources.getDimension(R.dimen.default_title_indicator_clip_padding);

        mTopPadding = properties.getDimension(R.styleable.TitlePageIndicator_topPadding, defaultTopPadding);
        mFooterPadding = properties.getDimension(R.styleable.TitlePageIndicator_footerPadding, defaultFooterPadding);
        mTitlePadding = properties.getDimension(R.styleable.TitlePageIndicator_titlePadding, defaultTitlePadding);
        mClipPadding = properties.getDimension(R.styleable.TitlePageIndicator_clipPadding, defaultClipPadding);

        // Allocate resources.
        mPaintText = new Paint();
        mPaintText.setTextSize(textSize);
        mPaintText.setAntiAlias(true);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        // Clean unnecessary stuff.
        properties.recycle();
    }


    private final class PageTitleInfo
    {
        private String mText;
        private boolean mIsCurrentPage;

        private float mPageXLeft;
        // public float mPageXRight; // Not used

        private float mTextWidth;
        private float mTextHeight;
        private float mTextXLeft;
        private float mTextXRight;


        public PageTitleInfo() {}

        public void update(String pTitle, Paint pPaint, int pIndexRelativeToCurrentPage, int pOffsetRelativeToCurrentPage) {
            mText = pTitle.toUpperCase();
            mIsCurrentPage = false;

            mPageXLeft = (pIndexRelativeToCurrentPage * mPageWidth) - pOffsetRelativeToCurrentPage;
            // mPageXRight = mPageXLeft + mPageWidth; // Not used

            mTextWidth = pPaint.measureText(mText);
            mTextHeight = pPaint.descent() - pPaint.ascent();
            mTextXLeft = mPageXLeft + (mPageWidth * 0.5f) - (mTextWidth * 0.5f);
            mTextXRight = mTextXLeft + mTextWidth;
        }

        /**
         * Shift text title (when clipping text on the sides).
         */
        public void shift(float x) {
            mTextXLeft += x;
            mTextXRight += x;
        }

        /**
         * Check if page title is visible in the specified rectangle area. Visibility is checked on
         * the X axis and not the Y axis.
         */
        private boolean isVisibleIn(RectF visibleRect) {
            return (mTextXLeft > visibleRect.left && mTextXLeft < visibleRect.right)
                   || (mTextXRight > visibleRect.left && mTextXRight < visibleRect.right);
        }

        /**
         * Render page title. A title can be in rendered 3 states: - On the side, in which case the
         * default style is applied. - Selected, when a page is close to the center. - Highlighted,
         * when a page is very close or in the center.
         */
        public void render(Canvas pCanvas, RectF pCanvasBounds, Paint pPaint) {
            // Render text only if it is visible in the current boundaries.
            if (!isVisibleIn(pCanvasBounds)) return;

            // Selected and highlighted is possible only for the current page.
            if (mIsCurrentPage) {
                final boolean selected = (mCurrentOffsetInPercent <= SELECTED_RANGE_IN_PERCENT);
                final boolean highlighted = (mCurrentOffsetInPercent <= HIGHLIGHTED_RANGE_N_PERCENT);

                if (selected && highlighted) {
                    renderHighlightedTitle(pCanvas, pPaint);
                } else if (selected) {
                    renderSelectedTitle(pCanvas, pPaint);
                } else {
                    renderSideTitle(pCanvas, pPaint);
                }
                // Page is no the current one but a side one.
            } else {
                renderSideTitle(pCanvas, pPaint);
            }
        }

        private void renderHighlightedTitle(Canvas pCanvas, Paint pPaint) {
            pPaint.setColor(mColorSelectedText);
            pPaint.setFakeBoldText(mHighlightedTextIsBold);
            pCanvas.drawText(mText, mTextXLeft, mTopPadding + mTextHeight, pPaint);
            // Uncomment to render titlePadding.
            // pPaint.setColor(Color.RED);
            // pCanvas.drawRect(mTextXLeft - mTitlePadding, mTopPadding, mTextXLeft, mTopPadding +
            // mTextHeight, pPaint);
            // pCanvas.drawRect(mTextXRight, mTopPadding, mTextXRight + mTitlePadding, mTopPadding +
            // mTextHeight, pPaint);
        }

        private void renderSelectedTitle(Canvas pCanvas, Paint pPaint) {
            pPaint.setColor(mColorSelectedText);
            pPaint.setFakeBoldText(false);
            pCanvas.drawText(mText, mTextXLeft, mTopPadding + mTextHeight, pPaint);
            // Uncomment to render titlePadding.
            // pPaint.setColor(Color.RED);
            // pCanvas.drawRect(mTextXLeft - mTitlePadding, mTopPadding, mTextXLeft, mTopPadding +
            // mTextHeight, pPaint);
            // pCanvas.drawRect(mTextXRight, mTopPadding, mTextXRight + mTitlePadding, mTopPadding +
            // mTextHeight, pPaint);
        }

        private void renderSideTitle(Canvas pCanvas, Paint pPaint) {
            pPaint.setColor(mColorSideText);
            pPaint.setFakeBoldText(false);
            pCanvas.drawText(mText, mTextXLeft, mTopPadding + mTextHeight, pPaint);
            // Uncomment to render titlePadding.
            // pPaint.setColor(Color.RED);
            // pCanvas.drawRect(mTextXLeft - mTitlePadding, mTopPadding, mTextXLeft, mTopPadding +
            // mTextHeight, pPaint);
            // pCanvas.drawRect(mTextXRight, mTopPadding, mTextXRight + mTitlePadding, mTopPadding +
            // mTextHeight, pPaint);
        }

        public void clipCurrentTitle() {
            float currentRight = mTextXRight;
            float maxRightWhenOutside = mClipPadding;
            float currentLeft = mTextXLeft;
            float maxLeftWhenOutside = mPageWidth - mClipPadding;
            // This happens only if clipping is big enough so that less than half part of the text
            // is clipped
            // In that case, there is a "jump".
            if (currentRight < maxRightWhenOutside) {
                float newRight = maxRightWhenOutside;

                float distanceToStickToBorder = newRight - mTextXRight;
                shift(distanceToStickToBorder);
            } else if (currentLeft > maxLeftWhenOutside) {
                float newLeft = maxLeftWhenOutside;

                float distanceToStickToBorder = newLeft - mTextXLeft;
                shift(distanceToStickToBorder);
            }
        }

        public void clipLeftBorderTitle(PageTitleInfo nextPageTitleInfo) {
            float currentRight = mTextXRight;
            float maxRightWhenOutside = mClipPadding;
            if (currentRight < maxRightWhenOutside) {
                float maxRightWithRespectToCurrentPage = nextPageTitleInfo.mTextXLeft - mTitlePadding;
                float newRight = Math.min(maxRightWithRespectToCurrentPage, maxRightWhenOutside);

                float distanceToStickToBorder = newRight - mTextXRight;
                shift(distanceToStickToBorder);
            }
        }

        public void clipRightBorderTitle(PageTitleInfo previousPageTitleInfo) {
            float currentLeft = mTextXLeft;
            float maxLeftWhenOutside = mPageWidth - mClipPadding;
            if (currentLeft > maxLeftWhenOutside) {
                float maxLeftWithRespectToCurrentPage = previousPageTitleInfo.mTextXRight + mTitlePadding;
                float newLeft = Math.max(maxLeftWithRespectToCurrentPage, maxLeftWhenOutside);

                float distanceToStickToBorder = newLeft - mTextXLeft;
                shift(distanceToStickToBorder);
            }
        }
    }


    private void cacheViewPagerState(int pFirstVisiblePage, int pOffsetOfFirstVisiblePage) {
        float halfWidth = mPageWidth * 0.5f;
        float offsetOfFirstVisiblePage = pOffsetOfFirstVisiblePage;

        mFirstVisiblePage = pFirstVisiblePage;
        mOffsetOfFirstVisiblePage = pOffsetOfFirstVisiblePage;

        // If current page is on the right side of the screen.
        if (offsetOfFirstVisiblePage > halfWidth) {
            mCurrentPage = (pFirstVisiblePage + 1);
            mCurrentOffsetInPercent = (mPageWidth - offsetOfFirstVisiblePage) / mPageWidth;
        }
        // Current page is on the left side of the screen.
        else {
            mCurrentPage = pFirstVisiblePage;
            mCurrentOffsetInPercent = offsetOfFirstVisiblePage / mPageWidth;
        }
    }

    /**
     * Update page titles position according to the current ViewPager scroll state/position
     */
    public void update(Paint pPaint) {
        // We can display at most 3 page titles at the same time.
        // Need a -1 if current page is entirely visible and the left one is not... => TODO Explain
        // later!!!
        int firstPotentiallyVisiblePage = Math.max(mFirstVisiblePage - 1, 0);
        int lastPotentiallyVisiblePage = Math.min(mFirstVisiblePage + 2, mTitleProvider.getTitleCount() - 1);

        mPageTitleInfosSize = 0;
        // If there is at least one page to display.
        if ((firstPotentiallyVisiblePage >= 0) && (lastPotentiallyVisiblePage >= 0)) {
            for (int i = firstPotentiallyVisiblePage; i <= lastPotentiallyVisiblePage; ++i) {
                int indexRelativeToCurrentPage = i - mFirstVisiblePage;
                mPageTitleInfos[mPageTitleInfosSize].update(mTitleProvider.getTitle(i),
                                                            pPaint,
                                                            indexRelativeToCurrentPage,
                                                            mOffsetOfFirstVisiblePage);
                ++mPageTitleInfosSize;
            }
        }

        // Cache current page infos.
        int currentPageIndex = mCurrentPage - firstPotentiallyVisiblePage;
        // Save the current page.
        mCurrentPageTitleInfo = mPageTitleInfos[currentPageIndex];
        mCurrentPageTitleInfo.mIsCurrentPage = true;
    }

    /**
     * If left and right page titles are not visible, they are shifted so that a small part is
     * visible on the side.
     */
    public void clipTitles() {
        for (int i = 0; i < mPageTitleInfosSize; ++i) {
            PageTitleInfo PageTitleInfo = mPageTitleInfos[i];

            if (PageTitleInfo.mIsCurrentPage) {
                PageTitleInfo.clipCurrentTitle();
            } else {
                if (PageTitleInfo.mPageXLeft < mCurrentPageTitleInfo.mPageXLeft) {
                    // The following if is just a security but shouldn't be necessary.
                    if ((i + 1) < mPageTitleInfosSize) {
                        PageTitleInfo.clipLeftBorderTitle(mPageTitleInfos[i + 1]);
                    }
                } else {
                    // The following if is just a security but shouldn't be necessary.
                    if ((i - 1) >= 0) {
                        PageTitleInfo.clipRightBorderTitle(mPageTitleInfos[i - 1]);
                    }
                }
            }
        }
    }

    /**
     * Display page titles on the canvas.
     */
    public void render(Canvas canvas) {
        for (int i = 0; i < mPageTitleInfosSize; ++i) {
            mPageTitleInfos[i].render(canvas, mVisibleRect, mPaintText);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTitleProvider.getTitleCount() == 0) return;

        update(mPaintText);
        clipTitles();
        render(canvas);
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        if (super.onTouchEvent(ev)) {
            return true;
        }
        if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0)) {
            return false;
        }
        return mViewPager.onTouchEvent(ev);
    };

    @Override
    public void setViewPager(ViewPager view) {
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        if (!(adapter instanceof TitleProvider)) {
            throw new IllegalStateException("ViewPager adapter must implement TitleProvider to be used with TitlePageIndicator.");
        }
        mViewPager = view;
        mViewPager.setOnPageChangeListener(this);

        mTitleProvider = (TitleProvider) adapter;
        invalidate();
    }

    // @Override
    // public void setViewPager(ViewPager view, int initialPosition) {
    // setViewPager(view);
    // setCurrentItem(initialPosition);
    // }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);

        cacheViewPagerState(item, 0);
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mCurrentScrollState = state;
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // It is unlikely that two consecutive pageScrolledEvent occurs at the same position
        // (although that happens
        // regularly). This test avoids a useless redraw of the view in that case.
        if (mOffsetOfFirstVisiblePage != positionOffsetPixels) {
            cacheViewPagerState(position, positionOffsetPixels);

            // Security to make sure we don't go beyond the last page.
            int pageCount = mViewPager.getAdapter().getCount();
            if (mFirstVisiblePage >= pageCount) {
                // setCurrentItem() already invalidates view display.
                setCurrentItem(pageCount - 1);
            }
            // Update the view anyway.
            else {
                invalidate();
            }
        }

        // Transfer event to any listener.
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mCurrentScrollState == ViewPager.SCROLL_STATE_IDLE) {
            cacheViewPagerState(position, mOffsetOfFirstVisiblePage);
            invalidate();
        }

        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Measure our width in whatever mode specified.
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        mPageWidth = measuredWidth;

        // Determine our height.
        int measuredHeight;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            // We were told how big to be
            measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            // Calculate the text bounds
            float textSize = mPaintText.descent() - mPaintText.ascent(); // TODO
            measuredHeight = (int) (mTopPadding + textSize + mFooterPadding);
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onSizeChanged(int pWidth, int pHeight, int pOldWidth, int pOldHeight) {
        super.onSizeChanged(pWidth, pHeight, pOldWidth, pOldHeight);
        mVisibleRect = new RectF(0, 0, pWidth, pHeight);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        cacheViewPagerState(savedState.currentPage, 0);
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mFirstVisiblePage;
        return savedState;
    }


    static class SavedState extends BaseSavedState
    {
        int currentPage;


        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }


        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    public float getFooterIndicatorPadding() {
        return mFooterPadding;
    }

    public void setFooterIndicatorPadding(float footerIndicatorPadding) {
        mFooterPadding = footerIndicatorPadding;
        invalidate();
    }

    public int getSelectedColor() {
        return mColorSelectedText;
    }

    public void setSelectedColor(int selectedColor) {
        mColorSelectedText = selectedColor;
        invalidate();
    }

    public boolean isSelectedBold() {
        return mHighlightedTextIsBold;
    }

    public void setSelectedBold(boolean selectedBold) {
        mHighlightedTextIsBold = selectedBold;
        invalidate();
    }

    public int getTextColor() {
        return mColorSideText;
    }

    public void setTextColor(int textColor) {
        mPaintText.setColor(textColor);
        mColorSideText = textColor;
        invalidate();
    }

    public float getTextSize() {
        return mPaintText.getTextSize();
    }

    public void setTextSize(float textSize) {
        mPaintText.setTextSize(textSize);
        invalidate();
    }

    public float getTitlePadding() {
        return mTitlePadding;
    }

    public void setTitlePadding(float titlePadding) {
        mTitlePadding = titlePadding;
        invalidate();
    }

    public float getTopPadding() {
        return mTopPadding;
    }

    public void setTopPadding(float topPadding) {
        mTopPadding = topPadding;
        invalidate();
    }

    public float getClipPadding() {
        return mClipPadding;
    }

    public void setClipPadding(float clipPadding) {
        mClipPadding = clipPadding;
        invalidate();
    }

    public void setTypeface(Typeface typeface) {
        mPaintText.setTypeface(typeface);
        invalidate();
    }

    public Typeface getTypeface() {
        return mPaintText.getTypeface();
    }
}
