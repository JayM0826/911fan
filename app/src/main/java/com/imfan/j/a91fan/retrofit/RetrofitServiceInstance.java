/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午7:28
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

package com.imfan.j.a91fan.retrofit;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.imfan.j.a91fan.util.Constant.BASE_SERVER;

/**
 * Created by J on 2017/3/19 0019.
 */

public class RetrofitServiceInstance {
    private static RetrofitService instance;

    private RetrofitServiceInstance() {
    }

    static public RetrofitService getInstance(){
        if (instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_SERVER)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//支持RxJava
                    .build().create(RetrofitService.class);
        }
        return instance;
    }



}
