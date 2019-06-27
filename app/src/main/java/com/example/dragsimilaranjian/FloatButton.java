package com.example.dragsimilaranjian;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Size;
import android.view.MotionEvent;

import me.jessyan.autosize.utils.ScreenUtils;

/**
 * author: eagle
 * created on: 2019-06-27 14:20
 * description:
 */
public class FloatButton extends AppCompatImageView {

    /**
     * View的宽高
     */
    private int width;
    private int height;

    /**
     * 触摸点相对于View的坐标
     */
    private float touchX;
    private float touchY;

    /**
     * x,y坐标的纠正值
     * 考虑到一些异性屏和非标准的显示区域
     */
    int xCorrection = 0;
    int yCorrection = 300;

    public void setxCorrection(int xCorrection) {
        this.xCorrection = xCorrection;
    }

    public void setyCorrection(int yCorrection) {
        this.yCorrection = yCorrection;
    }


    private int screenWidth = 1440;
    private int screenHeight = 2560;

    private final static int FADE_OUT = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADE_OUT:
                    ObjectAnimator.ofFloat(FloatButton.this, "alpha",
                            1.0f, ASSIST_TOUCH_VIEW_ALPHA_RATE)
                            .setDuration(ANIMATION_DURATION)
                            .start();
                    break;
                default:
                    break;
            }
        }
    };

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public FloatButton(Context context) {
        super(context);
    }

    public FloatButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
    }

    private float mTouchStartX;
    private float mTouchStartY;
    private static final float TOLERANCE_RANGE = 18.0f;
    private static final float ASSIST_TOUCH_VIEW_ALPHA_RATE = 0.3f;
    private static final int ANIMATION_DURATION = 110;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(1.0f);
                clearAnimation();
                mHandler.removeMessages(FADE_OUT);
                touchX = event.getX();
                touchY = event.getY();
                mTouchStartX = event.getRawX();
                mTouchStartY = event.getRawY();
                setBackgroundResource(R.mipmap.start_icon);
                return true;
            case MotionEvent.ACTION_MOVE:
                setAlpha(1.0f);
                float nowY = event.getRawY() - touchY - yCorrection;
                float nowX = event.getRawX() - touchX - xCorrection;
                nowX = nowX < 0 ? 0 : (nowX + width > screenWidth) ? (screenWidth - width) : nowX;
                nowY = nowY < 0 ? 0 : (nowY + height > screenHeight) ? (screenHeight - height) : nowY;
                this.setY(nowY);
                this.setX(nowX);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                //这里做动画贴边效果
                float centerX = getX() + width / 2f;
                int halfOfScreenWidth = screenWidth / 2;
                if (centerX > halfOfScreenWidth) {
                    ObjectAnimator.ofFloat(this, "translationX",
                            getX(), screenWidth - width)
                            .setDuration(250)
                            .start();
                } else {
                    ObjectAnimator.ofFloat(this, "translationX",
                            getX(), 0)
                            .setDuration(250)
                            .start();
                }
                mHandler.sendEmptyMessageDelayed(FADE_OUT, 1000);
                touchX = 0;
                touchY = 0;
                setBackgroundResource(R.mipmap.start_icon);
                float mTouchEndX = event.getRawX();
                float mTouchEndY = event.getRawY();
                if (Math.abs(mTouchEndX - mTouchStartX) < TOLERANCE_RANGE
                        && Math.abs(mTouchEndY - mTouchStartY) < TOLERANCE_RANGE) {
                    performClick();
                    return true;
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
