package com.netease.nim.uikit.contact.core.provider;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.UIKitLogTag;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.query.TextQuery;
import com.netease.nim.uikit.contact.core.util.ContactHelper;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class UserDataProvider {
    public static final List<AbsContactItem> provide(TextQuery query) {
        List<UserInfoProvider.UserInfo> sources = query(query);
        List<AbsContactItem> items = new ArrayList<>(sources.size());
        for (UserInfoProvider.UserInfo u : sources) {

            /*ContactHelper.makeContactFromUserInfo(u, 2)该方法返回IContact数据,正好可以放在ContactItem中

            ContactItem(IContact contact, int type)这是ContactItem的构造方法，正好符合
            ContactItem扩展了 AbsContactItem类*/
            items.add(new ContactItem(ContactHelper.makeContactFromUserInfo(u, 2), ItemTypes.FRIEND));
        }

        LogUtil.i(UIKitLogTag.CONTACT, "contact provide data size =" + items.size());
        return items;
    }

    // 核心语句，对这个类型的数据进行query字段的搜索,然后将hit到的数据进行返回
    private static final List<UserInfoProvider.UserInfo> query(TextQuery query) {
        if (query != null) {
            List<UserInfoProvider.UserInfo> users = NimUIKit.getContactProvider().getUserInfoOfMyFriends();
            UserInfoProvider.UserInfo user;
            for (Iterator<UserInfoProvider.UserInfo> iter = users.iterator(); iter.hasNext(); ) {
                user = iter.next();
                Friend friend = FriendDataCache.getInstance().getFriendByAccount(user.getAccount());
                boolean hit = ContactSearch.hitUser(user, query) || (friend != null && ContactSearch.hitFriend(friend, query));
                if (!hit) {
                    iter.remove();
                }
            }
            return users;
        } else {
            return NimUIKit.getContactProvider().getUserInfoOfMyFriends();
        }
    }
}