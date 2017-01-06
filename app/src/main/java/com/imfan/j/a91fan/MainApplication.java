package com.imfan.j.a91fan;

import android.app.Application;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.imfan.j.a91fan.Constant;

import static com.imfan.j.a91fan.Constant.APP_ID;


/**
 * Created by J on 2017/1/4 0004.
 */

public class MainApplication extends Application {


   public static IWXAPI api;

    private void regToWx(){
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
        Toast.makeText(this, "register success", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        regToWx(); // 向微信注册
    }
}
