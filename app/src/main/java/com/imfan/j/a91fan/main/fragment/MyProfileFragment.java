package com.imfan.j.a91fan.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by jay on 17-2-6.
 */

public class MyProfileFragment extends MainFragment {

    // constant
    private static final int PICK_AVATAR_REQUEST = 0x0E;
    private static final int AVATAR_TIME_OUT = 30000;
    private final String TAG = "MyProfileFragment:";
    // data
    AbortableFuture<String> uploadAvatarFuture;
    private String account;
    // view
    private HeadImageView userHead;
    private RelativeLayout nickLayout;
    private RelativeLayout genderLayout;
    private RelativeLayout birthLayout;
    private RelativeLayout phoneLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout signatureLayout;
    private TextView nickText;
    private TextView genderText;
    private TextView birthText;
    private TextView phoneText;
    private TextView emailText;
    private TextView signatureText;
    private NimUserInfo userInfo;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    @Override
    protected void onInit() {

    }

}
