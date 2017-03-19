/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-15 上午12:23
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

import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;

/**
 * Created by J on 2017/3/15 0015.
 */

public interface EaseCustomChatRowProvider {
    /**
     * 获取多少种类型的自定义chatrow<br/>
     * 注意，每一种chatrow至少有两种type：发送type和接收type
     * @return
     */
    int getCustomChatRowTypeCount();

    /**
     * 获取chatrow type，必须大于0, 从1开始有序排列
     * @return
     */
    int getCustomChatRowType(EMMessage message);

    /**
     * 根据给定message返回chat row
     * @return
     */
    EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter);

}