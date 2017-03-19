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

package com.imfan.j.a91fan.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.imfan.j.a91fan.main.viewholder.SystemMessageViewHolder;
import com.netease.nim.uikit.common.adapter.TAdapter;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;

import java.util.List;

/**
 * Created by jay on 17-2-19.
 */

public class SystemMessageAdapter extends TAdapter {

    private SystemMessageViewHolder.SystemMessageListener systemMessageListener;

    public SystemMessageAdapter(Context context, List<?> items, TAdapterDelegate delegate,
                                SystemMessageViewHolder.SystemMessageListener listener) {
        super(context, items, delegate);
        this.systemMessageListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (systemMessageListener != null) {
            ((SystemMessageViewHolder) view.getTag()).setListener(systemMessageListener);
        }

        return view;
    }
}
