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

import com.imfan.j.a91fan.myserver.CommentOfServer;
import com.imfan.j.a91fan.myserver.Common;
import com.imfan.j.a91fan.myserver.BlogOfServer;
import com.imfan.j.a91fan.myserver.SuccessOfServer;
import com.imfan.j.a91fan.myserver.User;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @Headers("Content-type:application/json")
    @POST("blog/create.action")
    Observable<Common> createBlog(@Query("fanID") long fanID,
                                  @Query("content") String content,
                                  @Query("time") long time);

    @Headers("Content-type:application/json")
    @GET("blog/myblog.action")
    Observable<BlogOfServer> getMyBlogFromServer(@Query("fanID") long fanID);

    /**
     * 读取某个blog下面的所有评论
     *
     * @param blogID 要读取的blog的id，是服务器那边的id
     * @return
     */
    @Headers("Content-type:application/json")
    @GET("comment/read.action")
    Observable<CommentOfServer> readCommentFromServer(@Query("blogID") long blogID);


    @Headers("Content-type:application/json")
    @GET("user/retrieve.get")
    Observable<User> getUserFromServerByFanID(@Query("id") long id);


    @Headers("Content-type:application/json")
    @GET("blog/all.action")
    Observable<BlogOfServer> getAllBlogFromServer();

    @Headers("Content-type:application/json")
    @PUT("user/update.put")
    Observable<SuccessOfServer> updateMyNickname(@Query("id") long id,
                                                 @Query("nickname") String nickname);


    @Headers("Content-type:application/json")
    @GET("blog/refresh.action")
    Observable<BlogOfServer> refreshBlog(@Query("updateTime") long updateTime);


    //
    @Headers("Content-type:application/json")
    @GET("blog/getbynetease.action")
    Observable<BlogOfServer> getBlogByWxunion(@Query("wxunion") String wxunion);

}
