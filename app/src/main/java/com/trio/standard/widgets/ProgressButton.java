package com.trio.standard.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.trio.standard.R;

/**
 * Created by lixia on 2018/10/11.
 */

public class ProgressButton extends ProgressBar {

    private int mProgress = 0;
    private String mText = "";
    private String mPercentText = "0%";
    private Rect mPercentRect;
    private Rect mPauseRect;
    private Paint mPaint;
    private PorterDuffXfermode mPorterDuffXfermode;
    private float mWidth;
    private STATE mState = STATE.NORMAL;

    public enum STATE {
        PERCENT,
        NORMAL
    }

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPauseRect = new Rect();
        mPercentRect = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mPaint.setColor(Color.parseColor("#00B38A"));
        mPaint.setTextSize(40);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setXfermode(null);
        mPaint.setTextAlign(Paint.Align.LEFT);
    }

    public synchronized void setState(STATE state, String str) {
        mState = state;
        if (!TextUtils.isEmpty(str))
            mText = str;
        invalidate();
    }

    public void setProgress(int progress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        mProgress = progress;
        mPercentText = progress + "%";
        super.setProgress(progress);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth() * mProgress / 100;//进度作一下处理，解决不同分辨率的适配问题
        mPaint.getTextBounds(mPercentText, 0, mPercentText.length(), mPercentRect);//为了获取文字的宽高以及坐标位置，get之后，rect
        // .centerX才有值
        mPaint.getTextBounds(mText, 0, mText.length(), mPauseRect);
        int textX = (getWidth() / 2) - mPauseRect.centerX();//获取“暂停”文字的中心横坐标
        int textY = (getHeight() / 2) - mPauseRect.centerY();
        int percentX = (getWidth() / 2) - mPercentRect.centerX();//获取百分比文字的中心横坐标
        int percentY = (getHeight() / 2) - mPercentRect.centerY();
        Bitmap srcBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas srcCanvas = new Canvas(srcBitmap);
        switch (mState) {
            case NORMAL://默认初始化状态
                drawTextUI(canvas, textX, textY, mText, srcBitmap, srcCanvas);
                break;
            case PERCENT:
                drawTextUI(canvas, percentX, percentY, mPercentText, srcBitmap, srcCanvas);
                break;
            default:
                drawTextUI(canvas, textX, textY, mText, srcBitmap, srcCanvas);
                break;
        }
    }

    private void drawTextUI(Canvas canvas, int x, int y, String textContent, Bitmap srcBitmap, Canvas srcCanvas) {
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawText(textContent, x, y, mPaint);
        srcCanvas.drawText(textContent, x, y, mPaint);
        // 设置混合模式
        mPaint.setXfermode(mPorterDuffXfermode);
        mPaint.setColor(Color.WHITE);
        RectF rectF = new RectF(0, 0, mWidth, getHeight());//mWidth是不断变化的
        // 绘制源图形
        srcCanvas.drawRect(rectF, mPaint);
        // 绘制目标图
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        // 清除混合模式
        mPaint.setXfermode(null);
        // 恢复画笔颜色
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
    }
}
