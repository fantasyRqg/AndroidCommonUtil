package com.rqg.common.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.rqg.common.R;

import java.security.InvalidParameterException;


/**
 * *Created by rqg on 5/18/16.
 */
public class SnackView {
    static final int ANIMATION_DURATION = 250;
    static final int ANIMATION_FADE_DURATION = 180;
    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();


    private SnackViewLayout mView;
    private ViewGroup mParent;
    private AccessibilityManager mAccessibilityManager;

    /**
     * @param view   any view
     * @param parent must be frameLayout or coordinatorLayout
     */
    public SnackView(View view, ViewGroup parent) {
        if (!(parent instanceof FrameLayout || parent instanceof CoordinatorLayout)) {
            throw new InvalidParameterException("parent should be one of FrameLayout and CoordinatorLayout");
        }
        mAccessibilityManager = (AccessibilityManager) view.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        mParent = parent;
        initView(view);
    }


    private void initView(View view) {
        mView = new SnackViewLayout(view.getContext());
        mView.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams p;

        if (mParent instanceof FrameLayout) {
            FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            fp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            p = fp;
        } else {
            CoordinatorLayout.LayoutParams cp = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            p = cp;
        }
        mView.setLayoutParams(p);
        mView.addView(view);
    }


    /**
     * Returns true if we should animate the Snackbar view in/out.
     */
    private boolean shouldAnimate() {
        return !mAccessibilityManager.isEnabled();
    }

    public void show() {
        if (mParent.indexOfChild(mView) == -1) {
            mParent.addView(mView);
        } else {
            return;
        }

        if (ViewCompat.isLaidOut(mView)) {
            if (shouldAnimate()) {
                // If animations are enabled, animate it in
                animateViewIn();
            }
        } else {
            // Otherwise, add one of our layout change listeners and show it in when laid out
            mView.setOnLayoutChangeListener(new SnackViewLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    mView.setOnLayoutChangeListener(null);

                    if (shouldAnimate()) {
                        // If animations are enabled, animate it in
                        animateViewIn();
                    }
                }
            });
        }
    }

    public void dismiss() {
        if (mParent.indexOfChild(mView) == -1)
            return;

        if (shouldAnimate() && mView.getVisibility() == View.VISIBLE) {
            animateViewOut();
        } else {
            mParent.removeView(mView);
        }
    }


    private void animateViewOut() {
        Animation anim = AnimationUtils.loadAnimation(mView.getContext(),
                R.anim.snack_view_out);
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setDuration(ANIMATION_DURATION);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mParent.removeView(mView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mView.startAnimation(anim);
    }

    private void animateViewIn() {
        Animation anim = AnimationUtils.loadAnimation(mView.getContext(),
                R.anim.snack_view_in);
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setDuration(ANIMATION_DURATION);
        mView.startAnimation(anim);
    }

    public static class SnackViewLayout extends LinearLayout {
        interface OnLayoutChangeListener {
            void onLayoutChange(View view, int left, int top, int right, int bottom);
        }

        interface OnAttachStateChangeListener {
            void onViewAttachedToWindow(View v);

            void onViewDetachedFromWindow(View v);
        }

        private OnLayoutChangeListener mOnLayoutChangeListener;
        private OnAttachStateChangeListener mOnAttachStateChangeListener;

        public SnackViewLayout(Context context) {
            this(context, null);
        }

        public SnackViewLayout(Context context, AttributeSet attrs) {
            super(context, attrs);

            ViewCompat.setAccessibilityLiveRegion(this,
                    ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE);
            ViewCompat.setImportantForAccessibility(this,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }


        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (mOnLayoutChangeListener != null) {
                mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
            }
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewAttachedToWindow(this);
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewDetachedFromWindow(this);
            }
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            mOnLayoutChangeListener = onLayoutChangeListener;
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
            mOnAttachStateChangeListener = listener;
        }
    }
}
