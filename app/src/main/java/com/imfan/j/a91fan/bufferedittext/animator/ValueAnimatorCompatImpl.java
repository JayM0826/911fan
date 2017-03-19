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
package com.imfan.j.a91fan.bufferedittext.animator;

import android.animation.ValueAnimator;
import android.view.animation.Interpolator;

public class ValueAnimatorCompatImpl extends ValueAnimatorCompat.Impl {
    private final ValueAnimator mValueAnimator;

    public ValueAnimatorCompatImpl() {
        mValueAnimator = new ValueAnimator();
    }

    @Override
    public void start() {
        mValueAnimator.start();
    }

    @Override
    public boolean isRunning() {
        return mValueAnimator.isRunning();
    }

    @Override
    public void setInterpolator(Interpolator interpolator) {
        mValueAnimator.setInterpolator(interpolator);
    }

    @Override
    public void addUpdateListener(final AnimatorUpdateListenerProxy updateListener) {
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateListener.onAnimationUpdate();
            }
        });
    }

    @Override
    public void setFloatValues(float from, float to) {
        mValueAnimator.setFloatValues(from, to);
    }

    @Override
    public float getAnimatedFloatValue() {
        return (float) mValueAnimator.getAnimatedValue();
    }

    @Override
    public void setDuration(long duration) {
        mValueAnimator.setDuration(duration);
    }

    @Override
    public void cancel() {
        mValueAnimator.cancel();
    }

    @Override
    public void end() {
        mValueAnimator.end();
    }

    @Override
    public long getDuration() {
        return mValueAnimator.getDuration();
    }
}