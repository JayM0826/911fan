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

package com.imfan.j.a91fan.util.crash;

import android.content.Context;

/**
 * Created by jay on 17-1-14.
 */

public class AppCrashHandler {

    private static AppCrashHandler instance;
    private Context context;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    private AppCrashHandler(Context context) {
        this.context = context;

        // get default
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // install
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable ex) {
                // save log
                saveException(ex, true);

                // uncaught
                uncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }

    public static AppCrashHandler getInstance(Context mContext) {
        if (instance == null) {
            instance = new AppCrashHandler(mContext);
        }

        return instance;
    }

    public final void saveException(Throwable ex, boolean uncaught) {
        CrashSaver.save(context, ex, uncaught);
    }

    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        if (handler != null) {
            this.uncaughtExceptionHandler = handler;
        }
    }
}

