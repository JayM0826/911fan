package com.imfan.j.a91fan;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.imfan.j.a91fan.main.MainActivity;
import com.imfan.j.a91fan.util.Cache;
import com.imfan.j.a91fan.util.CustomeActivityManager;
import com.imfan.j.a91fan.util.Extras;
import com.imfan.j.a91fan.util.LogUtil;
import com.imfan.j.a91fan.util.Preferences;
import com.imfan.j.a91fan.util.SysInfoUtil;
import com.imfan.j.a91fan.wxapi.WXEntryActivity;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;


/**
 * 欢迎界面，进入欢迎界面才能进入名副其实的MainActivity
 */
public class WelcomeActivity extends Activity {

    private static final String TAG = "WelcomeActivity";
    private static boolean firstEnter = true; // 是否首次进入
    /**
     * 基本权限管理
     */
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    private boolean customSplash = false;

    // 生命周期第一步
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 无状态栏，actionbar，且全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);


        if (savedInstanceState != null) {
            setIntent(new Intent()); // 从堆栈恢复，不再重复解析之前的intent
        }

        if (!firstEnter) { // 不是首次进入
            onIntent();
        } else {
            showSplashView();  // 首次进入则显示欢迎界面
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomeActivityManager.getCustomeActivityManager().pushActivity(this);
    }

    // 生命周期第三步
    @Override
    protected void onResume() {  // 熟记Android生命周期的重要性
        super.onResume();
        CustomeActivityManager.getCustomeActivityManager().popActivity(this);
        requestBasicPermission();
        if (firstEnter) {  // 第一次进入
            firstEnter = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (canAutoLogin()) {
                        onIntent();
                    } else {

                    }
                }
            };
            if (customSplash) { // 启动欢迎页，1秒退出
                new Handler().postDelayed(runnable, 500);
            } else {
                runnable.run();
            }
        }
    }

    private void requestBasicPermission() {
        MPermission.with(WelcomeActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        WXEntryActivity.start(WelcomeActivity.this); // 启动登录程序

        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        finish(); // 结束欢迎页activity
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /**
         * 如果Activity在，不会走到onCreate，而是onNewIntent，这时候需要setIntent
         * 场景：点击通知栏跳转到此，会收到Intent
         */
        setIntent(intent);
        onIntent();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
    }

    // 处理收到的Intent
    private void onIntent() {
        LogUtil.i(TAG, "onIntent...");

        if (TextUtils.isEmpty(Cache.getAccount())) {
            // 并没有账户数据，继续判断当前app是否正在运行
            if (!SysInfoUtil.stackResumed(this)) {
                // 拉起微信登录
                Intent intent = new Intent(this, WXEntryActivity.class);
                startActivity(intent);
            }
            finish();
        } else {
            // 已经登录过了，处理过来的请求
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    parseNotifyIntent(intent);
                    return;
                } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P) /*|| intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)*/) {
                    parseNormalIntent(intent);
                }
            }

            if (!firstEnter && intent == null) {
                finish();
            } else {
                showMainActivity();
            }
        }
    }

    /**
     * 已经登陆过，自动登陆，网易这里的登录只需要用户名和token，别的不需要
     */
    private boolean canAutoLogin() {
        String account = Preferences.getUserAccount();  // 获取缓存中的账户
        String token = Preferences.getNeteaseToken();  // 获取缓存中的口令，但不一定存在

        Log.i(TAG, "get local sdk token =" + token);
        // 如果账户和口令都在缓存中存在，那么则可以实现自动登陆

        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }


    private void parseNotifyIntent(Intent intent) {
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
        if (messages == null || messages.size() > 1) {
            showMainActivity(null);
        } else {
            showMainActivity(new Intent().putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, messages.get(0)));
        }
    }

    private void parseNormalIntent(Intent intent) {
        showMainActivity(intent);
    }

    /**
     * 首次进入，打开欢迎界面
     */
    private void showSplashView() {
        getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
        customSplash = true;

    }

    private void showMainActivity() {
        showMainActivity(null);
    }

    private void showMainActivity(Intent intent) {
        MainActivity.start(WelcomeActivity.this, intent); // 跳转到主界面
        finish();
    }


}
