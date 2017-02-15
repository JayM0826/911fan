package com.imfan.j.a91fan.contact.constant;

/**
 * Created by jay on 17-2-8.
 */

public class UserConstant {

    public static final int KEY_NICKNAME= 1;
    public static final int KEY_GENDER= 2;
    public static final int KEY_BIRTH= 3;
    public static final int KEY_PHONE= 4;
    public static final int KEY_EMAIL= 5;
    public static final int KEY_SIGNATURE = 6;
    // 每个人创建时都会给一个纯数字的号码，与云信的id相映射，可能还会弄一个昵称存储，
    // 这样用户搜索昵称时也可以返回，从1开始，人为的创造一个数字用来表示是
    // 该明星的第多少号粉丝
    public static final int KEY_ALIAS = 7;
    public static final int KEY_ACCOUNT = 8;
}
