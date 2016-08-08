package com.rqg.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rqg.common.R;

/**
 * *Created by rqg on 8/8/16.
 */
public class SingleView extends View {
    private static final String TAG = "SingleView";
    private int mActiveColor, mNegativeColor;
    private Paint mPaint;
    private int mColumeCount;
    private int mSignal;
    private RectF[] mRectFs;
    private float mGapRatio;
    float mStartRatio;

    private float mRadius;


    public SingleView(Context context) {
        this(context, null);
    }

    public SingleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SingleView,
                0, 0);


        try {
            mActiveColor = a.getColor(R.styleable.SingleView_active_color, Color.BLUE);
            mNegativeColor = a.getColor(R.styleable.SingleView_negative_color, Color.GRAY);
            mColumeCount = a.getInt(R.styleable.SingleView_column_count, 5);
            mGapRatio = a.getFloat(R.styleable.SingleView_gap_ratio, 0.6f);
            mStartRatio = a.getFloat(R.styleable.SingleView_start_ratio, 1f);
        } finally {
            a.recycle();
        }

    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        dealSingleRectFs(getMeasuredHeight(), getMeasuredWidth());

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mActiveColor);
        for (int i = 0; i < mColumeCount; i++) {
            Log.d(TAG, "onDraw: " + mRectFs[i]);
            canvas.drawRoundRect(mRectFs[i], mRadius, mRadius, mPaint);

            if (mSignal == i) {
                mPaint.setColor(mNegativeColor);
            }
        }
    }

    private void dealSingleRectFs(int height, int width) {
        float sw = width * (1 - mGapRatio) / mColumeCount;
        float gw = sw * mGapRatio / (1 - mGapRatio);


        mRadius = sw / 2f;


        mRectFs = new RectF[mColumeCount];

        float left = 0;
        float tStep = ((float) height - sw) / ((float) mColumeCount + mStartRatio);
        float top = height - tStep * mStartRatio;


        for (int i = 0; i < mColumeCount; i++) {
            RectF r = new RectF();
            r.set(left, top, left + sw, height);
            top -= tStep;
            left += sw + gw;

            mRectFs[i] = r;
        }

    }

    public int getSignal() {
        return mSignal;
    }

    public void setSignal(int signal) {
        mSignal = signal;
    }

    public void setActiveColor(int activeColor) {
        mActiveColor = activeColor;
        invalidate();
    }
}
