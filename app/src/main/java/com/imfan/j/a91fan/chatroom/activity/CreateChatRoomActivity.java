package com.imfan.j.a91fan.chatroom.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.util.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateChatRoomActivity extends Activity {


    @BindView(R.id.btn_ok)
    Button createRoom;

    @OnClick(R.id.btn_ok)
    public void createRoom(){
        CustomToast.show(this, "房间创建好了FAKE");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat_room);
        ButterKnife.bind(this);
    }
}
