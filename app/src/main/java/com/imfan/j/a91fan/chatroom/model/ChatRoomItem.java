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