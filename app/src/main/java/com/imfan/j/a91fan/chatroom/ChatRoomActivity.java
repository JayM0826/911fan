package com.imfan.j.a91fan.chatroom;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
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


import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChatRoomActivity extends AppCompatActivity {



    private String roomID;
    private String owner;
    private EMChatRoom chatroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomid");
        owner = intent.getStringExtra("owner");
        chatroom = EMClient.getInstance().chatroomManager().getChatRoom(roomID);
        LogUtil.i("查看该聊天室在线人数", chatroom.getMemberCount() + "");
        EMClient.getInstance().chatroomManager().joinChatRoom(roomID, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom value) {
                LogUtil.i("ChatRoomActivity的onCreate", "进入房间成功" + value.getName());
                final SweetAlertDialog dialog = new SweetAlertDialog(ChatRoomActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("成功")
                        .setContentText("进入聊天室成功");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 2000);

            }

            @Override
            public void onError(int error, String errorMsg) {
                LogUtil.i("ChatRoomActivity的onCreate", "进入房间失败" + error + errorMsg);
                final SweetAlertDialog dialog = new SweetAlertDialog(ChatRoomActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("成功")
                        .setContentText("进入聊天室失败");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 2000);

            }
        });

        init();
    }

    /**
     * 获取所在房间的roomID
     * @return roomid
     */
    private String getRoomID(){
        return roomID;
    }


    private EMChatRoomChangeListener chatroomListener;
    /**
     * 添加监听器
     */
    private void init() {

        chatroomListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(getRoomID())) {
                    // 监测到了一个房间被删除且就是我们这个房间，则退出
                    LogUtil.i("ChatRoomActivity的init()", "我们这个房间被删除了");
                    finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                LogUtil.i("查看该聊天室在线人数", chatroom.getMemberCount() + "");
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                                       String participant) {
                LogUtil.i("查看该聊天室在线人数", chatroom.getMemberCount() + "");

            }


            @Override
            public void onRemovedFromChatRoom(String roomId, String roomName, String participant) {

            }

            @Override
            public void onMuteListAdded(final String chatRoomId, final List<String> mutes, final long expireTime) {

            }

            @Override
            public void onMuteListRemoved(final String chatRoomId, final List<String> mutes) {

            }

            @Override
            public void onAdminAdded(final String chatRoomId, final String admin) {

            }

            @Override
            public void onAdminRemoved(final String chatRoomId, final String admin) {

            }

            @Override
            public void onOwnerChanged(final String chatRoomId, final String newOwner, final String oldOwner) {

            }
        };
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatroomListener);
    }

    // 注销监听器
    @Override
    protected void onDestroy() {
        if (chatroomListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatroomListener);
        }
    }
}
