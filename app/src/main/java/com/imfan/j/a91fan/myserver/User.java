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


    /**
     * user : {"id":11,"nickname":"又土又逗，简称土逗。","wxUnion":"oPXUEwJCJZXKEnamdhBozBI98Ico"}
     * status : 200
     */

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
         * id : 11
         * nickname : 又土又逗，简称土逗。
         * wxUnion : oPXUEwJCJZXKEnamdhBozBI98Ico
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
