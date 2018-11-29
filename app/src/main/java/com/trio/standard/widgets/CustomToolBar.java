package com.trio.standard.widgets;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trio.standard.R;

/**
 * Created by lixia on 2018/9/13.
 */

public class CustomToolBar extends Toolbar {

    private ImageButton mIvBack;
    private TextView mTvTitle;
    private RelativeLayout mLayout;

    private View mView;
    private LayoutInflater mInflater;

    public CustomToolBar(Context context) {
        this(context, null);
    }

    public CustomToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        //设置toolbar的边距
        setContentInsetsRelative(0, 0);
        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CustomToolBar, defStyleAttr, 0);

            if (a.hasValue(R.styleable.CustomToolBar_title_text)) {
                mTvTitle.setText(a.getString(R.styleable.CustomToolBar_title_text));
            }
            if (a.hasValue(R.styleable.CustomToolBar_title_color)) {
                mTvTitle.setTextColor(a.getColor(R.styleable.CustomToolBar_title_color, Color.WHITE));
            }

            final float title_size = a.getDimension(R.styleable.CustomToolBar_title_size,
                    //将16sp转换为对应的px
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
            if (mTvTitle != null)
                mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, title_size);

            if (a.hasValue(R.styleable.CustomToolBar_background_color)) {
                mLayout.setBackgroundColor(a.getColor(R.styleable.CustomToolBar_background_color, Color.WHITE));
            }
            if (a.hasValue(R.styleable.CustomToolBar_hide_back_icon)) {
                boolean hide = a.getBoolean(R.styleable.CustomToolBar_hide_back_icon, false);
                if (hide)
                    hideBackIcon();
                else
                    showBackIcon();
            }
            a.recycle();
        }
    }

    public void initView() {
        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.view_custom_toolbar, null);
            mLayout = (RelativeLayout) mView.findViewById(R.id.layout);
            mTvTitle = (TextView) mView.findViewById(R.id.tv_title);
            mIvBack = (ImageButton) mView.findViewById(R.id.iv_back);
            //然后使用LayoutParams把控件添加到子view中
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }
    }

    //隐藏标题
    public void hideTitleView() {
        if (mTvTitle != null)
            mTvTitle.setVisibility(GONE);
    }

    public void hideBackIcon() {
        if (mIvBack != null)
            mIvBack.setVisibility(GONE);
    }

    public void showBackIcon() {
        if (mIvBack != null)
            mIvBack.setVisibility(VISIBLE);
    }

}
