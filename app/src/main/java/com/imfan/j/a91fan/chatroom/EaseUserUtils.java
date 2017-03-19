/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-16 下午2:00
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

package com.imfan.j.a91fan.chatroom;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.imfan.j.a91fan.R;

/**
 * Created by J on 2017/3/15 0015.
 */

public class EaseUserUtils {

    static EaseUI.EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
        EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
            EaseUser user = getUserInfo(username);
            if(user != null && user.getNick() != null){
                textView.setText(user.getNick());
            }else{
                textView.setText(username);
            }
        }
    }

}
