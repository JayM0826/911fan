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

package com.imfan.j.a91fan.main.fragment;

import android.os.Bundle;
import android.util.Log;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.chatroom.ChatRoomFragment;
import com.imfan.j.a91fan.main.fragment.MainFragment;
import com.imfan.j.a91fan.main.model.MainTab;
import com.netease.nim.uikit.common.util.log.LogUtil;

/**
 * Created by jay on 17-2-26.
 */
public class ChatRoomListFragment extends MainFragment {
    private ChatRoomFragment fragment;
    private static final String API_NAME_CHAT_ROOM_LIST = "homeList";
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    public ChatRoomListFragment() {
        setContainerId(MainTab.CHAT_ROOM.fragmentId);
    }

    @Override
    protected void onInit() {
        fragment = (ChatRoomFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.chat_room_fragment);
    }

    @Override
    public void onCurrent() {
        super.onCurrent(); // 这行父类的方法，就执行了onInit了
    }
}
