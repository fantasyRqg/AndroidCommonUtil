package com.rqg.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.rqg.common.R;
import com.rqg.common.util.DisplayUtil;

/**
 * *Created by rqg on 6/14/16.
 */
public class SideView extends View {
    private static final String TAG = "SideView";

    private String mLeftText, mRightText;

    private int mFocusColor = 0, mUnFocusColor;

    private float mTextSize;

    private int mTextWidth, mTextHeight;

    private float mRoundRadius;

    private RectF mRectF = new RectF(), mBounds = new RectF();

    private boolean mSelectLeft = true;

    private float mStrokeWidth;

    private float mTextPaddingVertical, mTextPaddingHorizontal;

    private float mTextYdiff = 0;

    private Paint mPaint;

    private GestureDetectorCompat mGestureDetector;

    private OnSideViewClick mListener;

    public SideView(Context context) {
        this(context, null);
    }

    public SideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SideView,
                0, 0);

        try {
            mLeftText = a.getString(R.styleable.SideView_leftText);
            mRightText = a.getString(R.styleable.SideView_rightText);
            mFocusColor = a.getColor(R.styleable.SideView_focusColor, Color.RED);
            mUnFocusColor = a.getColor(R.styleable.SideView_unfocusColor, Color.WHITE);
            mTextSize = a.getDimension(R.styleable.SideView_textSize, 20f);
            mRoundRadius = a.getDimension(R.styleable.SideView_radius, DisplayUtil.dp2Px(context, 5));
            mStrokeWidth = a.getDimension(R.styleable.SideView_strokeWidth, DisplayUtil.dp2Px(context, 2));
            mTextPaddingVertical = a.getDimension(R.styleable.SideView_textPaddingVertical, DisplayUtil.dp2Px(context, 5));
            mTextPaddingHorizontal = a.getDimension(R.styleable.SideView_textPaddingHorizontal, DisplayUtil.dp2Px(context, 5));
        } finally {
            a.recycle();
        }


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);

        mTextYdiff = ((mPaint.descent() + mPaint.ascent()) / 2);


        Rect b = new Rect();


        if (!TextUtils.isEmpty(mLeftText)) {

            mPaint.getTextBounds(mLeftText, 0, mLeftText.length(), b);
        }

        mTextWidth = b.width();
        mTextHeight = b.height();

        if (!TextUtils.isEmpty(mRightText)) {
            mPaint.getTextBounds(mRightText, 0, mRightText.length(), b);
        }

        mTextWidth = b.width() > mTextWidth ? b.width() : mTextWidth;
        mTextHeight = b.height() > mTextHeight ? b.height() : mTextHeight;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

// Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        minw += mTextWidth * 2f + mTextPaddingHorizontal * 4f;
        int w = resolveSizeAndState(minw, widthMeasureSpec, getMeasuredState());

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = getSuggestedMinimumHeight() + getPaddingBottom() + getPaddingTop();
        minh += mTextHeight + mTextPaddingVertical * 2f;
        int h = resolveSizeAndState(minh, heightMeasureSpec, getMeasuredState());

        setMeasuredDimension(w, h);

        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        float pw = (measuredWidth - minw) / 2f + mStrokeWidth;
        float ph = (measuredHeight - minh) / 2f + mStrokeWidth;

        Log.d(TAG, "onMeasure: pw = " + pw + " ph = " + ph + " minh = " + minh + " smh = " + getSuggestedMinimumHeight() + " pb = " + getPaddingBottom() + " pt = " + getPaddingTop());


        mBounds.set(pw + getPaddingLeft(), ph + getPaddingTop(),
                measuredWidth - pw - getPaddingRight(), measuredHeight - ph - getPaddingBottom()
        );

    }

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            float x = e.getX();

            mSelectLeft = x < getWidth() / 2f;

            invalidate();

            if (mListener != null) {
                mListener.onSideClick(SideView.this, mSelectLeft);
            }

            return true;
        }
    };


    @Override
    protected void onDraw(Canvas canvas) {
        mRectF.set(mBounds);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mFocusColor);
        canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);


        mPaint.setStyle(Paint.Style.FILL);
        if (mSelectLeft) {
            mRectF.set(mBounds.left, mBounds.top, mBounds.centerX(), mBounds.bottom);
        } else {
            mRectF.set(mBounds.centerX(), mBounds.top, mBounds.right, mBounds.bottom);
        }
        canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);

        float w4 = mBounds.left + mBounds.width() / 4f;
        float h2 = mBounds.top + mBounds.height() / 2f - mTextYdiff;

        if (!TextUtils.isEmpty(mLeftText)) {
            //left
            mPaint.setColor(!mSelectLeft ? mFocusColor : mUnFocusColor);
            canvas.drawText(mLeftText, w4, h2, mPaint);
        }

        if (!TextUtils.isEmpty(mRightText)) {
            //right
            mPaint.setColor(mSelectLeft ? mFocusColor : mUnFocusColor);
            canvas.drawText(mRightText, getWidth() - w4, h2, mPaint);
        }
    }

    public void setListener(OnSideViewClick listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return true;
    }


    public void checkOneSide(boolean left) {
        mSelectLeft = left;
        invalidate();
    }

    public interface OnSideViewClick {
        void onSideClick(SideView sideView, boolean isLeft);
    }
}
