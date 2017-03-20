/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午7:19
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

package com.imfan.j.a91fan.myserver;

/**
 * Created by J on 2017/3/19 0019.
 * 一般就用来获取一下状态和相应ID
 */


public class Common {

    /**
     * serverBlogId : 31
     * status : 200
     */

    private int serverBlogId;
    private int status;

    public int getServerBlogId() {
        return serverBlogId;
    }

    public void setServerBlogId(int serverBlogId) {
        this.serverBlogId = serverBlogId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
