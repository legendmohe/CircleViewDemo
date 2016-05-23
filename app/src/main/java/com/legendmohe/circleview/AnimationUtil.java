package com.legendmohe.circleview;

import android.animation.ValueAnimator;
import android.graphics.Color;

/**
 * Created by legendmohe on 16/5/23.
 */
public class AnimationUtil {
    public static ValueAnimator animateColorChange(
            int fromColor,
            int toColor,
            int duration,
            final AnimateColorChangeListener listener)
    {
        final float[] from = new float[3];
        final float[] to = new float[3];

        Color.colorToHSV(fromColor, from);   // from color
        Color.colorToHSV(toColor, to);     // to color

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
        anim.setDuration(duration);

        final float[] hsv  = new float[3];                  // transition color
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                if (listener != null) {
                    // Transition along each axis of HSV (hue, saturation, value)
                    hsv[0] = from[0] + (to[0] - from[0])*animation.getAnimatedFraction();
                    hsv[1] = from[1] + (to[1] - from[1])*animation.getAnimatedFraction();
                    hsv[2] = from[2] + (to[2] - from[2])*animation.getAnimatedFraction();
                    listener.onColorChange(Color.HSVToColor(hsv));
                }
            }
        });

        return anim;
    }

    public interface AnimateColorChangeListener {
        void onColorChange(int color);
    }
}
