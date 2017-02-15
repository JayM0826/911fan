package com.netease.nim.uikit.contact.core.provider;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.UIKitLogTag;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.query.TextQuery;
import com.netease.nim.uikit.contact.core.util.ContactHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jay on 17-2-13.
 */

public class UserProvider {

    static AbsContactItem items;
    private static UserInfoProvider.UserInfo user;
    private static Map<String, List<RequestCallback<NimUserInfo>>> requestUserInfoMap = new ConcurrentHashMap<>(); // 重复请求处理

    /*核心语句，对这个类型的数据进行query字段的搜索,然后将hit到的数据进行返回*/
    public static final void provide(TextQuery query) {
        user = null;
        items = null;
        if (query != null) {
            getUserInfoFromRemote(query.text, new RequestCallback<NimUserInfo>() {
                @Override
                public void onSuccess(NimUserInfo userResult) { // 成功获取用户，说明和后台是交互了，但是交互不说明一定有用户
                    if (userResult == null) {
                        LogUtil.i(getClass().getSimpleName(), "服务器没有这个用户！");
                        user = null;
                    }else{
                        LogUtil.i(getClass().getSimpleName(), "在服务器中找到了这个用户");

                        user = userResult;
                        items = new ContactItem(ContactHelper.makeContactFromUserInfo(user, 1), ItemTypes.USER);
                    }
                }

                @Override
                public void onFailed(int code) {
                    if (code == 408) {
                        LogUtil.e(getClass().getSimpleName(), "查找服务器用户时网络连接失败，请检查你的网络设置");
                    } else {
                        LogUtil.e(getClass().getSimpleName(), "不明原因查找服务器用户时出现错误！");
                        user = null;
                    }
                }

                @Override
                public void onException(Throwable exception) {
                    LogUtil.e(getClass().getSimpleName(), "查找服务器用户时出现了异常");
                    user = null;
                }
            });
        }else {
            LogUtil.i("UserInfoProvider.UserInfo query:", "搜索用户关键词为null！");
        }

    }

    public static void getUserInfoFromRemote(final String account, final RequestCallback<NimUserInfo> callback) {
        if (TextUtils.isEmpty(account)) {
            return;
        }

        if (requestUserInfoMap.containsKey(account)) {
            if (callback != null) {
                requestUserInfoMap.get(account).add(callback);
            }
            return; // 已经在请求中，不要重复请求
        } else {
            List<RequestCallback<NimUserInfo>> cbs = new ArrayList<>();
            if (callback != null) {
                cbs.add(callback);
            }
            requestUserInfoMap.put(account, cbs);
        }

        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);

        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {

            @Override
            public void onResult(int code, List<NimUserInfo> users, Throwable exception) {
                if (exception != null) {
                    callback.onException(exception);
                    return;
                }

                NimUserInfo user = null;
                boolean hasCallback = requestUserInfoMap.get(account).size() > 0;
                if (code == ResponseCode.RES_SUCCESS && users != null && !users.isEmpty()) {
                    user = users.get(0);
                    // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                }

                // 处理回调
                if (hasCallback) {
                    List<RequestCallback<NimUserInfo>> cbs = requestUserInfoMap.get(account);
                    for (RequestCallback<NimUserInfo> cb : cbs) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            cb.onSuccess(user);
                        } else {
                            cb.onFailed(code);
                        }
                    }
                }

                requestUserInfoMap.remove(account);
            }
        });
    }
}





