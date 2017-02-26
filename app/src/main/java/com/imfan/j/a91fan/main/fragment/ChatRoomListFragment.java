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
