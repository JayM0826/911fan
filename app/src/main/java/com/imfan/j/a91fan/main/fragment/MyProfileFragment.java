/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-13 上午11:12
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

package com.imfan.j.a91fan.main.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.contact.activity.UserProfileSettingActivity;
import com.imfan.j.a91fan.entity.DaoType;
import com.imfan.j.a91fan.netease.LogoutManager;
import com.imfan.j.a91fan.main.model.MainTab;
import com.imfan.j.a91fan.textabout.BlogListActivity;
import com.imfan.j.a91fan.textabout.GroupListActivity;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.contact.core.model.IContact;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by jay on 17-2-6.
 */

public class MyProfileFragment extends MainFragment implements View.OnClickListener , View.OnLongClickListener{




    private static NimUserInfo userInfo;
    private final String TAG = "MyProfileFragment:";
    HeadImageView userHead;
    private View headLayout, blog, following, fans, article, favorite, draft;
    private TextView user_logout, fanID, fanNickName;

    public MyProfileFragment(){
        setContainerId(MainTab.PROFILE.fragmentId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    @Override
    protected void onInit() {

       /* Glide.with(getActivity()).load(Preferences.getWxHeadimgurl().substring(0, Preferences.getWxHeadimgurl().length() - 2))
        .into(userHead);*/
        findViews();
        initEvents();


    }

    @Override
    public void onResume() {

        super.onResume();

        getUserInfo();
    }

    private void initEvents(){
        headLayout.setOnClickListener(this);
        headLayout.setOnLongClickListener(this);
        blog.setOnClickListener(this);
        article.setOnClickListener(this);
        draft.setOnClickListener(this);
        favorite.setOnClickListener(this);
        fans.setOnClickListener(this);
        fans.setOnClickListener(this);
        following.setOnClickListener(this);
        user_logout.setOnClickListener(this);

    }

    private void findViews(){

        fanID = (TextView)getView().findViewById(R.id.user_fanid);

        userHead = (HeadImageView)getView().findViewById(R.id.user_head1);
        fanNickName = (TextView)getView().findViewById(R.id.user_fanname);

        headLayout = (View)getView().findViewById(R.id.head_layout);

        blog = (View)getView().findViewById(R.id.blog);

        article = (View)getView().findViewById(R.id.article);

        draft = (View)getView().findViewById(R.id.draft);

        favorite = (View)getView().findViewById(R.id.favorite);

        fans = (View)getView().findViewById(R.id.fans);

        following = (View)getView().findViewById(R.id.following);

        user_logout = (TextView)getView().findViewById(R.id.user_logout);


    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        Bundle bundle;
        switch (v.getId()){

            case R.id.head_layout:
                UserProfileSettingActivity.start(getContext(), Preferences.getUserAccount().toLowerCase());
                break;
            case R.id.blog:
                intent = new Intent(getContext(), BlogListActivity.class);

                startActivity(intent);
                break;
            case R.id.following:
                CustomToast.show(getContext(), "将要查看我关注的人");
                break;
            case R.id.fans:
                CustomToast.show(getContext(), "将要查看谁关注了我");
                break;
            case R.id.article:
                intent = new Intent(getContext(), GroupListActivity.class);
                intent.putExtra("title", getResources().getString(R.string.user_acticle));
                bundle = null;
                bundle = new Bundle();
                bundle.putSerializable("type", DaoType.ARTICLE);
                intent.putExtras(bundle);
                startActivity(intent);
                intent = null;
                break;
            case R.id.draft:
                intent = new Intent(getContext(), GroupListActivity.class);
                intent.putExtra("title", getResources().getString(R.string.user_draft));
                bundle = null;
                bundle = new Bundle();
                bundle.putSerializable("type", DaoType.DRAFT);
                intent.putExtras(bundle);
                startActivity(intent);
                intent = null;
                break;
            case R.id.favorite:
                CustomToast.show(getContext(), "将要打开我的收藏");
                break;
            case R.id.user_logout:
                LogoutManager.logout();
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    private void getUserInfo() {

        userInfo = NimUserInfoCache.getInstance().getUserInfo(Preferences.getUserAccount().toLowerCase());

        if (userInfo == null) {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(Preferences.getUserAccount().toLowerCase(), new RequestCallback<NimUserInfo>() {
                @Override
                public void onSuccess(NimUserInfo param) {
                    userInfo = param;
                    updateUI();
                }

                @Override
                public void onFailed(int code) {
                    CustomToast.show(getContext(), "getUserInfoFromRemote failed:" + code);
                }

                @Override
                public void onException(Throwable exception) {
                    CustomToast.show(getContext(), "getUserInfoFromRemote exception:" + exception);
                }
            });
        } else {
            updateUI();
        }
    }


    @Override
    public boolean onLongClick(View v) {
        /*得到剪贴板管理器 */
        ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(Preferences.getUserAccount().toLowerCase());
        CustomToast.show(getContext(), "您的ID已经复制成功");
        return true;
    }

    private void updateUI() {

        userHead.loadBuddyAvatar(Preferences.getUserAccount().toLowerCase());
        fanNickName.setText(userInfo.getName());
        fanID.setText("fan" + Preferences.getFanId());

    }
}
