package com.song.eleshoppingcart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.song.eleshoppingcart.base.BaseActivity;
import com.song.eleshoppingcart.base.BaseFragment;
import com.song.eleshoppingcart.base.FragemtnPager;
import com.song.eleshoppingcart.base.Utils;
import com.song.eleshoppingcart.bean.Commodity;
import com.song.eleshoppingcart.fragment.FragmentCommodity;
import com.song.eleshoppingcart.fragment.FragmentMerchantInfo;
import com.song.eleshoppingcart.listener.OnPageChangeListener;
import com.song.eleshoppingcart.listener.OnShoppingItemClickListener;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class ShopActivity extends BaseActivity {


    @Bind(R.id.image)
    ImageView mImage;
    @Bind(R.id.title_bar)
    Toolbar mTitleBar;
    @Bind(R.id.colltoolbar_layout)
    CollapsingToolbarLayout mColltoolbarLayout;
    @Bind(R.id.appbar)
    AppBarLayout mAppbar;
    @Bind(R.id.container)
    ViewPager mContainer;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.tv_sum)
    TextView mTvSum;
    @Bind(R.id.submit)
    Button mSubmit;
    @Bind(R.id.iv_car)
    ImageView mIvCar;
    @Bind(R.id.rl_iv_car)
    RelativeLayout mRlIvCar;
    @Bind(R.id.tv_num)
    TextView mTvNum;
    @Bind(R.id.rl_car_bar)
    RelativeLayout mRlCarBar;
    @Bind(R.id.tv_distribution_price)
    TextView mTvDistributionPrice;
    @Bind(R.id.tv_distribution_price_bold)
    TextView mTvDistributionPriceBold;


    private boolean mShow = true;
    private FragmentCommodity mFragmentCommodity;
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private BottomSheetDialog mBottomSheetDialog;


    public static void launch(Context context) {
        Intent intent = new Intent(context, ShopActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, String title) {
        Intent intent = new Intent(context, ShopActivity.class);
        intent.putExtra(KEY, title);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(mTitleBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mValue);
        mColltoolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);//设置打开时的文字颜色

        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        initViewPager();

        mFragmentCommodity.setOnShoppingItemClickListener(new OnShoppingItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public void onAddClick(View view, int num, int position) {
                setAnim(view);
                if (num == 1) {
                    mIvCar.setImageResource(R.drawable.shopping_cart_blue);
                    mTvDistributionPrice.setVisibility(View.VISIBLE);
                    mTvDistributionPriceBold.setVisibility(View.GONE);
                    mSubmit.setVisibility(View.VISIBLE);
                    mTvSum.setTextColor(getResources().getColor(R.color.grey_400));
                    mTvNum.setVisibility(View.VISIBLE);

                }
                calculatedPrice(num, position);
            }

            @Override
            public void onMinusClick(View view, int num, int position) {
                if (num < 0) return;
                if (num == 0) {
                    mIvCar.setImageResource(R.drawable.shopping_cart_gray);
                    mTvDistributionPrice.setVisibility(View.GONE);
                    mTvDistributionPriceBold.setVisibility(View.VISIBLE);
                    mSubmit.setVisibility(View.GONE);
                    mTvSum.setTextColor(Color.WHITE);
                    mTvNum.setVisibility(View.GONE);
                }
                calculatedPrice(num, -position);

            }
        });

//        createBottomSheetDialog();

    }

    public void createBottomSheetDialog() {
        mBottomSheetDialog = new BottomSheetDialog(this);
//        View view = LayoutInflater.from(this).inflate(,null,false);
        mBottomSheetDialog.setContentView(R.layout.dialog_shopping_car);
        setBehaviorCallback();
        RecyclerView recyclerView = (RecyclerView) mBottomSheetDialog.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new CommonAdapter<Commodity>(
                mContext,R.layout.item_commodity_list,mFragmentCommodity.mCommodity) {
            @Override
            public void convert(ViewHolder holder, Commodity elementsBean, int position) {
//                ContentVH contentVH = (ContentVH) holder;
                holder.setText(R.id.tv_name,elementsBean.getName())
                        .setText(R.id.tv_content,elementsBean.getContent())
                        .setText(R.id.tv_sum,elementsBean.getSum());
            }
        });

    }

    private void setBehaviorCallback() {
        View view = mBottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetDialog.dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }


    private void calculatedPrice(int num, int position) {
        mTvNum.setText(String.valueOf(num));
        int sum = Utils.TextView2Integer(mTvSum);
        sum += position;
        mTvSum.setText("￥" + String.valueOf(sum));
    }

    private void initViewPager() {
        String[] titles = {"商品", "商家"};
        List<BaseFragment> fragments = new ArrayList<>();
        mFragmentCommodity = FragmentCommodity.newInstance();
        fragments.add(mFragmentCommodity);
        fragments.add(FragmentMerchantInfo.newInstance());
        mContainer.setAdapter(new FragemtnPager(getSupportFragmentManager(),
                fragments, titles));
        mTabLayout.setupWithViewPager(mContainer);

        mContainer.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            protected void onViewPageSelected(int position) {
                show();
            }
        });

    }


    private void show() {
        if (mShow) {
            mRlCarBar
                    .animate()
                    .translationY(mRlCarBar.getHeight())
                    .setDuration(300)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else {
            mRlCarBar
                    .animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
        mShow = !mShow;
    }


    /**
     *
     * @param v
     */
    public void setAnim(final View v) {
        //      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView mAnimatorImageView = new ImageView(this);
        int dip = Utils.dp2px(mContext,20);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dip, dip);
        mCoordinatorLayout.addView(mAnimatorImageView, params);
        mAnimatorImageView.setImageResource(R.drawable.number);
//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        mCoordinatorLayout.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        v.getLocationInWindow(startLoc);


        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        mRlIvCar.getLocationInWindow(endLoc);

//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
//        float startX = startLoc[0] - parentLocation[0] + v.getWidth() / 2;
//        float startY = startLoc[1] - parentLocation[1] + v.getHeight() / 2;
        float startX = startLoc[0] - parentLocation[0];
        float startY = startLoc[1] - parentLocation[1] - getStatusBarHeight(); // 减去状态栏高度
        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + mRlIvCar.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1] - mRlCarBar.getHeight() / 2;

        mAnimatorImageView.setY(startY);
        mAnimatorImageView.setX(startX);

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
//        path.quadTo((startX + toX)/8 , startY, toX, toY);
        path.quadTo(startX / 4, (float) (startY / 1.5), toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);
        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(300);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                mAnimatorImageView.setTranslationX(mCurrentPosition[0]);
                mAnimatorImageView.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
        valueAnimator.start();

//      六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                mCoordinatorLayout.removeView(mAnimatorImageView);
                startCarIconScaleAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

    }

    private void startCarIconScaleAnimation() {
        ScaleAnimation sa = new ScaleAnimation(0.7f, 1f, 0.7f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(600);
        sa.setInterpolator(new OvershootInterpolator());
        mRlIvCar.startAnimation(sa);

    }


    @OnClick({R.id.submit, R.id.rl_car_bar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                break;
            case R.id.rl_car_bar:
                mBottomSheetDialog.show();
                break;
        }
    }

    /**
     * 获取通知栏高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId =mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
