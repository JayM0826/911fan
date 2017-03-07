package com.imfan.j.a91fan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jay on 17-3-5.
 */

/**
 * 草稿
 */

@Entity
public class Draft {
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
    private int type;

    @NotNull
    @Property(nameInDb = "CREATE_TIME")
    private String createTime;


    @Property(nameInDb = "UPDATE_TIME")
    private String updateTime;


    @Generated(hash = 462830909)
    public Draft(Long id, String image_path, String title, String content,
            Long groupID, String groupName, int type, @NotNull String createTime,
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


    @Generated(hash = 686333920)
    public Draft() {
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


    public int getType() {
        return this.type;
    }


    public void setType(int type) {
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
