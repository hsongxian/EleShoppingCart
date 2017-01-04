package com.song.eleshoppingcart.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.song.eleshoppingcart.R;

import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/7 0007.
 */
public class ShoppingAddView extends FrameLayout {

    @Bind(R.id.iv_remove)
    ImageView mIvRemove;
    @Bind(R.id.tv_acount)
    TextView mTvAcount;
    @Bind(R.id.iv_add)
    ImageView mIvAdd;
    private OnShoppingClickListener mOnShoppingClickListener;

    public ShoppingAddView(Context context) {
        this(context, null);
    }

    public ShoppingAddView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingAddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_shopping_add, this, false);
        addView(inflate);
        ButterKnife.bind(this, inflate);

    }

    @OnClick({R.id.iv_remove, R.id.iv_add})
    public void onClick(View view) {
        if (mOnShoppingClickListener == null) return;
        switch (view.getId()) {
            case R.id.iv_remove:
                remove(view);
                break;
            case R.id.iv_add:
                add(view);
                break;
        }
    }

    private void remove(View view) {
        int count = getNum();
        count --;
        if (count <= 0) {
            count = 0;
            Animation hiddenAnimation = getHiddenAnimation();
            mIvRemove.setAnimation(hiddenAnimation);
            hiddenAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIvRemove.setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
//            mIvRemove.setVisibility(View.GONE);
            mTvAcount.setVisibility(View.GONE);
        }
        mTvAcount.setText(String.valueOf(count));
        mOnShoppingClickListener.onMinusClick(view,count);

    }

    private void add(View view) {
        int count = getNum();
        count ++;
        if (count <= 1 || mIvRemove.getVisibility() != View.VISIBLE) {
            mIvRemove.setAnimation(getShowAnimation());
            mIvRemove.setVisibility(View.VISIBLE);
            mTvAcount.setVisibility(View.VISIBLE);
        }
        mTvAcount.setText(String.valueOf(count));
        mOnShoppingClickListener.onAddClick(view,count);

    }

    public Integer getNum() {
        return TextView2Integer(mTvAcount);
    }

    //显示减号的动画
    private Animation getShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 2f
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    //隐藏减号的动画
    private Animation getHiddenAnimation() {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 2f
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    public void setOnShoppingClickListener(OnShoppingClickListener onShoppingClickListener) {
        mOnShoppingClickListener = onShoppingClickListener;
    }


    public interface OnShoppingClickListener {
        void onAddClick(View view, int num);

        void onMinusClick(View view, int num);
    }

    public static int TextView2Integer(TextView textView) {
        int i = 0;
        try {
            String text = textView.getText().toString();

            if (!TextUtils.isEmpty(text)){
                if (text.startsWith("￥"))
                    text = text.substring(1,text.length());
                i =Integer.valueOf(text);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i ;
    }

}
