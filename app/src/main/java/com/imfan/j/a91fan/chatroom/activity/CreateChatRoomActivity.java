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

package com.imfan.j.a91fan.chatroom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.chatroom.ChatRoomActivity;
import com.imfan.j.a91fan.netease.CheckSumBuilder;
import com.imfan.j.a91fan.netease.NeteaseClient;
import com.imfan.j.a91fan.uiabout.CustomEditText;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import rx.Observable;
import rx.Subscriber;

import static com.imfan.j.a91fan.netease.NeteaseClient.addHeaders;
import static com.imfan.j.a91fan.netease.NeteaseClient.checkSum;
import static com.imfan.j.a91fan.netease.NeteaseClient.curTime;
import static com.imfan.j.a91fan.netease.NeteaseClient.nonce;
import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_SECRET;

public class CreateChatRoomActivity extends Activity {

    CustomEditText mRoomName;

    Button createRoom;

    public void createRoom() {



        if (mRoomName.getText().toString().equals("")) {
            CustomToast.show(this, "房间名不能为空");
            return;
        }

        /*Observable<EMChatRoom> observable = Observable.create(new Observable.OnSubscribe<EMChatRoom>() {
            @Override
            public void call(Subscriber<? super EMChatRoom> subscriber) {
                EMChatRoom chatRoom = null;
                try{
                    chatRoom = EMClient.getInstance()
                            .chatroomManager()
                            .createChatRoom(mRoomName.getText().toString(), "这是第一个聊天室", "欢迎加入第一个聊天室", 500, null);
                }catch (HyphenateException e){
                    e.printStackTrace();
                    final SweetAlertDialog dialog = new SweetAlertDialog(CreateChatRoomActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("创建失败").setContentText("创建房间发生异常,正在跳转").setConfirmText("OK");
                    dialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            finish();
                        }
                    }, 3000);

                    LogUtil.e("CreateChatRoomActivity的createRoom()", "创建聊天室时出现异常");
                    LogUtil.e("CreateChatRoomActivity的createRoom()", e.getDescription());
                    LogUtil.e("CreateChatRoomActivity的createRoom()", e.getErrorCode() + "");
                }

                subscriber.onNext(chatRoom);
                // subscriber.onCompleted();
            }
        });

        Subscriber<EMChatRoom> subscriber = new Subscriber<EMChatRoom>() {
            @Override
            public void onCompleted() {
                LogUtil.i("CreateChatRoomActivity的createRoom()", "创建聊天室成功");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.i("CreateChatRoomActivity的createRoom()", "创建聊天室失败");
            }

            @Override
            public void onNext(EMChatRoom emChatRoom) {
                if (emChatRoom == null){
                    LogUtil.i("onNext中", "创建聊天室不成功");
                    return;
                }

                Intent intent = new Intent(CreateChatRoomActivity.this, ChatRoomActivity.class);
                intent.putExtra("roomid", emChatRoom.getId());
                startActivity(intent);
            }

            @Override
            public void onStart() {
                LogUtil.i("CreateChatRoomActivity的Subscriber<EMChatRoom>", "开始创建聊天室");
            }
        };

        observable.subscribe(subscriber);*/



        Thread thread = new Thread(){
            @Override
            public void run() {
                // you have no permission to create a chatroom!
                Looper.prepare();
                EMChatRoom chatRoom = null;
                try{
                    chatRoom = EMClient.getInstance()
                            .chatroomManager()
                            .createChatRoom(mRoomName.getText().toString(), "这是第一个聊天室", "欢迎加入第一个聊天室", 500, null);
                }catch (HyphenateException e){
                    e.printStackTrace();
                    CustomToast.show(CreateChatRoomActivity.this, "创建房间失败");
                    // catch完还是继续运行的，并不会跳过后面的程序
                    LogUtil.e("CreateChatRoomActivity的createRoom()", "创建聊天室时出现异常");
                    LogUtil.e("CreateChatRoomActivity的createRoom()", e.getDescription());
                    LogUtil.e("CreateChatRoomActivity的createRoom()", e.getErrorCode() + "");
                }

                if (chatRoom == null){
                    LogUtil.i("onNext中", "创建聊天室不成功");
                    finish();
                    return;
                }

                Intent intent = new Intent(CreateChatRoomActivity.this, ChatRoomActivity.class);
                intent.putExtra("roomid", chatRoom.getId());
                startActivity(intent);

                Looper.loop();
            }

        };
        thread.start();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat_room);
        mRoomName = (CustomEditText) findViewById(R.id.et_room_name);
        createRoom = (Button) findViewById(R.id.btn_ok);
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
