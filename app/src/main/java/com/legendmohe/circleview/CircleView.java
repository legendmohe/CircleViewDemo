package com.legendmohe.circleview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.legendmohe.circleview.model.Dot;
import com.legendmohe.circleview.model.Oval;

import java.util.Random;

/**
 * Created by legendmohe on 16/5/22.
 */
public class CircleView extends View {
    private static final String TAG = "CircleView";

    private Paint mOvalPaint;
    private Paint mDotPaint;
    private @ColorInt
    int mPaintColor;

    private ValueAnimator mOvalRotateAnimator;
    private ValueAnimator mOvalSizeAnimator;
    private ValueAnimator mDotMoveAnimator;
    private Random mRandom = new Random();
    private AnimatorSet mAnimatorSet;

    private Oval[] mOvals;
    private Dot[] mDots;
    private int mCenterX, mCenterY;
    private float mRotateAngle = 0.0f;

    private int mNumOfDot = 30;
    private int mNumOfOval = 30;
    private int mOvalRadius = 400;
    private int mOvalRegionWidth = mOvalRadius*2 - 15;
    private int mOvalRegionHeight = mOvalRadius*2;
    private int mOvalRotateDuration = 3000;
    private float mOvalStrokeWidth = 3.0f;
    private int mOvalSizeDuration = 2000;
    private int mOvalSizeExpendWidth = 50;
    private int mDotMovingDuration = 1000;
    private int mOvalAlphaDelta = 20;
    private int mOvalMinAlpha = 20;
    private int mDotMaxVelocity = 4;
    private int mDotMinVelocity = 2;
    private int mDotMaxRadius = 8;
    private int mDotMinRadius = 2;
    private int mDotMinAlpha = 100;
    private int mDotMaxAplha = 255;
    private int mDotBornMargin = 30;

    public CircleView(Context context) {
        super(context);
        setup();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup();
    }

    private void setup() {
        mPaintColor = Color.BLACK;

        mDotPaint = new Paint();
        mDotPaint.setColor(mPaintColor);
        mDotPaint.setAntiAlias(true);
        mDotPaint.setStyle(Paint.Style.FILL);

        mOvalPaint = new Paint();
        mOvalPaint.setColor(mPaintColor);
        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setStrokeWidth(mOvalStrokeWidth);
        mOvalPaint.setStyle(Paint.Style.STROKE);

        mOvalRotateAnimator = new ValueAnimator();
        mOvalRotateAnimator.setInterpolator(new LinearInterpolator());
        mOvalRotateAnimator.setDuration(mOvalRotateDuration);
        mOvalRotateAnimator.setFloatValues(0, 360);
        mOvalRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mOvalRotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        mOvalRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);

