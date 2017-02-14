package com.imfan.j.a91fan.main.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.util.CustomToast;

public class ChatToUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_to_user);
        CustomToast.show(this, "我们愉快的聊天吧！");
    }
}
