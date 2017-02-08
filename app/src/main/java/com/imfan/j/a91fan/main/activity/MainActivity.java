package com.imfan.j.a91fan.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.config.UserPreferences;
import com.imfan.j.a91fan.loginabout.LogoutManager;
import com.imfan.j.a91fan.main.fragment.HomeFragment;
import com.imfan.j.a91fan.util.CustomToast;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.common.activity.CustomActivityManager;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.contact_selector.activity.ContactSelectActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

import static com.netease.nimlib.sdk.StatusCode.LOGINED;

public class MainActivity extends UI {

    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;

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
        context.startActivity(intent);  // 启动MainActivity

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusCode status = NIMClient.getStatus(); // 获取在线状态
        if (status == LOGINED)
        {
            // CustomToast.show(this, getString(R.string.line_logined));
            Toast.makeText(this, R.string.line_logined, Toast.LENGTH_SHORT).show();
            LogUtil.i(TAG, getString(R.string.line_logined));
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

        showMainFragment();


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
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_btn:
                LogUtil.i(TAG, "启动搜索");
                Toast.makeText(this, "已经打开全局搜索界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.notify_btn:
                LogUtil.i(TAG, "打开推送通知");
                Toast.makeText(this, "已经打开推送消息", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "你打开了P2P聊天。", Toast.LENGTH_SHORT).show();
                    // SessionHelper.startP2PSession(this, message.getSessionId());
                    LogUtil.i(TAG, "你打开了P2P聊天。");
                    break;
                case Team:
                    Toast.makeText(this, "你打开了群聊。", Toast.LENGTH_SHORT).show();
                    LogUtil.i(TAG, "你打开了群聊。");
                    // SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
                case ChatRoom:
                    Toast.makeText(this, "你打开了聊天室。", Toast.LENGTH_SHORT).show();
                    LogUtil.i(TAG, "你打开了聊天室。");
                    break;
                default:
                    break;
            }
        } else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
            LogUtil.i(TAG, "马上注销！");
            Toast.makeText(this, "你已经选择了注销。", Toast.LENGTH_SHORT).show();
            return;
        }  /*else if (intent.hasExtra(com.netease.nim.demo.main.model.Extras.EXTRA_JUMP_P2P)) {
            Intent data = intent.getParcelableExtra(com.netease.nim.demo.main.model.Extras.EXTRA_DATA);
            String account = data.getStringExtra(com.netease.nim.demo.main.model.Extras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        }*/
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听
        LogoutManager.logout();
        // 消除所有的Activity
        CustomActivityManager.getCustomActivityManager().popAllActivity();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    // TeamCreateHelper.createNormalTeam(MainActivity.this, selected, false, null);
                } else {
                    Toast.makeText(MainActivity.this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                // TeamCreateHelper.createAdvancedTeam(MainActivity.this, selected);
            }
        }

    }

}
