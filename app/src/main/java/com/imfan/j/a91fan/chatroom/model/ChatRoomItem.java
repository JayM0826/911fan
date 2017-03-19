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

package com.imfan.j.a91fan.chatroom.model;

import android.support.annotation.NonNull;

/**
 * Created by jay on 17-2-25.
 */
public class ChatRoomItem {
    @NonNull public int roomID;
    @NonNull public String roomName;
    @NonNull public String owner;
    @NonNull public int roomMeberNumber;
    public ChatRoomItem(@NonNull final int roomID, @NonNull final String roomName, @NonNull String owner, @NonNull int roomMeberNumber){
        this.roomID = roomID;
        this.roomName = roomName;
        this.owner = owner;
        this.roomMeberNumber = roomMeberNumber;
    }
}