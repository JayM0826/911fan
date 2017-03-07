package com.imfan.j.a91fan.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jay on 17-3-5.
 * 动态
 */

@Entity
public class Blog {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "CONTENT")
    private String content;

    @Property(nameInDb = "TYPE")
    private String type;

    @Property(nameInDb = "IMAGE_PATH")
    private String image_path;

    @NotNull
    @Property(nameInDb = "CREATE_TIME")
    private String createTime;

    @Generated(hash = 403767426)
    public Blog(Long id, String content, String type, String image_path,
            @NotNull String createTime) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.image_path = image_path;
        this.createTime = createTime;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage_path() {
        return this.image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



}
