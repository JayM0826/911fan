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

import android.support.annotation.NonNull;

import com.imfan.j.a91fan.bufferedittext.animator.ValueAnimatorCompat;
import com.imfan.j.a91fan.bufferedittext.animator.ValueAnimatorCompatImpl;


public class ViewUtils {

    private static final ValueAnimatorCompat.Creator DEFAULT_ANIMATOR_CREATOR
            = new ValueAnimatorCompat.Creator() {
        @NonNull
        @Override
        public ValueAnimatorCompat createAnimator() {
            return new ValueAnimatorCompat(new ValueAnimatorCompatImpl());
        }
    };

    public static ValueAnimatorCompat createAnimator() {
        return DEFAULT_ANIMATOR_CREATOR.createAnimator();
    }

}