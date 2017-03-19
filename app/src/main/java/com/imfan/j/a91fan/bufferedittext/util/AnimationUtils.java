/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-13 上午11:12
 *  *
 *  * Project name: 911fan
 *  *
 *  * Contact me:
 *  * WeChat:  worromoT_
 *  * Email: 2212131349@qq.com
 *  *
 *  * Warning:If my code is same as yours, then i copy you!
 *
 */
package com.imfan.j.a91fan.bufferedittext.util;

import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class AnimationUtils {

    public static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    public static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR =
            new FastOutSlowInInterpolator();
    public static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR =
            new FastOutLinearInInterpolator();
    public static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR =
            new LinearOutSlowInInterpolator();

    /**
     * Linear interpolation between {@code startValue} and {@code endValue} by {@code fraction}.
     */
    public static float lerp(float startValue, float endValue, float fraction) {
        return startValue + (fraction * (endValue - startValue));
    }

}