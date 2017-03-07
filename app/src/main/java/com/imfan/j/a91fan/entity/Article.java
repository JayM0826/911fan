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

    @Property(nameInDb = "TITLE")
    private String title;

    @Property(nameInDb = "CONTENT")
    private String content;

    @Property(nameInDb = "IMAGE_PATH")
    private String image_path;

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


    @Generated(hash = 333681026)
    public Article(Long id, String title, String content, String image_path,
            Long groupID, String groupName, String type, @NotNull String createTime,
            String updateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image_path = image_path;
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


    public String getImage_path() {
        return this.image_path;
    }


    public void setImage_path(String image_path) {
        this.image_path = image_path;
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
