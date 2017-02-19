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