package com.tofu.mvp.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tofu.mvp.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


import java.util.HashMap;

import static android.view.animation.Animation.ABSOLUTE;
import static android.view.animation.Animation.RESTART;

/**
 * Copyright：贵州玄机科技有限公司
 * Created by wxl on 2020/7/20 16:52
 * Description：
 * Modify time：
 * Modified by：
 */
public class Anim {


    private static HashMap<String,AnimFragment> animFragments = new HashMap<>();

    private static void generateAnimFragment(Context context) {
        if (animFragments.containsKey(context.getClass().getName())) return;
        if (context instanceof RxAppCompatActivity) {
            AnimFragment animFragment = new AnimFragment();
            RxAppCompatActivity activity = (RxAppCompatActivity) context;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction tx = fragmentManager.beginTransaction();
            tx.add(animFragment, "AimFragment").commitAllowingStateLoss();
            animFragments.put(context.getClass().getName(),animFragment);
        }
    }

    public static Builder with(View target) {
        generateAnimFragment(target.getContext());
        if (target.getTag(R.id.anim_view_id) == null) {
            Builder builder = new Builder(target);
            animFragments.get(target.getContext().getClass().getName()).addBuilder(target, builder);
        }
        return animFragments.get(target.getContext().getClass().getName()).getBuilder(target).clearTargetAnim();
    }


    public static void stop(View target) {
        if (target.getTag(R.id.anim_view_id) != null) {
            animFragments.get(target.getContext().getClass().getName()).getBuilder(target).clearTargetAnim();
        }
    }

    public static class Builder {

        private View target;
        private AnimationSet set = new AnimationSet(false);

        private Builder(View target) {
            this.target = target;
        }


        public AbsTypeAnimBuilder alpha() {
            return new AlphaBuilder(this);
        }

        public AbsTypeAnimBuilder translateY() {
            return new TranslateYBuilder(this);
        }

        public AbsTypeAnimBuilder translateX() {
            return new TranslateXBuilder(this);
        }

        public TranslateXYBuilder translateXY() {
            return new TranslateXYBuilder(this);
        }

        public AbsTypeAnimBuilder scanY() {
            return new ScanYBuilder(this);
        }

        public AbsTypeAnimBuilder scanX() {
            return new ScanXBuilder(this);
        }

        public ScanXYBuilder scanXY() {
            return new ScanXYBuilder(this);
        }

        public RotateBuilder rotate() {
            return new RotateBuilder(this);
        }


        public RotateZBuilder rotateZ(){
            return new RotateZBuilder(this);
        }

        private Builder addAnim(AbsTypeAnimBuilder animBuilder) {
            set.addAnimation(animBuilder.animation);
            return this;
        }


        public void start() {
            if (target != null) {
                target.setVisibility(View.VISIBLE);
                target.startAnimation(set);
            }
        }

        public void release() {
            set.cancel();
            set.reset();
            set.getAnimations().clear();
            if (target != null) {
                if(animFragments.containsKey(target.getContext().getClass().getName())) {
                    animFragments.get(target.getContext().getClass().getName()).removeBuilder(target);
                    animFragments.remove(target.getContext().getClass().getName());
                }
                target.setTag(R.id.anim_view_id, null);
                target.clearAnimation();
                target = null;
            }
        }


        private Builder clearTargetAnim() {
            set.cancel();
            set.reset();
            set.getAnimations().clear();
            if (target != null) {
                if(animFragments.containsKey(target.getContext().getClass().getName())) {
                    animFragments.get(target.getContext().getClass().getName()).removeBuilder(target);
                    animFragments.remove(target.getContext().getClass().getName());
                }
                target.setTag(R.id.anim_view_id, null);
                target.clearAnimation();
            }
            return this;
        }

    }


    public static abstract class AbsTypeAnimBuilder {
        protected long duration;
        protected float from, to;
        protected Builder builder;
        protected int repeatCount = 0;
        protected int repeatMode = RESTART;
        protected Interpolator i;
        protected long startOffset;
        protected boolean fillAfter;
        protected AnimAdapter animAdapter;
        private Animation animation;

        private AbsTypeAnimBuilder(Builder builder) {
            this.builder = builder;
        }

        public AbsTypeAnimBuilder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public AbsTypeAnimBuilder setFrom(float from) {
            this.from = from;
            return this;
        }

        public AbsTypeAnimBuilder setTo(float to) {
            this.to = to;
            return this;
        }

        public AbsTypeAnimBuilder setRepeatCount(int repeatCount) {
            this.repeatCount = repeatCount;
            return this;
        }

        public AbsTypeAnimBuilder setRepeatMode(int repeatMode) {
            this.repeatMode = repeatMode;
            return this;
        }

        public AbsTypeAnimBuilder setInterpolator(Interpolator i) {
            this.i = i;
            return this;
        }

        public AbsTypeAnimBuilder setStartOffset(long startOffset) {
            this.startOffset = startOffset;
            return this;
        }

        public AbsTypeAnimBuilder setAnimAdapter(AnimAdapter animAdapter) {
            this.animAdapter = animAdapter;
            return this;
        }

        public AbsTypeAnimBuilder setFillAfter(boolean fillAfter) {
            this.fillAfter = fillAfter;
            return this;
        }

        protected abstract Animation getAnim();


