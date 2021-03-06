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

package com.imfan.j.a91fan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jay on 17-3-5.
 * 动态,这里就是往本地数据库存的形式
 */

@Entity
public class Blog {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "CONTENT")
    private String content;

    @NotNull
    @Property(nameInDb = "CREATE_TIME")
    private long createTime;

    @NotNull
    @Property(nameInDb = "BLOG_ID")
    private int blogID;

    @Generated(hash = 1215796344)
    public Blog(Long id, String content, long createTime, int blogID) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.blogID = blogID;
    }

    @Generated(hash = 1388801592)
    public Blog() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getBlogID() {
        return this.blogID;
    }

    public void setBlogID(int blogID) {
        this.blogID = blogID;
    }





}
