package com.imfan.j.a91fan.contact.helper;

import android.content.Context;

import com.imfan.j.a91fan.contact.activity.UserProfileActivity;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.contact.ContactEventListener;

/**
 * UIKit联系人列表定制展示类
 * Created by jay on 17-2-17.
 */

public class ContactHelper {

    public static void init() {
        setContactEventListener();
    }

    private static void setContactEventListener() {
        NimUIKit.setContactEventListener(new ContactEventListener() {
            @Override
            public void onItemClick(Context context, String account) {
                UserProfileActivity.start(context, account);
            }

            @Override
            public void onItemLongClick(Context context, String account) {

            }

            @Override
            public void onAvatarClick(Context context, String account) {
                UserProfileActivity.start(context, account);
            }
        });
    }

}
