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


import android.support.annotation.NonNull;
import android.view.animation.Interpolator;

/**
 * This class offers a very small subset of {@code ValueAnimator}'s API.
 * <p>
 * You shouldn't not instantiate this directly. Instead use {@code ViewUtils.createAnimator()}.
 */
public class ValueAnimatorCompat {

    public interface AnimatorUpdateListener {
        /**
         * <p>Notifies the occurrence of another frame of the animation.</p>
         *
         * @param animator The animation which was repeated.
         */
        void onAnimationUpdate(ValueAnimatorCompat animator);
    }

    public interface Creator {
        @NonNull
        ValueAnimatorCompat createAnimator();
    }

    static abstract class Impl {
        interface AnimatorUpdateListenerProxy {
            void onAnimationUpdate();
        }

        abstract void start();
        abstract boolean isRunning();
        abstract void setInterpolator(Interpolator interpolator);
        abstract void addUpdateListener(AnimatorUpdateListenerProxy updateListener);
        abstract void setFloatValues(float from, float to);
        abstract float getAnimatedFloatValue();
        abstract void setDuration(long duration);
        abstract void cancel();
        abstract void end();
        abstract long getDuration();
    }

    private final Impl impl;

    public ValueAnimatorCompat(Impl impl) {
        this.impl = impl;
    }

    public void start() {
        impl.start();
    }

    public boolean isRunning() {
        return impl.isRunning();
    }

    public void setInterpolator(Interpolator interpolator) {
        impl.setInterpolator(interpolator);
    }

    public void addUpdateListener(final AnimatorUpdateListener updateListener) {
        if (updateListener != null) {
            impl.addUpdateListener(new Impl.AnimatorUpdateListenerProxy() {
                @Override
                public void onAnimationUpdate() {
                    updateListener.onAnimationUpdate(ValueAnimatorCompat.this);
                }
            });
        } else {
            impl.addUpdateListener(null);
        }
    }

    public void setFloatValues(float from, float to) {
        impl.setFloatValues(from, to);
    }

    public float getAnimatedFloatValue() {
        return impl.getAnimatedFloatValue();
    }

    public void setDuration(long duration) {
        impl.setDuration(duration);
    }

    public void cancel() {
        impl.cancel();
    }

    public void end() {
        impl.end();
    }

    public long getDuration() {
        return impl.getDuration();
    }
}