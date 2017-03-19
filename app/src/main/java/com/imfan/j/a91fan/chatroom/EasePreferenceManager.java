/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-15 上午12:11
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by J on 2017/3/15 0015.
 */

public class EasePreferenceManager {
    private SharedPreferences.Editor editor;
    private SharedPreferences mSharedPreferences;
    private static final String KEY_AT_GROUPS = "AT_GROUPS";

    @SuppressLint("CommitPrefEdits")
    private EasePreferenceManager(){
        mSharedPreferences = EaseUI.getInstance().getContext().getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }
    private static EasePreferenceManager instance;

    public synchronized static EasePreferenceManager getInstance(){
        if(instance == null){
            instance = new EasePreferenceManager();
        }
        return instance;

    }


    public void setAtMeGroups(Set<String> groups) {
        editor.remove(KEY_AT_GROUPS);
        editor.putStringSet(KEY_AT_GROUPS, groups);
        editor.apply();
    }

    public Set<String> getAtMeGroups(){
        return mSharedPreferences.getStringSet(KEY_AT_GROUPS, null);
    }

}
