package com.imfan.j.a91fan.chatroom;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.event.ChatRoomNameEvent;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChatRoomActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        String roomName = intent.getStringExtra("roomName");
        String creator = Preferences.getWxNickname();
        int roomid = intent.getIntExtra("roomid", 0);

        LogUtil.i("ChatRoomActivity", "RoomName为" + roomName + "  创建者为" + creator + "  roomid为" + roomid);
        EnterChatRoomData data = new EnterChatRoomData(roomid + "");
        NIMClient.getService(ChatRoomService.class).enterChatRoom(data)
                .setCallback(new RequestCallback<EnterChatRoomResultData>() {
                    @Override
                    public void onSuccess(EnterChatRoomResultData o) {
                        LogUtil.i("onSuccess进入房间成功", "进入房间成功了");
                        final SweetAlertDialog dialog = new SweetAlertDialog(ChatRoomActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setContentText("恭喜你啊恭喜你");
                        dialog.setTitleText("进入房间");
                        dialog.show();

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        };

                        new Handler().postDelayed(runnable, 2000);
                    }

                    @Override
                    public void onFailed(int i) {

                        final SweetAlertDialog dialog = new SweetAlertDialog(ChatRoomActivity.this, SweetAlertDialog.ERROR_TYPE);
                        dialog.setContentText("进入房间失败了");
                        dialog.setTitleText("oops失败了");
                        LogUtil.i("onFailed进入房间失败", "进入房间失败了" + i);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        };

                        new Handler().postDelayed(runnable,1000);

                    }

                    @Override
                    public void onException(Throwable throwable) {
                        LogUtil.i("onException进入房间出现异常", "进入房间异常了");
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
