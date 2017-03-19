/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-14 下午11:56
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

package com.imfan.j.a91fan.chatroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


@SuppressLint({"NewApi", "Registered"})
public class EaseBaseActivity extends FragmentActivity {

    protected InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        // should be in launcher activity, but all app use this can avoid the problem
        if(!isTaskRoot()){
            Intent intent = getIntent();
            String action = intent.getAction();
            if(intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)){
                finish();
                return;
            }
        }
        
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    

    @Override
    protected void onResume() {
        super.onResume();
        // cancel the notification
        EaseUI.getInstance().getNotifier().reset();
    }
    
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * back
     * 
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
