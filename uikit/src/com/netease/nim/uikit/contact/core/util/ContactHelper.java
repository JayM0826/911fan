package com.netease.nim.uikit.contact.core.util;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.model.IContact;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by huangjun on 2015/9/8.
 */
public class ContactHelper {

    /*UserInfoProvider是一个接口，里面的另一个接口就是UserInfo，
    里面有getAccount()，getName(),getAvatar()方法*/

    public static IContact makeContactFromUserInfo(final UserInfoProvider.UserInfo userInfo,final int type) {

        /*直接返回一个IContact数据*/
        return new IContact() {
            @Override
            public String getContactId() {
                return userInfo.getAccount();
            }

            @Override
            public int getContactType() {
                if (type == 1){
                    return ItemTypes.USER;
                }else {
                    return ItemTypes.FRIEND;
                }
            }

            @Override
            public String getDisplayName() {
                return NimUIKit.getContactProvider().getUserDisplayName(userInfo.getAccount());
            }
        };
    }




    public static IContact makeContactFromMsgIndexRecord(final MsgIndexRecord record) {
        return new IContact() {
            @Override
            public String getContactId() {
                return record.getSessionId();
            }

            @Override
            public int getContactType() {
                return Type.Msg;
            }

            @Override
            public String getDisplayName() {
                String sessionId = record.getSessionId();
                SessionTypeEnum sessionType = record.getSessionType();

                if (sessionType == SessionTypeEnum.P2P) {
                    return NimUserInfoCache.getInstance().getUserDisplayName(sessionId);
                } else if (sessionType == SessionTypeEnum.Team) {
                    return TeamDataCache.getInstance().getTeamName(sessionId);
                }

                return "";
            }
        };
    }
}
