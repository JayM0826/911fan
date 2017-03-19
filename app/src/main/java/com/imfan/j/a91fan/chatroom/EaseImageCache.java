/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-15 上午1:11
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

package com.imfan.j.a91fan.chatroom;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by J on 2017/3/15 0015.
 */

public class EaseImageCache {

    private EaseImageCache() {
        // use 1/8 of available heap size
        cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    private static EaseImageCache imageCache = null;

    public static synchronized EaseImageCache getInstance() {
        if (imageCache == null) {
            imageCache = new EaseImageCache();
        }
        return imageCache;

    }
    private LruCache<String, Bitmap> cache = null;

    /**
     * put bitmap to image cache
     * @param key
     * @param value
     * @return  the puts bitmap
     */
    public Bitmap put(String key, Bitmap value){
        return cache.put(key, value);
    }

    /**
     * return the bitmap
     * @param key
     * @return
     */
    public Bitmap get(String key){
        return cache.get(key);
    }
}
