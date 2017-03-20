/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午9:38
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
 */
public class CommentItem {
    public String getWxunion() {
        return wxunion;
    }

    public void setWxunion(String wxunion) {
        this.wxunion = wxunion;
    }

    private String wxunion;

    public CommentItem(String content, String nickname, long time, String wxunion) {
        this.content = content;
        this.wxunion = wxunion;
        this.nickname = nickname;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private int getFanID() {
        return fanID;
    }

    private void setFanID(int fanID) {
        this.fanID = fanID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private String content;
    private int fanID;
    private String nickname;
    private long time;


}