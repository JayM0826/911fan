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
