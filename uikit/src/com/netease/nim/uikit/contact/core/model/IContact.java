package com.netease.nim.uikit.contact.core.model;

// 一个接口，目前作用不明朗,显示用户名，确定用户类型
public interface IContact {

    /**
     * get contact id
     *
     * @return
     */
    String getContactId();

    /**
     * get contact type {@link Type}
     *
     * @return
     */
    int getContactType();

    /**
     * get contact's display name to show to user
     *
     * @return
     */
    String getDisplayName();

    interface Type {

        /**
         * TYPE USER
         */
        int User = 0x1;

        int Friend = 0x2;

        /**
         * TYPE TEAM
         */
        int Team = 0x3;

        /**
         * TYPE TEAM MEMBER
         */
        int TeamMember = 0x04;

        /**
         * TYPE_MSG
         */
        int Msg = 0x05;
    }
}
