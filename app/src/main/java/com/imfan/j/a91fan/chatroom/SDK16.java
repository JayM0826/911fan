/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-15 上午12:58
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

import android.annotation.TargetApi;
import android.view.View;

/**
 * Created by J on 2017/3/15 0015.
 */

@TargetApi(16)
class SDK16 {

    public static void postOnAnimation(View view, Runnable r) {
        view.postOnAnimation(r);
    }

}

