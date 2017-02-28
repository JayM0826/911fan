package com.imfan.j.a91fan.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.main.fragment.HomeFragment;
import com.imfan.j.a91fan.main.helper.SystemMessageUnreadManager;
import com.imfan.j.a91fan.netease.LoginNetease;
import com.imfan.j.a91fan.netease.LogoutManager;
import com.imfan.j.a91fan.netease.UserPreferences;
import com.imfan.j.a91fan.session.SessionHelper;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Extras;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.MD5;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import static com.netease.nimlib.sdk.StatusCode.LOGINED;

public class MainActivity extends UI {

/*Client ID：	YXA6SiOfAPz-EeaDnNF12_Y7Bg
Client Secret：	YXA6ilSCh_mBhZipU7iT5NObQmr244I*/
    /*client_id 和 client_secret 可以在环信管理后台的 APP 详情页面看到。

Path: /{org_name}/{app_name}/token
HTTP Method: POST
URL Params: 无
Request Headers: {“Content-Type”:”application/json”}
Request Body: {“grant_type”: “client_credentials”,”client_id”: “{APP的client_id}”,”client_secret”: “{APP的client_secret}”}
Response Body:
Key	Value
access_token	token 值
expires_in	token 有效时间，以秒为单位，在有效期内不需要重复获取
application	当前 APP 的 UUID 值
可能的错误码：400（client_id 或 client_secret 错误）、5xx。详见：服务器端REST API常见错误码
curl 示例：

curl -X POST "https://a1.easemob.com/easemob-demo/chatdemoui/token" -d '{"grant_type":"client_credentials","client_id":"YXA6wDs-MARqEeSO0VcBzaqg11","client_secret":"YXA6JOMWlLap_YbI_ucz77j-4-mI0dd"}'
Response 示例：

{
  "access_token":"YWMtWY779DgJEeS2h9OR7fw4QgAAAUmO4Qukwd9cfJSpkWHiOa7MCSk0MrkVIco",
  "expires_in":5184000,
  "application":"c03b3e30-046a-11e4-8ed1-5701cdaaa0e4"
}
*/
    // https://a1.easemob.com/1122170227115459/91fan/users
    static public boolean isRed = false;

    private static final String EXTRA_APP_QUIT = "APP_QUIT";

    private final String TAG = getClass().getSimpleName();
    private HomeFragment homeFragment;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        // 日后如果有问题记着改参数
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (extras != null) {
            intent.putExtras(extras);
        }
        FriendDataCache.getInstance().buildCache(); // 再次更新一下好友数据
        context.startActivity(intent);  // 启动MainActivity

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginHuanxin();

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();
        NimUIKit.setAccount(Preferences.getUserAccount().toLowerCase());
        StatusCode status = NIMClient.getStatus(); // 获取在线状态
        if (status != LOGINED)
        {
            LoginNetease.getInstance().login(this);
        }
        onParseIntent();
        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {

                syncPushNoDisturb(UserPreferences.getStatusConfig());

                DialogMaker.dismissProgressDialog();
            }
        });

        LogUtil.i(TAG, "sync completed = " + syncCompleted);
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(MainActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        }else {
            syncPushNoDisturb(UserPreferences.getStatusConfig());
        }

        showMainFragment();


    }

    private void loginHuanxin() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                EMClient.getInstance().login(Preferences.getUserAccount().toLowerCase(),MD5.getStringMD5(Preferences.getUserAccount().toLowerCase()), new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {

                        Log.d("main", "登录聊天服务器成功！");
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("main", "登录聊天服务器失败！" + code  + "   " + message);
                    }
                });
            }
        };
        thread.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
    }

    private void showMainFragment() {
        if (homeFragment == null && !isDestroyedCompatible()) {
            homeFragment = new HomeFragment();
            switchFragmentContent(homeFragment);
            LogUtil.i(TAG, "进入homeFragment!");
        }
    }
    /**
     * 若增加第三方推送免打扰（V3.2.0新增功能），则：
     * 1.添加下面逻辑使得 push 免打扰与先前的设置同步。
     * 2.设置界面{ com.netease.nim.demo.main.activity.SettingsActivity} 以及
     * 免打扰设置界面{ com.netease.nim.demo.main.activity.NoDisturbActivity} 也应添加 push 免打扰的逻辑
     * <p>
     * 注意：isPushDndValid 返回 false， 表示未设置过push 免打扰。
     */
    private void syncPushNoDisturb(StatusBarNotificationConfig staConfig) {

        boolean isNoDisbConfigExist = NIMClient.getService(MixPushService.class).isPushNoDisturbConfigExist();

        if(!isNoDisbConfigExist && staConfig.downTimeToggle) {
            NIMClient.getService(MixPushService.class).setPushNoDisturbConfig(staConfig.downTimeToggle,
                    staConfig.downTimeBegin, staConfig.downTimeEnd);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        onParseIntent();
    }

    @Override
    public void onBackPressed() {
        if (homeFragment != null) {
            if (homeFragment.onBackPressed()) {
                return;
            } else {
                moveTaskToBack(true);
            }
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        if (isRed){
            menu.getItem(1).setIcon(R.drawable.actionbar_notify_red);
        }else{
            menu.getItem(1).setIcon(R.drawable.actionbar_notify);
        }
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_btn:
                LogUtil.i(TAG, "启动搜索");
                SearchActivity.start(this);
                break;
            case R.id.notify_btn:
                LogUtil.i(TAG, "打开推送通知");
                SystemMessageActivity.start(this);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onParseIntent() {
        /** Return the intent that started this activity. */
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            switch (message.getSessionType()) {
                case P2P:
                    SessionHelper.startP2PSession(this, message.getSessionId());
                    LogUtil.i(TAG, "你打开了P2P聊天。");
                    break;
                case Team:
                    LogUtil.i(TAG, "你打开了群聊。");
                    SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
                case ChatRoom:
                    LogUtil.i(TAG, "你打开了聊天室。");
                    break;
                default:
                    break;
            }
        } else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
            LogUtil.i(TAG, "马上注销！");
            CustomToast.show(this, "你已经选择了注销。");
            return;
        }  else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P)) {
            Intent data = intent.getParcelableExtra(Extras.EXTRA_DATA);
            String account = data.getStringExtra(Extras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        }
    }

    // 注销
    public void onLogout() {
        // 清理缓存&注销监听
        LogoutManager.logout();

        finish();
    }



    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            if (unreadCount > 0){
                isRed = true;
                invalidateOptionsMenu();//重新调用
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            LogUtil.i(getClass().getSimpleName(), "环信连接成功");
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        CustomToast.show(MainActivity.this, "帐号已经被移除");
                        // 显示
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示
                        LogUtil.e(getClass().getSimpleName(), "帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)){
                            LogUtil.e(getClass().getSimpleName(), "环信连接不上服务器");
                        }
                        else{
                            LogUtil.e(getClass().getSimpleName(), "当前网络不可用，请检查网络设置");
                            //当前网络不可用，请检查网络设置
                        }

                    }
                }
            });
        }
    }



}
