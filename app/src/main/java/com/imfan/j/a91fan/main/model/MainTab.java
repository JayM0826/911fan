package com.imfan.j.a91fan.main.model;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.main.fragment.ChatRoomListFragment;
import com.imfan.j.a91fan.main.fragment.BlogWallFragment;
import com.imfan.j.a91fan.main.fragment.MainFragment;
import com.imfan.j.a91fan.main.fragment.MessageFragment;
import com.imfan.j.a91fan.main.fragment.MyProfileFragment;

/**
 * Created by jay on 17-2-8.
 */

public enum MainTab {
    CHAT_ROOM(0,  ChatRoomListFragment.class,  R.layout.fragment_list_chatroom),
    MESSAGE(1,  MessageFragment.class,  R.layout.fragment_message),
    BLOG_WALL(2,  BlogWallFragment.class,  R.layout.fragment_profile),
    PROFILE(3,  MyProfileFragment.class,  R.layout.fragment_profile);

    public final int tabIndex;


    public final Class<? extends MainFragment> clazz;


    // 用来确定是第几个fragment,用来进行切换吧
    public final int fragmentId;

    public final int layoutId;

    MainTab(int index, Class<? extends MainFragment> clazz,  int layoutId) {
        this.tabIndex = index;
        this.clazz = clazz;
        this.fragmentId = index;
        this.layoutId = layoutId;
    }



}
