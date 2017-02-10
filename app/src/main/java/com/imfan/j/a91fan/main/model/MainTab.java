package com.imfan.j.a91fan.main.model;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.main.fragment.BlogWallFragment;
import com.imfan.j.a91fan.main.fragment.ChatRoomFragment;
import com.imfan.j.a91fan.main.fragment.MainFragment;
import com.imfan.j.a91fan.main.fragment.MessageFragment;
import com.imfan.j.a91fan.main.fragment.MyProfileFragment;
import com.imfan.j.a91fan.main.reminder.ReminderId;
import com.netease.nim.uikit.common.fragment.TFragment;

/**
 * Created by jay on 17-2-8.
 */

public enum MainTab {
    CHAT_ROOM(0, ReminderId.SESSION, ChatRoomFragment.class,  R.layout.chat_room_net_status),
    MESSAGE(1, ReminderId.SESSION, MessageFragment.class,  R.layout.chat_room_net_status),
    BLOG_WALL(2, ReminderId.SESSION, BlogWallFragment.class,  R.layout.chat_room_net_status),
    PROFILE(3, ReminderId.SESSION, MyProfileFragment.class,  R.layout.fragment_profile);

    public final int tabIndex;

    public final int reminderId;

    public final Class<? extends MainFragment> clazz;


    public final int fragmentId;

    public final int layoutId;

    MainTab(int index, int reminderId, Class<? extends MainFragment> clazz,  int layoutId) {
        this.tabIndex = index;
        this.reminderId = reminderId;
        this.clazz = clazz;
        this.fragmentId = index;
        this.layoutId = layoutId;
    }

    public static final MainTab fromReminderId(int reminderId) {
        for (MainTab value : MainTab.values()) {
            if (value.reminderId == reminderId) {
                return value;
            }
        }

        return null;
    }

    public static final MainTab fromTabIndex(int tabIndex) {
        for (MainTab value : MainTab.values()) {
            if (value.tabIndex == tabIndex) {
                return value;
            }
        }

        return null;
    }
}
