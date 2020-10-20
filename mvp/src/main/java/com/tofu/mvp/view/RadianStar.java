package com.tofu.mvp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.tofu.mvp.R;
import com.tofu.mvp.anim.Anim;
import com.tofu.mvp.util.DPUtil;


/**
 * create file time : 2020/9/24
 * create user : wxl
 * subscribe :
 */
public class RadianStar extends LinearLayout {

    private int star_res_id = R.drawable.ic_full;
    private int star_empty_id = R.drawable.ic_empty;
    private float radian = 15f;
    private int starCount = 5;
    private int starSize;
    private int count = 0;
    private int h_margin;
    private boolean autoHeight = false;
    private boolean enableAnim = false;
    private int position = 0;
    private Handler handler;

    public RadianStar(Context context) {
        this(context, null);
    }

    public RadianStar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RadianStar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttribute(context, attrs);
    }

    @SuppressLint("HandlerLeak")
    private void loadAttribute(Context context, AttributeSet attrs) {
        starSize = DPUtil.dip2px(10);
        h_margin = DPUtil.dip2px(10);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadianStar);
            if (typedArray != null) {
                radian = typedArray.getFloat(R.styleable.RadianStar_radian, 15f);
                star_res_id = typedArray.getResourceId(R.styleable.RadianStar_star, R.drawable.ic_full);
                star_empty_id = typedArray.getResourceId(R.styleable.RadianStar_empty_star, R.drawable.ic_empty);
                starCount = typedArray.getInt(R.styleable.RadianStar_count, 5);
                count = typedArray.getInt(R.styleable.RadianStar_count, 0);
                h_margin = typedArray.getDimensionPixelSize(R.styleable.RadianStar_h_margin, h_margin);
                starSize = typedArray.getDimensionPixelSize(R.styleable.RadianStar_starSize, starSize);
                autoHeight = typedArray.getBoolean(R.styleable.RadianStar_auto_height, false);
                enableAnim = typedArray.getBoolean(R.styleable.RadianStar_enable_anim,false);
                typedArray.recycle();
            }
        }
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(HORIZONTAL);
        handler = new Handler(){
            @Override
            public void dispatchMessage(@NonNull Message msg) {
                playStarLightAnim(++position);
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setStar(int starId) {
        this.star_res_id = starId;
    }


    public void setStarEmpty(int starEmptyId) {
        this.star_empty_id = starEmptyId;
    }

    public void setRadian(float radian) {
        this.radian = radian;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setHMargin(int margin) {
        this.h_margin = margin;
    }

    public void setStarSize(int starSize) {
        this.starSize = starSize;
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
    }

    public void setEnableAnim(boolean enableAnim) {
        this.enableAnim = enableAnim;
    }

    public void show() {
        if (starCount > 0) {
            removeAllViews();
            if (starCount % 2 == 0) {
                createEvenNumberStar();
            } else {
                createOddNumberStar();
            }
            if (autoHeight) {
                changeHeight();
            }
        }
    }

    /**
     * 偶数星星
     */
    private void createEvenNumberStar() {
        createEvenLeftStar();
        createEvenRightStar();
        setStarLight();
    }


    /**
     * 奇数星星
     */
    private void createOddNumberStar() {
        createOddLeftStar();
        createOddTopStar();
        createOddRightStar();
        setStarLight();
    }


    /***
     * 点亮星星
     */
    private void setStarLight() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            RelativeLayout childAt = (RelativeLayout) getChildAt(i);
            ImageView star = (ImageView) childAt.getChildAt(0);
            if (i < count) {
                Glide.with(star).load(star_res_id).error(R.drawable.ic_full).into(star);
            } else {
                Glide.with(star).load(star_empty_id).error(R.drawable.ic_empty).into(star);
            }
            if(enableAnim) {
                star.setVisibility(INVISIBLE);
                loadStarLightAnim(star, i);
            }
        }
        if(enableAnim) {
            playStarLightAnim(position);
        }
    }

    /**
     * 点亮星星动画
     */
    private void loadStarLightAnim(ImageView star, int j) {
        star.setTag(Anim.with(star)
                .scanXY()
                .setFromX(0)
                .setToX(1)
                .setFromY(0)
                .setToY(1)
                .setDuration(500)
                .setInterpolator(new BounceInterpolator())
                .build()
        );
    }


    /**
     * 开始播放
     *
     * @param i
     */
    private void playStarLightAnim(int i) {
        RelativeLayout childAt = (RelativeLayout) getChildAt(i);
        if(childAt != null) {
            ImageView star = (ImageView) childAt.getChildAt(0);
            Anim.stop(star);
            Anim.Builder builder = (Anim.Builder) star.getTag();
            builder.start();
            if (i < getChildCount()) {
                handler.sendEmptyMessageDelayed(123, 300);
            }
        }
    }


    private void releaseStarLightAnim() {
        if(handler != null) {
            handler.removeMessages(123);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseStarLightAnim();
    }

    /**
     * 创建星星父级控件
     *
     * @param j
     * @return
     */
    private RelativeLayout generateStarParent(int j) {
        RelativeLayout parent = new RelativeLayout(getContext());
        parent.setGravity(Gravity.CENTER);
        int size = starSize + h_margin / 2;
        LayoutParams params = new LayoutParams(size, size);
        int t = Math.abs((int) (Math.tan(radian) * h_margin / 2));
        params.topMargin = t * j;
        parent.setLayoutParams(params);
        ImageView imageView = generateStarView();
        parent.addView(imageView);
        return parent;
    }

    /**
     * 改变高度
     */
    private void changeHeight() {
        boolean isOdd = starCount % 2 != 0;
        int t = Math.abs((int) (Math.tan(radian) * h_margin / 2));
        int size = starSize + h_margin / 2;
        int height = (starCount / 2) * t + size;
        if (!isOdd) {
            height -= t;
        }
        getLayoutParams().height = height;
        requestLayout();
    }

    /**
     * 创建星星
     *
     * @return
     */
    private ImageView generateStarView() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(starSize, starSize);
        imageView.setLayoutParams(params);
        return imageView;
    }


    /**
     * 创建偶数左边的星星
     */
    private void createEvenLeftStar() {
        int i = starCount / 2;
        for (int j = i; j > 0; j--) {
            addView(generateStarParent(j - 1));
        }
    }


    /**
     * 创建偶数右边的星星
     */
    private void createEvenRightStar() {
        int i = starCount / 2;
        for (int j = 0; j < i; j++) {
            addView(generateStarParent(j));
        }
    }


    /**
     * 创建奇数第一颗星
     */
    private void createOddTopStar() {
        addView(generateStarParent(0));
    }

    /**
     * 创建奇数左边的星星
     */
    private void createOddLeftStar() {
        int i = (starCount - 1) / 2;
        for (int j = i; j > 0; j--) {
            addView(generateStarParent(j));
        }
    }

    /**
     * 创建奇数右边的星星
     */
    private void createOddRightStar() {
        int i = (starCount - 1) / 2;
        for (int j = 0; j < i; j++) {
            addView(generateStarParent(j + 1));
        }
    }

}
