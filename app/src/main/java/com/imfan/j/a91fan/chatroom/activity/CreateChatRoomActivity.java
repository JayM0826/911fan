package com.imfan.j.a91fan.chatroom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.imfan.j.a91fan.netease.NeteaseClient.addHeaders;
import static com.imfan.j.a91fan.netease.NeteaseClient.checkSum;
import static com.imfan.j.a91fan.netease.NeteaseClient.curTime;
import static com.imfan.j.a91fan.netease.NeteaseClient.nonce;
import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_SECRET;

public class CreateChatRoomActivity extends Activity {

    CustomEditText mRoomName;

    Button createRoom;

    public void createRoom(){

        if (mRoomName.getText().toString().equals("")){
            CustomToast.show(this, "房间名不能为空");
            return;
        }


        String url = "chatroom/create.action";

        curTime = String.valueOf((new Date()).getTime() / 1000L);
        checkSum = CheckSumBuilder.getCheckSum(NetEaseAPP_SECRET, nonce, curTime);
        RequestParams params = new RequestParams();
        params.add("creator", Preferences.getUserAccount().toLowerCase());
        params.add("name", mRoomName.getText().toString());

        addHeaders();
        NeteaseClient.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                // called before request is started
                LogUtil.i("onStart开始啦", "开始创建聊天室");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                // called when response HTTP status is "200 OK"
                LogUtil.i("onSuccess成功啦", "通信成功" + statusCode);
                try {
                    JSONObject jsonObject = object.getJSONObject("chatroom");
                    String roomName = jsonObject.getString("name");
                    String creator = jsonObject.getString("creator");
                    int roomid = jsonObject.getInt("roomid");
                    Intent intent = new Intent(CreateChatRoomActivity.this, ChatRoomActivity.class);
                    intent.putExtra("roomName", roomName);
                    intent.putExtra("creator", creator);
                    intent.putExtra("roomid", roomid);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i("onFailure失败了", "创建room失败了");
                CustomToast.show(CreateChatRoomActivity.this, "ooooops,主人,没创建好房间");
                finish();

            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat_room);
        mRoomName = (CustomEditText)findViewById(R.id.et_room_name);
        createRoom = (Button)findViewById(R.id.btn_ok);
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
