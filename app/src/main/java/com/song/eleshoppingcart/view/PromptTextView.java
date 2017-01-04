package com.song.eleshoppingcart.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class PromptTextView extends TextView {

    private Paint mPaint;

    public PromptTextView(Context context) {
        this(context, null);
    }

    public PromptTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromptTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {

    }


    private int mAnimationDuration = 200;


    /**
     * @param animationDuration hide and show animation time
     * @return this, to allow builder pattern
     */
    public PromptTextView setAnimationDuration(int animationDuration) {
        this.mAnimationDuration = animationDuration;
        return this;
    }


    /**
     * @return this, to allow builder pattern
     */
    public PromptTextView toggle() {
        return toggle(true);
    }

    /**
     * @param animate whether to animate the change
     * @return this, to allow builder pattern
     */
    public PromptTextView toggle(boolean animate) {
        if (isHidden()) {
            return show(animate);
        } else {
            return hide(animate);
        }
    }

    /**
     * @return this, to allow builder pattern
     */
    public PromptTextView show() {
        return show(true);
    }

    /**
     * @param animate whether to animate the change
     * @return this, to allow builder pattern
     */
    public PromptTextView show(boolean animate) {
        TextView textView = this;
        if (animate) {
            textView.setScaleX(0);
            textView.setScaleY(0);
            textView.setVisibility(View.VISIBLE);
            ViewPropertyAnimatorCompat animatorCompat = ViewCompat.animate(textView);
            animatorCompat.cancel();
            animatorCompat.setDuration(mAnimationDuration);
            animatorCompat.scaleX(1).scaleY(1);
            animatorCompat.setListener(null);
            animatorCompat.start();
        } else {
            textView.setScaleX(1);
            textView.setScaleY(1);
            textView.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * @return this, to allow builder pattern
     */
    public PromptTextView hide() {
        return hide(true);
    }

    /**
     * @param animate whether to animate the change
     * @return this, to allow builder pattern
     */
    public PromptTextView hide(boolean animate) {
        TextView textView = this;
        if (animate) {
            ViewPropertyAnimatorCompat animatorCompat = ViewCompat.animate(textView);
            animatorCompat.cancel();
            animatorCompat.setDuration(mAnimationDuration);
            animatorCompat.scaleX(0).scaleY(0);
            animatorCompat.setListener(new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {
                    // Empty body
                }

                @Override
                public void onAnimationEnd(View view) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(View view) {
                    view.setVisibility(View.GONE);
                }
            });
            animatorCompat.start();
        } else {
            textView.setVisibility(View.GONE);
        }

        return this;
    }

    /**
     * @return if the badge is hidden
     */
    public boolean isHidden() {
        boolean isHidden = GONE == getVisibility() || INVISIBLE == getVisibility();
        return isHidden;
    }
}
