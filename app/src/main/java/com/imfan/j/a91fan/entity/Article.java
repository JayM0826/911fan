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
 * 文章
 */

@Entity
public class Article {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "IMAGE_PATH")
    private String image_path;

    @Property(nameInDb = "TITLE")
    private String title;

    @Property(nameInDb = "CONTENT")
    private String content;

    @Property(nameInDb = "GROUP_ID")
    private Long groupID;

    @Property(nameInDb = "GROUP_NAME")
    private String groupName;

    @Property(nameInDb = "TYPE")
    private String type;

    @NotNull
    @Property(nameInDb = "CREATE_TIME")
    private String createTime;


    @Property(nameInDb = "UPDATE_TIME")
    private String updateTime;


    @Generated(hash = 549299751)
    public Article(Long id, String image_path, String title, String content,
            Long groupID, String groupName, String type, @NotNull String createTime,
            String updateTime) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.content = content;
        this.groupID = groupID;
        this.groupName = groupName;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }


    @Generated(hash = 742516792)
    public Article() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getImage_path() {
        return this.image_path;
    }


    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    public String getTitle() {
        return this.title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return this.content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public Long getGroupID() {
        return this.groupID;
    }


    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }


    public String getGroupName() {
        return this.groupName;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public String getType() {
        return this.type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getCreateTime() {
        return this.createTime;
    }


    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getUpdateTime() {
        return this.updateTime;
    }


    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
}
