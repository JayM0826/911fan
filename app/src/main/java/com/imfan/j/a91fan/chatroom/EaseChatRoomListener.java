/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-15 上午1:16
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

import com.hyphenate.EMChatRoomChangeListener;

import java.util.List;

/**
 * Created by J on 2017/3/15 0015.
 */

public abstract class EaseChatRoomListener implements EMChatRoomChangeListener {

    @Override
    public void onChatRoomDestroyed(final String roomId, final String roomName) {

    }

    @Override
    public void onMemberJoined(final String roomId, final String participant) {

    }

    @Override
    public void onMemberExited(final String roomId, final String roomName, final String participant) {

    }

    @Override
    public void onRemovedFromChatRoom(final String roomId, final String roomName, final String participant) {

    }

    @Override
    public void onMuteListAdded(final String chatRoomId, final List<String> mutes, final long expireTime) {

    }

    @Override
    public void onMuteListRemoved(final String chatRoomId, final List<String> mutes) {

    }

    @Override
    public void onAdminAdded(final String chatRoomId, final String admin) {

    }

    @Override
    public void onAdminRemoved(final String chatRoomId, final String admin) {

    }

    @Override
    public void onOwnerChanged(final String chatRoomId, final String newOwner, final String oldOwner) {

    }
}

