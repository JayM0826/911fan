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

/**
 * A class that contains utility methods related to numeric operations.
 *
 */
public final class MathUtils {

    private MathUtils() { }

    public static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

}