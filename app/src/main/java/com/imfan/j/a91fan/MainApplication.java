package com.imfan.j.a91fan;

import android.app.Application;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.imfan.j.a91fan.config.UserPreferences;

import com.imfan.j.a91fan.session.NimDemoLocationProvider;
import com.imfan.j.a91fan.util.SystemUtil;

import com.imfan.j.a91fan.util.crash.AppCrashHandler;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.custom.DefalutUserInfoProvider;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.imfan.j.a91fan.Constant;

import static com.imfan.j.a91fan.Constant.APP_ID;


/**
 * Created by J on 2017/1/4 0004.
 */

public class MainApplication extends Application {

   public static IWXAPI api;
    private static String TAG = "MainApplication";
    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };

    private void regToWx(){
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
        Log.d(TAG, "注册成功");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        regToWx(); // 向微信注册
        Cache.setContext(this);  // 这里是实例化Cahce
        NIMClient.init(this, getLoginInfo(), getOptions());  // null是默认配置
        AppCrashHandler.getInstance(this);
        if (inMainProcess()){
            // 不适用uikit模块
            initUIKit();
        }
    }

    private void initUIKit() {
        // 初始化，使用 uikit 默认的用户信息提供者
        NimUIKit.init(this);

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // 会话窗口的定制初始化。
       // SessionHelper.init();

        // 通讯录列表定制初始化
       // ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        // NimUIKit.CustomPushContentProvider(new DemoPushContentProvider());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            Cache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {

            Log.i(TAG, "没有本地用户");
            return null;
        }
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。

        // load 应用的状态栏配置
        StatusBarNotificationConfig config = loadStatusBarNotificationConfig();

        // load 用户的 StatusBarNotificationConfig 设置项
        StatusBarNotificationConfig userConfig = UserPreferences.getStatusConfig();
        if (userConfig == null) {
            userConfig = config;
            UserPreferences.setStatusConfig(config);
        }
        // SDK statusBarNotificationConfig 生效
        options.statusBarNotificationConfig = userConfig;

        // 配置保存图片，文件，log等数据的目录
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "91FAN";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
         options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

        // 用户信息提供者
       options.userInfoProvider = new DefalutUserInfoProvider(this);

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        // 在线多端同步未读数
        options.sessionReadAck = true;

        return options;
    }

    // 这里开发者可以自定义该应用的 StatusBarNotificationConfig
    private StatusBarNotificationConfig loadStatusBarNotificationConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        config.notificationEntrance = WelcomeActivity.class;
        config.notificationSmallIconId = R.mipmap.icon;

        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";

        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        // save cache，留做切换账号备用
        Cache.setNotificationConfig(config);
        return config;
    }
}
