/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-18 下午2:36
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

import com.imfan.j.a91fan.entity.Article;
import com.imfan.j.a91fan.entity.Blog;
import com.imfan.j.a91fan.myserver.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by J on 2017/3/18 0018.
 */
public interface RetrofitService {
    @Headers("Content-type:application/json")
    @POST("user/create.post")
    Observable<User> createUser(@Query("wxunion") String wxunion,
                                @Query("nickname") String nickname);
}
