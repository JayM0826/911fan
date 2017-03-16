package com.imfan.j.a91fan.chatroom;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.chatroom.viewprovider.MessageItemViewProvider;
import com.imfan.j.a91fan.event.ChatRoomNameEvent;
import com.imfan.j.a91fan.textabout.item.TextItem;
import com.imfan.j.a91fan.textabout.item.TextItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.common.ui.listview.MessageListView;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.session.emoji.EmoticonPickerView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class ChatRoomActivity extends AppCompatActivity {
    private String roomID;
    private String owner;
    private EMChatRoom chatroom;

    private MultiTypeAdapter multiTypeAdapter;

    private Items items;

    @BindView(R.id.messageListView)
    RecyclerView messageListView;

    @BindView(R.id.layoutPlayAudio)
    FrameLayout layoutPlayAudio;

    @BindView(R.id.switchLayout)
    FrameLayout layout;

    @BindView(R.id.buttonAudioMessage)
    ImageView buttonAudioMessage;

    @BindView(R.id.buttonTextMessage)
    ImageView buttonTextMessage;

    @BindView(R.id.audioTextSwitchLayout)
    FrameLayout audioTextSwitchLayout;

    @BindView(R.id.audioRecord)
    Button audioRecord;

    @BindView(R.id.editTextMessage)
    EditText editTextMessage;

    @BindView(R.id.emoji_button)
    ImageView emoji_button;

    @BindView(R.id.sendLayout)
    FrameLayout sendLayout;

    @BindView(R.id.buttonMoreFuntionInText)
    ImageView buttonMoreFuntionInText;

    @BindView(R.id.buttonSendMessage)
    TextView buttonSendMessage;

    @OnClick(R.id.buttonSendMessage)
    void sendMessage(){
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        final EMMessage message = EMMessage.createTxtSendMessage(editTextMessage.getText().toString(), roomID);
        message.setChatType(EMMessage.ChatType.GroupChat);
        items.add(message);
        multiTypeAdapter.notifyDataSetChanged();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
    }

    @BindView(R.id.emoticon_picker_view)
    EmoticonPickerView emoticon_picker_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomid");
        owner = intent.getStringExtra("owner");

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().chatroomManager().joinChatRoom(roomID, new EMValueCallBack<EMChatRoom>() {
                    @Override
                    public void onSuccess(EMChatRoom value) {
                        LogUtil.i("ChatRoomActivity的onCreate", "进入房间成功" + value.getName());
                        init();
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
                                finish();
                            }
                        }, 2000);

                    }
                });
            }
        });
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(EMMessage.class, new MessageItemViewProvider());
        items = new Items();
        multiTypeAdapter.setItems(items);
        messageListView.setAdapter(multiTypeAdapter);

    }

    /**
     * 获取所在房间的roomID
     * @return roomid
     */
    private String getRoomID(){
        return roomID;
    }


    private EMChatRoomChangeListener chatroomListener;
    private EMMessageListener msgListener;
    /**
     * 添加监听器
     */
    private void init() {


         msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };


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
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    // 注销监听器
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatroomListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatroomListener);
        }
        if (msgListener != null){
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
    }
}
