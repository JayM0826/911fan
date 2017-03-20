/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 上午7:39
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

package com.imfan.j.a91fan.textabout.item;

/**
 * Created by J on 2017/3/19 0019.
 * 用来展示动态
 */
public class BlogItem {

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String nickname;

    private String content;
    private long time;
    private int serverBlogID;

    public String getWxunion() {
        return wxunion;
    }

    public void setWxunion(String wxunion) {
        this.wxunion = wxunion;
    }

    private String wxunion;

    public int getServerBlogID() {
        return serverBlogID;
    }

    public void setServerBlogID(int serverBlogID) {
        this.serverBlogID = serverBlogID;
    }


    public BlogItem(String content, long time, int blogID, String nickname, String wxunion) {
        this.content = content;
        this.time = time;
        this.wxunion = wxunion;
        this.serverBlogID = blogID;
        this.nickname = nickname;

    }

    public BlogItem() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}