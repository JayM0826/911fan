/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-18 下午4:02
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
 * Created by J on 2017/3/18 0018.
 */

public class User {

    private UserBean user;
    private int status;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class UserBean {
        /**
         * id : 4
         * nickname : 哎呦哥哥的完美主义
         * wxUnion : 123456789
         */

        private int id;
        private String nickname;
        private String wxUnion;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getWxUnion() {
            return wxUnion;
        }

        public void setWxUnion(String wxUnion) {
            this.wxUnion = wxUnion;
        }
    }
}
