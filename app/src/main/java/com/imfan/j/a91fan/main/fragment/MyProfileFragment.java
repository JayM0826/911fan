package com.imfan.j.a91fan.main.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.loginabout.LogoutManager;
import com.imfan.j.a91fan.main.model.MainTab;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;

/**
 * Created by jay on 17-2-6.
 */

public class MyProfileFragment extends MainFragment implements View.OnClickListener {




    private final String TAG = "MyProfileFragment:";
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

        findViews();
        initEvents();
        initDatas();




    }

    private void initDatas(){
        fanNickName.setText(Preferences.getWxNickname());
        if (!(Preferences.getFanId() == null)){ // fanid不为空
            fanID.setText(Preferences.getFanId());
        } // 否则默认显示


    }

    private void initEvents(){
        headLayout.setOnClickListener(this);
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
            case R.id.user_logout:
                LogoutManager.logout();
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
