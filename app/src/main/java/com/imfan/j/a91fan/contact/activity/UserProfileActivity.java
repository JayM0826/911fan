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

package com.imfan.j.a91fan.contact.activity;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.session.SessionHelper;
import com.imfan.j.a91fan.textabout.BlogListActivity;
import com.imfan.j.a91fan.textabout.UserBlogListActivity;
import com.imfan.j.a91fan.util.Cache;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Extras;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.model.MuteListChangedNotify;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class UserProfileActivity extends SwipeBackActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private final String KEY_BLACK_LIST = "black_list";
    private final String KEY_MSG_NOTICE = "msg_notice";
    LinearLayout linearLayout;
    private boolean destroyed = false;
    private String account;
    // 基本信息
    private HeadImageView headImageView;
    private TextView accountText;
    private TextView birthdayText;
    private TextView mobileText;
    private TextView emailText;
    private TextView signatureText;
    private TextView articleText;
    private TextView blogText;

    private RelativeLayout birthdayLayout;
    private RelativeLayout phoneLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout signatureLayout;
    private RelativeLayout articleLayout;
    private RelativeLayout blogLayout;

    private TextView nickText;
    // 开关
    private ViewGroup toggleLayout;
    private Button chatBtn;
    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onDeletedFriends(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            updateUserOperatorView();
        }
    };
    private SwitchButton blackSwitch;
    private SwitchButton noticeSwitch;
    Observer<MuteListChangedNotify> muteListChangedNotifyObserver = new Observer<MuteListChangedNotify>() {
        @Override
        public void onEvent(MuteListChangedNotify notify) {
            setToggleBtn(noticeSwitch, !notify.isMute());
        }
    };
    private Map<String, Boolean> toggleStateMap;
    private ActionBar actionBar;
    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            final String key = (String) v.getTag();
            if (!NetworkUtil.isNetAvailable(UserProfileActivity.this)) {
                CustomToast.show(UserProfileActivity.this, R.string.network_is_not_available);
                if (key.equals(KEY_BLACK_LIST)) {
                    blackSwitch.setCheck(!checkState);
                } else if (key.equals(KEY_MSG_NOTICE)) {
                    noticeSwitch.setCheck(!checkState);
                }
                return;
            }

            updateStateMap(checkState, key);

            if (key.equals(KEY_BLACK_LIST)) {
                if (checkState) {
                    NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            CustomToast.show(UserProfileActivity.this, "加入黑名单成功");
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                CustomToast.show(UserProfileActivity.this, R.string.network_is_not_available);
                            } else {
                                CustomToast.show(UserProfileActivity.this, "on failed：" + code);
                            }
                            updateStateMap(!checkState, key);
                            blackSwitch.setCheck(!checkState);
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    NIMClient.getService(FriendService.class).removeFromBlackList(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            CustomToast.show(UserProfileActivity.this, "移除黑名单成功");
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                CustomToast.show(UserProfileActivity.this, R.string.network_is_not_available);
                            } else {
                                CustomToast.show(UserProfileActivity.this, "on failed:" + code);
                            }
                            updateStateMap(!checkState, key);
                            blackSwitch.setCheck(!checkState);
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                }
            } else if (key.equals(KEY_MSG_NOTICE)) {
                NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (checkState) {
                            CustomToast.show(UserProfileActivity.this, "开启消息提醒成功");
                        } else {
                            CustomToast.show(UserProfileActivity.this, "关闭消息提醒成功");
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            CustomToast.show(UserProfileActivity.this, R.string.network_is_not_available);
                        } else {
                            CustomToast.show(UserProfileActivity.this, "on failed:" + code);
                        }
                        updateStateMap(!checkState, key);
                        noticeSwitch.setCheck(!checkState);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        }
    };
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == chatBtn) { // 去聊天
                onChat();
            }
        }
    };

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.user_profile_activity);

        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);

        initActionbar();

        findViews();
        registerObserver(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUserInfo();
        updateToggleView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.ui("activity: " + getClass().getSimpleName() + " onDestroy()");
        destroyed = true;
        registerObserver(false);
    }

    private void registerObserver(boolean register) {
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
        NIMClient.getService(FriendServiceObserve.class).observeMuteListChangedNotify(muteListChangedNotifyObserver, register);
    }

    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }

    private void findViews() {
        linearLayout = findView(R.id.long_click_copy);
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(account);
                CustomToast.show(UserProfileActivity.this, "您的ID已经复制成功");
                return true;
            }
        });
        headImageView = findView(R.id.user_head_image);
        accountText = findView(R.id.user_account);
        toggleLayout = findView(R.id.toggle_layout);
        chatBtn = findView(R.id.begin_chat);
        birthdayLayout = findView(R.id.birthday);
        nickText = findView(R.id.user_nick);
        birthdayText = (TextView) birthdayLayout.findViewById(R.id.value);
        articleLayout = findView(R.id.article);
        articleText = (TextView)articleLayout.findViewById(R.id.value);
        blogLayout = findView(R.id.blog);
        blogText = (TextView)findViewById(R.id.value);
        phoneLayout = findView(R.id.phone);
        mobileText = (TextView) phoneLayout.findViewById(R.id.value);
        emailLayout = findView(R.id.email);
        emailText = (TextView) emailLayout.findViewById(R.id.value);
        signatureLayout = findView(R.id.signature);
        signatureText = (TextView) signatureLayout.findViewById(R.id.value);
        ((TextView) blogLayout.findViewById(R.id.attribute)).setText(R.string.blog);
        ((TextView) articleLayout.findViewById(R.id.attribute)).setText(R.string.article);
        ((TextView) birthdayLayout.findViewById(R.id.attribute)).setText(R.string.birthday);
        ((TextView) phoneLayout.findViewById(R.id.attribute)).setText(R.string.phone);
        ((TextView) emailLayout.findViewById(R.id.attribute)).setText(R.string.email);
        ((TextView) signatureLayout.findViewById(R.id.attribute)).setText(R.string.signature);
        chatBtn.setOnClickListener(onClickListener);
    }

    private void initActionbar() {

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.user_info);
        actionBar.show();
    }

    private void addToggleBtn(boolean black, boolean notice) {
        blackSwitch = addToggleItemView(KEY_BLACK_LIST, R.string.black_list, black);
        noticeSwitch = addToggleItemView(KEY_MSG_NOTICE, R.string.msg_notice, notice);
    }

    private void setToggleBtn(SwitchButton btn, boolean isChecked) {
        btn.setCheck(isChecked);
    }

    private void updateUserInfo() {
        if (NimUserInfoCache.getInstance().hasUser(account)) {
            updateUserInfoView();
            return;
        }

        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallbackWrapper<NimUserInfo>() {
            @Override
            public void onResult(int code, NimUserInfo result, Throwable exception) {
                updateUserInfoView();
            }
        });
    }

    private void updateUserInfoView() {
        accountText.setText("帐号：" + account);
        headImageView.loadBuddyAvatar(account);

        final NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(account);
        if (userInfo == null) {
            LogUtil.e(TAG, "userInfo is null when updateUserInfoView");
            return;
        }


        if (!TextUtils.isEmpty(userInfo.getBirthday())) {
            birthdayLayout.setVisibility(View.VISIBLE);
            birthdayText.setText(userInfo.getBirthday());
        } else {
            birthdayLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            phoneLayout.setVisibility(View.VISIBLE);
            mobileText.setText(userInfo.getMobile());
        } else {
            phoneLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getEmail())) {
            emailLayout.setVisibility(View.VISIBLE);
            emailText.setText(userInfo.getEmail());
        } else {
            emailLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getSignature())) {
            signatureLayout.setVisibility(View.VISIBLE);
            signatureText.setText(userInfo.getSignature());
        } else {
            signatureLayout.setVisibility(View.GONE);
        }
        nickText.setText("昵称：" + NimUserInfoCache.getInstance().getUserName(account));
        
        blogLayout.setVisibility(View.VISIBLE);
        blogText.setText("查看动态请点击");
        blogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                // 是自己的资料的时候
                if (account.toLowerCase().equals(Preferences.getUserAccount().toLowerCase())){
                    intent = new Intent(UserProfileActivity.this, BlogListActivity.class);
                }else{ // 是其他用户的时候
                    intent = new Intent(UserProfileActivity.this, UserBlogListActivity.class);
                    intent.putExtra("account", account);
                    intent.putExtra("nickname", NimUserInfoCache.getInstance().getUserName(account));
                }
                startActivity(intent);
            }
        });

        articleLayout.setVisibility(View.VISIBLE);
        articleText.setText("查看用户文章功能并未开放");

    }

    private void updateUserOperatorView() {
        chatBtn.setVisibility(View.VISIBLE);

    }

    private void updateToggleView() {
        if (Cache.getAccount() != null && !Cache.getAccount().equals(account)) {
            boolean black = NIMClient.getService(FriendService.class).isInBlackList(account);
            boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
            if (blackSwitch == null || noticeSwitch == null) {
                addToggleBtn(black, notice);
            } else {
                setToggleBtn(blackSwitch, black);
                setToggleBtn(noticeSwitch, notice);
            }
            Log.i(TAG, "black=" + black + ", notice=" + notice);
            updateUserOperatorView();
        }
    }

    private SwitchButton addToggleItemView(String key, int titleResId, boolean initState) {
        ViewGroup vp = (ViewGroup) getLayoutInflater().inflate(R.layout.nim_user_profile_toggle_item, null);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.isetting_item_height));
        vp.setLayoutParams(vlp);

        TextView titleText = ((TextView) vp.findViewById(R.id.user_profile_title));
        titleText.setText(titleResId);

        SwitchButton switchButton = (SwitchButton) vp.findViewById(R.id.user_profile_toggle);
        switchButton.setCheck(initState);
        switchButton.setOnChangedListener(onChangedListener);
        switchButton.setTag(key);

        toggleLayout.addView(vp);

        if (toggleStateMap == null) {
            toggleStateMap = new HashMap<>();
        }
        toggleStateMap.put(key, initState);
        return switchButton;
    }



    private void updateStateMap(boolean checkState, String key) {
        if (toggleStateMap.containsKey(key)) {
            toggleStateMap.put(key, checkState);  // update state
            Log.i(TAG, "toggle " + key + "to " + checkState);
        }
    }



    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    /**
     * 如果是用户本人，则显示编辑按钮；否则，不显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_profile_activity_menu, menu);
        MenuItem menuItem = menu.getItem(0);
        if (!Cache.getAccount().equals(account)) {
            menuItem.setVisible(false);
        } else {
            menuItem.setVisible(true);
        }
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_bar_right_clickable_textview1:
                UserProfileSettingActivity.start(UserProfileActivity.this, account);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }

    private void onChat() {
        Log.i(TAG, "onChat");
        SessionHelper.startP2PSession(this, account);
    }
}

