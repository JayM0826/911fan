package com.imfan.j.a91fan.netease;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.imfan.j.a91fan.util.Cache;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.activity.CustomActivityManager;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.util.log.LogUtil;
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
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.i("退出成功", "退出成功");
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                LogUtil.i("退出失败", "退出失败" + code + "  "  + message);

            }
        });
    }
}
