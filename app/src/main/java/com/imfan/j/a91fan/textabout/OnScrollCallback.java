/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午4:31
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

/**
 * Created by J on 2017/3/19 0019.
 */

public interface OnScrollCallback {
    void onStateChanged(ScrollRecyclerView recycler, int state);

    void onScrollUp(ScrollRecyclerView recycler, int dy);

    void onScrollToBottom();

    void onScrollDown(ScrollRecyclerView recycler, int dy);

    void onScrollToTop();
}
