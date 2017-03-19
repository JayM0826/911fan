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

package com.imfan.j.a91fan.contact.activity;

import android.content.Context;

import com.netease.nim.uikit.common.adapter.TAdapter;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.List;

/**
 * Created by jay on 17-2-19.
 */

public class BlackListAdapter extends TAdapter<UserInfoProvider.UserInfo> {

    private ViewHolderEventListener viewHolderEventListener;

    public BlackListAdapter(Context context, List<UserInfoProvider.UserInfo> items, TAdapterDelegate delegate, ViewHolderEventListener
            viewHolderEventListener) {
        super(context, items, delegate);

        this.viewHolderEventListener = viewHolderEventListener;
    }

    public ViewHolderEventListener getEventListener() {
        return viewHolderEventListener;
    }

    public interface ViewHolderEventListener {
        void onItemClick(UserInfoProvider.UserInfo user);

        void onRemove(UserInfoProvider.UserInfo user);
    }
}
