/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午4:28
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

package com.imfan.j.a91fan.textabout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by J on 2017/3/19 0019.
 */

public class ScrollRecyclerView extends RecyclerView {

    OnScrollCallback onScrollCallback;

    public ScrollRecyclerView(Context context) {
        super(context);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollCallback(final OnScrollCallback callback) {
        if (callback == null) {
            return;
        }
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                callback.onStateChanged(ScrollRecyclerView.this, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(1)) {
                        callback.onScrollToBottom();
                    }
                    if (!recyclerView.canScrollVertically(-1)) {
                        callback.onScrollToTop();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    callback.onScrollDown(ScrollRecyclerView.this, dy);
                } else {
                    callback.onScrollUp(ScrollRecyclerView.this, dy);
                }
            }
        });
    }
}
