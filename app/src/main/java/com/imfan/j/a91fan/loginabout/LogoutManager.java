package com.imfan.j.a91fan.loginabout;

import com.imfan.j.a91fan.util.Cache;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.activity.CustomActivityManager;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

/**
 * Created by jay on 17-2-8.
 */

public class LogoutManager {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();
        // ChatRoomHelper.logout();
       // Cache.clear();

        LoginSyncDataStatusObserver.getInstance().reset();

        /*在uikit库中我把public void destroy()中的removexxxx方法注释了，如果需要开启
        * 请手动开启，在DropManager中*/
        DropManager.getInstance().destroy();
        // 消除所有的Activity
        CustomActivityManager.getCustomActivityManager().popAllActivity();
        NIMClient.getService(AuthService.class).logout();
    }
}
