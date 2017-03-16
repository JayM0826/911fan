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

