package com.imfan.j.a91fan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.imfan.j.a91fan.util.LogUtil;
import com.imfan.j.a91fan.util.SysInfoUtil;
import com.imfan.j.a91fan.wxapi.WXEntryActivity;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;


public class WelcomeActivity extends AppCompatActivity  {

    private static final String TAG = "WelcomeActivity";

    private boolean customSplash = false;

    private static boolean firstEnter = true; // 是否首次进入


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        /*Intent intent = new Intent(this, WXEntryActivity.class);
        startActivity(intent);
        finish();
*/

        if (savedInstanceState != null){
            setIntent(new Intent()); // 从堆栈恢复，不再重复解析之前的intent
        }

        if (!firstEnter){ // 不是首次进入
            onIntent();
        }else{
            showSplashView();  // 首次进入则显示欢迎界面
        }
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

        if (TextUtils.isEmpty(Cache.getAccount())) {  // 查看缓存是否有账户数据
            // 并没有账户数据，继续判断当前app是否正在运行
            if (!SysInfoUtil.stackResumed(this)) {
                Intent intent = new Intent(this, WXEntryActivity.class);
                startActivity(intent);
            }
            finish();
        } else {
            // 已经登录过了，处理过来的请求
            Intent intent = getIntent();
            /*if (intent != null) {
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    parseNotifyIntent(intent);
                    return;
                } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P) || intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
                    parseNormalIntent(intent);
                }
            }*/

            if (!firstEnter && intent == null) {
                finish();
            } else {
                showMainActivity();
            }
        }
    }

    /**
     * 已经登陆过，自动登陆
     */
    private boolean canAutoLogin() {
        //String account = Preferences.getUserAccount();  // 获取缓存中的账户
        // String token = Preferences.getUserToken();  // 获取缓存中的口令，但不一定存在

        //Log.i(TAG, "get local sdk token =" + token);
        // 如果账户和口令都在缓存中存在，那么则可以实现自动登陆
        return false;
        // return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
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
        getWindow().setBackgroundDrawableResource(R.mipmap.welcome_bg);
        customSplash = true;
        WXEntryActivity.start(this);
        finish();
    }

    private void showMainActivity() {
        showMainActivity(null);
    }

    private void showMainActivity(Intent intent) {
        // MainActivity.start(WelcomeActivity.this, intent);
        finish();
    }


}
