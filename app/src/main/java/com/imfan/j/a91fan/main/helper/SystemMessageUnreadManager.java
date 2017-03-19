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

package com.imfan.j.a91fan.main.helper;

/**
 * Created by jay on 17-2-18.
 */

public class SystemMessageUnreadManager {

    private static SystemMessageUnreadManager instance = new SystemMessageUnreadManager();
    private int sysMsgUnreadCount = 0;

    public static SystemMessageUnreadManager getInstance() {
        return instance;
    }

    public int getSysMsgUnreadCount() {
        return sysMsgUnreadCount;
    }

    public synchronized void setSysMsgUnreadCount(int unreadCount) {
        this.sysMsgUnreadCount = unreadCount;
    }
}