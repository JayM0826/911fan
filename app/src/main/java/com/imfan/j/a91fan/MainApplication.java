package com.imfan.j.a91fan;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;


import com.imfan.j.a91fan.config.UserPreferences;
import com.imfan.j.a91fan.contact.helper.ContactHelper;
import com.imfan.j.a91fan.session.SessionHelper;
import com.imfan.j.a91fan.util.Cache;

import com.imfan.j.a91fan.util.Preferences;
import com.imfan.j.a91fan.util.SystemUtil;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.custom.DefalutUserInfoProvider;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import com.imfan.j.a91fan.util.crash.AppCrashHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.imfan.j.a91fan.util.Constant.WX_APP_ID;


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
    private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                updateLocale();
            }
        }
    };

    public static LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getNeteaseToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            Cache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {

            LogUtil.i(TAG, "没有本地用户");
            return null;
        }
    }

    // 不知道为啥用
     /*如何获取Context

    通常我们想要获取Context对象，主要有以下四种方法
    1：View.getContext,返回当前View对象的Context对象，通常是当前正在展示的Activity对象。
    2：Activity.getApplicationContext,获取当前Activity所在的(应用)进程的Context对象，
    通常我们使用Context对象时，要优先考虑这个全局的进程Context。
    3：ContextWrapper.getBaseContext():用来获取一个ContextWrapper进行装饰之前的Context，
    可以使用这个方法，这个方法在实际开发中使用并不多，也不建议使用。
    4：Activity.this 返回当前的Activity实例，如果是UI控件需要使用Activity作为Context对象
    ，但是默认的Toast实际上使用ApplicationContext也可以。
*/
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        api.registerApp(WX_APP_ID);
        LogUtil.i(TAG, "注册成功");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        regToWx(); // 向微信注册
        Cache.setContext(this);  // 这里是实例化Cahce

        // 注册小米推送appID 、appKey 以及在云信管理后台添加的小米推送证书名称，该逻辑放在 NIMClient init 之前
        // NIMPushClient.registerMiPush(this, "DEMO_MI_PUSH", "2882303761517502883", "5671750254883");
        // 注册自定义小米推送消息处理，这个是可选项
        //NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());


        NIMClient.init(this, getLoginInfo(), getOptions());  // null是默认配置
        AppCrashHandler.getInstance(this);
        // ExtraOptions.provide();

        if (inMainProcess()) {
            initUIKit();
            // 注册通知消息过滤器
            registerIMMessageFilter();

            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());


            // 注册语言变化监听
            registerLocaleReceiver(true);
        }
    }


    private void registerLocaleReceiver(boolean register) {
        if (register) {
            updateLocale();
            IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(localeReceiver, filter);
        } else {
            unregisterReceiver(localeReceiver);
        }
    }

    private void updateLocale() {
        NimStrings strings = new NimStrings();
        strings.status_bar_multi_messages_incoming = getString(R.string.nim_status_bar_multi_messages_incoming);
        strings.status_bar_image_message = getString(R.string.nim_status_bar_image_message);
        strings.status_bar_audio_message = getString(R.string.nim_status_bar_audio_message);
        strings.status_bar_custom_message = getString(R.string.nim_status_bar_custom_message);
        strings.status_bar_file_message = getString(R.string.nim_status_bar_file_message);
        strings.status_bar_location_message = getString(R.string.nim_status_bar_location_message);
        strings.status_bar_notification_message = getString(R.string.nim_status_bar_notification_message);
        strings.status_bar_ticker_text = getString(R.string.nim_status_bar_ticker_text);
        strings.status_bar_unsupported_message = getString(R.string.nim_status_bar_unsupported_message);
        strings.status_bar_video_message = getString(R.string.nim_status_bar_video_message);
        strings.status_bar_hidden_message_content = getString(R.string.nim_status_bar_hidden_msg_content);
        NIMClient.updateStrings(strings);
    }

    private void initUIKit() {
        // 初始化，使用 uikit 默认的用户信息提供者
        NimUIKit.init(this);

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        // NimUIKit.setLocationProvider(new FanLocationProvider());

        // 会话窗口的定制初始化。
        SessionHelper.init();

        // 通讯录列表定制初始化
         ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        // NimUIKit.CustomPushContentProvider(new DemoPushContentProvider());
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

    public SDKOptions getOptions() {
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



    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter() {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (UserPreferences.getMsgIgnore() && message.getAttachment() != null) {
                    if (message.getAttachment() instanceof UpdateTeamAttachment) {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet()) {
                            if (field.getKey() == TeamFieldEnum.ICON) {
                                return true;
                            }
                        }
                    } else if (message.getAttachment() instanceof AVChatAttachment) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
