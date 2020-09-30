package com.tofu.mvp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tofu.mvp.R;
import com.tofu.mvp.util.CollectionUtils;
import com.tofu.mvp.util.DPUtil;

import java.util.List;

public class ImageOverlyingView extends RelativeLayout {

    int imageWidth;
    int imageHeight;
    int padding;

    public ImageOverlyingView(Context context) {
        super(context);
        setViewSettings(null);
    }

    public ImageOverlyingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setViewSettings(attrs);
    }

    public ImageOverlyingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setViewSettings(attrs);
    }


    private void setViewSettings(AttributeSet attrs) {
        setGravity(Gravity.CENTER_VERTICAL);
        imageWidth = DPUtil.dip2px(getContext(), 20);
        imageHeight = DPUtil.dip2px(getContext(), 20);
        padding = DPUtil.dip2px(getContext(), 15);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImageOverlyingView);
            imageWidth = typedArray.getDimensionPixelSize(R.styleable.ImageOverlyingView_imageSize,imageWidth);
            padding = typedArray.getDimensionPixelSize(R.styleable.ImageOverlyingView_padding,padding);
            imageHeight = imageWidth;
        }
    }


    /**
     * 设置图片
     *
     * @param images
     */
    public void load(List<String> images,LoadImgAdapter adapter) {
        if (CollectionUtils.isNotEmpty(images)) {
            for (int i = 0; i < images.size(); i++) {
                ImageView imageView = generateImageView(i);
                if(adapter != null){
                    adapter.loadImg(images.get(i),imageView,i);
                }
                addView(imageView);
            }
        }
    }


    private ImageView generateImageView(int position) {
        ImageView imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(imageWidth, imageHeight);
        params.leftMargin = position * padding;
        imageView.setLayoutParams(params);
        return imageView;
    }

    public interface LoadImgAdapter {
        void loadImg(String url, ImageView imageView, int position);
    }
}
