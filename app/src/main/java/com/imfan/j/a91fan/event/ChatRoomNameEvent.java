package com.imfan.j.a91fan.event;

/**
 * Created by jay on 17-2-27.
 */

public class ChatRoomNameEvent {

    private String mRoomName;

    public ChatRoomNameEvent(String name){
        this.mRoomName = name;
    }

    public String getmRoomName(){
        if (this.mRoomName == null){
            return null;
        }
        return this.mRoomName;
    }

}
