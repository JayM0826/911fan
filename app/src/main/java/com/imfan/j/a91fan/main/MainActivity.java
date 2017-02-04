package com.imfan.j.a91fan.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.util.Preferences;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);  // 启动MainActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, Preferences.getUserAccount());
        Log.i(TAG, Preferences.getNeteaseToken());
        Log.i(TAG, Preferences.getWxNickname());

    }


}
