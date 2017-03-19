/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-15 上午12:39
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by J on 2017/3/15 0015.
 */

public class EaseEmojiconMenuBase extends LinearLayout {
    protected EaseEmojiconMenuListener listener;

    public EaseEmojiconMenuBase(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    public EaseEmojiconMenuBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public EaseEmojiconMenuBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * set emojicon menu listener
     * @param listener
     */
    public void setEmojiconMenuListener(EaseEmojiconMenuListener listener){
        this.listener = listener;
    }

    public interface EaseEmojiconMenuListener{
        /**
         * on emojicon clicked
         * @param emojicon
         */
        void onExpressionClicked(EaseEmojicon emojicon);
        /**
         * on delete image clicked
         */
        void onDeleteImageClicked();
    }
}

