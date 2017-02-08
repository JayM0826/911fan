package com.imfan.j.a91fan.loginabout;

import com.imfan.j.a91fan.util.Cache;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.ui.drop.DropManager;

/**
 * Created by jay on 17-2-8.
 */

public class LogoutManager {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();
        // ChatRoomHelper.logout();
        Cache.clear();
        LoginSyncDataStatusObserver.getInstance().reset();
        DropManager.getInstance().destroy();
    }
}
