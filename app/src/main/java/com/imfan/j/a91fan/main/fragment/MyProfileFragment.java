package com.imfan.j.a91fan.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.main.model.MainTab;
import com.imfan.j.a91fan.util.CustomToast;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by jay on 17-2-6.
 */

public class MyProfileFragment extends MainFragment implements View.OnClickListener {




    private final String TAG = "MyProfileFragment:";
    private View headLayout, blog, following, fans, article, favorite, draft;
    private TextView settings;

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
        headLayout = (View)getView().findViewById(R.id.head_layout);
        headLayout.setOnClickListener(this);

        blog = (View)getView().findViewById(R.id.blog);
        blog.setOnClickListener(this);

        article = (View)getView().findViewById(R.id.article);
        article.setOnClickListener(this);

        draft = (View)getView().findViewById(R.id.draft);
        draft.setOnClickListener(this);

        favorite = (View)getView().findViewById(R.id.favorite);
        favorite.setOnClickListener(this);

        fans = (View)getView().findViewById(R.id.fans);
        fans.setOnClickListener(this);

        following = (View)getView().findViewById(R.id.following);
        following.setOnClickListener(this);

        settings = (TextView)getView().findViewById(R.id.user_settings);
        settings.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_layout:
                CustomToast.show(getContext(), "修改用户资料");
                break;
            case R.id.blog:
                CustomToast.show(getContext(), "将要打开我的动态");
                break;
            case R.id.following:
                CustomToast.show(getContext(), "将要查看我关注的人");
                break;
            case R.id.fans:
                CustomToast.show(getContext(), "将要查看谁关注了我");
                break;
            case R.id.article:
                CustomToast.show(getContext(), "将要打开我写的文章");
                break;
            case R.id.draft:
                CustomToast.show(getContext(), "将要打开我的草稿");
                break;
            case R.id.favorite:
                CustomToast.show(getContext(), "将要打开我的收藏");
                break;
            case R.id.user_settings:
                CustomToast.show(getContext(), "进行系统应用设置");
                break;
            default:
                break;
        }
    }
}
