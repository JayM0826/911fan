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

package com.imfan.j.a91fan.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imfan.j.a91fan.R;

/**
 * Created by jay on 17-2-8.
 * 
 */

public class CustomToast {
    /**
     * 展示toast==LENGTH_SHORT
     *
     * @param msg
     */
    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int id) {
        show(context, context.getString(id), Toast.LENGTH_SHORT);
    }

    /**
     * 展示toast==LENGTH_LONG
     *
     * @param msg
     */
    public static void showLong(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, int id) {
        show(context, context.getString(id), Toast.LENGTH_LONG);
    }


    private static void show(Context context, String massage, int show_length) {
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        //获取ImageView
        ImageView image = (ImageView) view.findViewById(R.id.toast_iv);
        //获取TextView
        TextView title = (TextView) view.findViewById(R.id.toast_tv);
        //设置显示的内容
        title.setText(massage);
        Toast toast = new Toast(context);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 70);
        //设置显示时间
        toast.setDuration(show_length);

        toast.setView(view);
        toast.show();
    }

}
