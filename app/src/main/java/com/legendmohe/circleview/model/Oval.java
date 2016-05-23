package com.legendmohe.circleview.model;

import android.graphics.RectF;

/**
 * Created by legendmohe on 16/5/22.
 */
public class Oval {
    public RectF mRectF;
    public RectF mOriginalRectF;
    public float mRotateAngle;
    public int mAlpha;

    public Oval(RectF rectF, float rotateAngle, int alpha) {
        mRectF = rectF;
        mOriginalRectF = new RectF(rectF);
        mRotateAngle = rotateAngle;
        mAlpha = alpha;
    }
}