        mOvalSizeAnimator = new ValueAnimator();
        mOvalSizeAnimator.setInterpolator(new LinearInterpolator());
        mOvalSizeAnimator.setDuration(mOvalSizeDuration);
        mOvalSizeAnimator.setIntValues(0, mOvalSizeExpendWidth);
        mOvalSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                for (int i = 0; i < mNumOfOval; i++) {
                    Oval oval = mOvals[i];
                    oval.mRectF.set(
                            oval.mOriginalRectF.left + value,
                            oval.mOriginalRectF.top,
                            oval.mOriginalRectF.right - value,
                            oval.mOriginalRectF.bottom);
                }
                invalidate();
            }
        });
        mOvalSizeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mOvalSizeAnimator.setRepeatCount(ValueAnimator.INFINITE);

        mDotMoveAnimator = new ValueAnimator();
        mDotMoveAnimator.setInterpolator(new LinearInterpolator());
        mDotMoveAnimator.setDuration(mDotMovingDuration);
        mDotMoveAnimator.setIntValues(0, mDotMovingDuration);
        mDotMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (int i = 0; i < mNumOfDot; i++) {
                    Dot dot = mDots[i];
                    dot.distance -= dot.velocity;
                    dot.alpha = (int) (dot.baseAlpha*((double)(dot.distance - mOvalRadius)/(dot.baseDistance - mOvalRadius)));
                    if (dot.distance < mOvalRadius) {
                        randomDot(dot);
                    }
                }
                invalidate();
            }
        });
        mDotMoveAnimator.setRepeatMode(ValueAnimator.RESTART);
        mDotMoveAnimator.setRepeatCount(ValueAnimator.INFINITE);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(mOvalRotateAnimator, mOvalSizeAnimator, mDotMoveAnimator);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = w/2;
        mCenterY = h/2;
        setupDrawables(w, h);
        if (mAnimatorSet.isStarted()) {
            mAnimatorSet.cancel();
        }
        mAnimatorSet.start();
    }

    private void setupDrawables(int w, int h) {
        mOvals = new Oval[mNumOfOval];
        int alpha = 255;
        for (int i = 0; i < mNumOfOval; i++) {
            RectF rectF = new RectF(
                    mCenterX - mOvalRegionWidth /2,
                    mCenterY - mOvalRegionHeight /2,
                    mCenterX + mOvalRegionWidth /2,
                    mCenterY + mOvalRegionHeight /2);
            alpha = alpha - mOvalAlphaDelta;
            if (alpha < mOvalMinAlpha)
                alpha = mOvalMinAlpha;
            mOvals[i] = new Oval(rectF, 0, alpha);
        }

        mDots = new Dot[mNumOfDot];
        for (int i = 0; i < mNumOfDot; i++) {
            mDots[i] = new Dot(mCenterX, mCenterY);
            randomDot(mDots[i]);
        }
    }

    private void randomDot(Dot dot) {
        int w = getWidth();
        int h = getHeight();

        int minDistance = mOvalRadius + mDotBornMargin;
        int alpha = mRandom.nextInt(mDotMaxAplha - mDotMinAlpha) + mDotMinAlpha;
        double angle = Math.toRadians(mRandom.nextInt(360));
        int distance = mRandom.nextInt((int) Math.sqrt(w/2.0*w/2.0 + h/2.0*h/2.0) - minDistance) + minDistance;
        int velocity = mRandom.nextInt(mDotMaxVelocity - mDotMinVelocity) + mDotMinVelocity;
        int radius = mRandom.nextInt(mDotMaxRadius - mDotMinRadius) + mDotMinRadius;

        dot.setAlpha(alpha);
        dot.setBaseAlpha(alpha);
        dot.setAngle(angle);
        dot.setDistance(distance);
        dot.setBaseDistance(distance);
        dot.setVelocity(velocity);
        dot.setRadius(radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mNumOfDot; i++) {
            Dot dot = mDots[i];
            mDotPaint.setAlpha(dot.alpha);
            canvas.drawCircle(dot.getCenterX(), dot.getCenterY(), dot.radius, mDotPaint);
        }

        canvas.rotate(mRotateAngle, mCenterX, mCenterY);
        for (int i = mNumOfOval - 1; i >= 0; i--) {
            Oval oval = mOvals[i];
            mOvalPaint.setAlpha(oval.mAlpha);
            canvas.rotate(360/mNumOfOval, mCenterX, mCenterY);
            canvas.drawOval(oval.mRectF, mOvalPaint);
        }
    }

    public void setPaintColor(@ColorInt int color) {
        mPaintColor = color;
        mDotPaint.setColor(mPaintColor);
        mOvalPaint.setColor(mPaintColor);
    }

    public int getPaintColor() {
        return mPaintColor;
    }

    public void setNumOfDot(final int numOfDot) {
        postOnAnimation(new Runnable() {
            @Override
            public void run() {
                Dot[] copyDots = new Dot[numOfDot];
                System.arraycopy(mDots, 0, copyDots, 0, Math.min(mDots.length, numOfDot));

                int numberDelta = numOfDot - mNumOfDot;
                if (numberDelta > 0) {
                    for (int i = 0; i < numberDelta; i++) {
                        copyDots[i + mNumOfDot] = new Dot(mCenterX, mCenterY);
                        randomDot(copyDots[i + mNumOfDot]);
                    }
                }
                mNumOfDot = numOfDot;
                mDots = copyDots;
            }
        });
        postInvalidateOnAnimation();
    }

    public void setDotVelocity(final int maxVelocity, final int minVelocity) {
        postOnAnimation(new Runnable() {

            @Override
            public void run() {
                mDotMaxVelocity = maxVelocity;
                mDotMinVelocity = minVelocity;

                if (mDotMinVelocity <= 0) {
                    mDotMinVelocity = 1;
                }
                mDotMaxVelocity = Math.max(mDotMaxVelocity, mDotMinVelocity);
            }
        });
        postInvalidateOnAnimation();
    }

    public void setOvalRotateDuration(final int ovalRotateDuration) {
        postOnAnimation(new Runnable() {

            @Override
            public void run() {
                mOvalRotateDuration = ovalRotateDuration;
                mOvalRotateDuration = Math.max(mOvalRotateDuration, 200);
                mOvalRotateAnimator.setDuration(mOvalRotateDuration);
            }
        });
        postInvalidateOnAnimation();
    }

    public void setOvalSizeExpendWidth(final int ovalSizeExpendWidth) {
        postOnAnimation(new Runnable() {

            @Override
            public void run() {
                mOvalSizeExpendWidth = ovalSizeExpendWidth;
                mOvalSizeExpendWidth = Math.max(mOvalSizeExpendWidth, 0);
                mOvalSizeAnimator.setIntValues(0, mOvalSizeExpendWidth);
            }
        });
        postInvalidateOnAnimation();
    }
}