        public Builder build() {
            animation = getAnim();
            if (animation != null) {
                animation.setDuration(duration);
                animation.setRepeatCount(repeatCount);
                animation.setRepeatMode(repeatMode);
                animation.setFillAfter(fillAfter);
                animation.setStartOffset(startOffset);
                if (i != null) {
                    animation.setInterpolator(i);
                }
                if (animAdapter != null) {
                    animation.setAnimationListener(animAdapter);
                }
            }
            return builder.addAnim(this);
        }
    }

    public static class AlphaBuilder extends AbsTypeAnimBuilder {

        private AlphaBuilder(Builder builder) {
            super(builder);
        }

        @Override
        protected Animation getAnim() {
            return new AlphaAnimation(from, to);
        }
    }


    public static class TranslateYBuilder extends AbsTypeAnimBuilder {
        private TranslateYBuilder(Builder builder) {
            super(builder);
        }

        @Override
        protected Animation getAnim() {
            return new TranslateAnimation(0, 0, from, to);
        }
    }


    public static class TranslateXBuilder extends AbsTypeAnimBuilder {
        private TranslateXBuilder(Builder builder) {
            super(builder);
        }

        @Override
        protected Animation getAnim() {
            return new TranslateAnimation(from, to, 0, 0);
        }
    }


    public static class TranslateXYBuilder extends AbsTypeAnimBuilder {

        private float fromX, toX, fromY, toY;

        private TranslateXYBuilder(Builder builder) {
            super(builder);
        }

        public TranslateXYBuilder pointX(float fromX, float toX) {
            this.fromX = fromX;
            this.toX = toX;
            return this;
        }


        public TranslateXYBuilder pointY(float fromY, float toY) {
            this.fromY = fromY;
            this.toY = toY;
            return this;
        }


        @Override
        protected Animation getAnim() {
            return new TranslateAnimation(fromX, toX, fromY, toY);
        }
    }


    public static class ScanYBuilder extends AbsTypeAnimBuilder {


        private ScanYBuilder(Builder builder) {
            super(builder);
        }

        @Override
        protected Animation getAnim() {
            return new ScaleAnimation(0, 0, from, to);
        }
    }


    public static class ScanXBuilder extends AbsTypeAnimBuilder {


        private ScanXBuilder(Builder builder) {
            super(builder);
        }

        @Override
        protected Animation getAnim() {
            return new ScaleAnimation(from, to, 0, 0);
        }
    }


    public static class ScanXYBuilder extends AbsTypeAnimBuilder {

        private float fromX, toX, fromY, toY;

        private ScanXYBuilder(Builder builder) {
            super(builder);
        }

        public ScanXYBuilder setFromX(float fromX) {
            this.fromX = fromX;
            return this;
        }

        public ScanXYBuilder setToX(float toX) {
            this.toX = toX;
            return this;
        }

        public ScanXYBuilder setFromY(float fromY) {
            this.fromY = fromY;
            return this;
        }

        public ScanXYBuilder setToY(float toY) {
            this.toY = toY;
            return this;
        }

        @Override
        protected Animation getAnim() {
            return new ScaleAnimation(fromX, toX, fromY, toY);
        }
    }


    public static class RotateBuilder extends AbsTypeAnimBuilder {

        private float fromDegrees;
        private float toDegrees;
        private int pivotXType = ABSOLUTE;
        private float pivotXValue;
        private int pivotYType = ABSOLUTE;
        private float pivotYValue;

        private RotateBuilder(Builder builder) {
            super(builder);
        }

        public RotateBuilder setFromDegrees(float fromDegrees) {
            this.fromDegrees = fromDegrees;
            return this;
        }

        public RotateBuilder setToDegrees(float toDegrees) {
            this.toDegrees = toDegrees;
            return this;
        }

        public RotateBuilder setPivotXType(int pivotXType) {
            this.pivotXType = pivotXType;
            return this;
        }

        public RotateBuilder setPivotXValue(float pivotXValue) {
            this.pivotXValue = pivotXValue;
            return this;
        }

        public RotateBuilder setPivotYType(int pivotYType) {
            this.pivotYType = pivotYType;
            return this;
        }

        public RotateBuilder setPivotYValue(float pivotYValue) {
            this.pivotYValue = pivotYValue;
            return this;
        }

        @Override
        protected Animation getAnim() {
            return new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        }
    }


   public static class RotateZBuilder extends AbsTypeAnimBuilder {

        private float mFromDegrees;
        private float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private float mDepthZ;
        private boolean mReverse;
        private Byte rotateAxis;

        private RotateZBuilder(Builder builder) {
            super(builder);
        }

        public RotateZBuilder setRotateAxis(Byte rotateAxis) {
            this.rotateAxis = rotateAxis;
            return this;
        }

        public RotateZBuilder setFromDegrees(float mFromDegrees) {
            this.mFromDegrees = mFromDegrees;
            return this;
        }

        public RotateZBuilder setToDegrees(float mToDegrees) {
            this.mToDegrees = mToDegrees;
            return this;
        }

        public RotateZBuilder setCenterX(float mCenterX) {
            this.mCenterX = mCenterX;
            return this;
        }

        public RotateZBuilder setCenterY(float mCenterY) {
            this.mCenterY = mCenterY;
            return this;
        }

        public RotateZBuilder setDepthZ(float mDepthZ) {
            this.mDepthZ = mDepthZ;
            return this;
        }

        public RotateZBuilder setReverse(boolean mReverse) {
            this.mReverse = mReverse;
            return this;
        }

        @Override
        protected Animation getAnim() {
            return new Rotate3dAnimation(mFromDegrees,mToDegrees,mCenterX,mCenterY,mDepthZ,rotateAxis,mReverse);
        }
    }


    public static class AnimAdapter implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
