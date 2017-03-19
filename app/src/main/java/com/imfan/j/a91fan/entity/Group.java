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
 * Created by jay on 17-3-6.
 */

@Entity
public class Group {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "GROUP_NAME")
    private String group_name;

    @Property(nameInDb = "CREATE_TIME")
    private String create_name;

    @Property(nameInDb = "IMAGE_PATH")
    private String image_path;

    @Generated(hash = 990148014)
    public Group(Long id, String group_name, String create_name,
            String image_path) {
        this.id = id;
        this.group_name = group_name;
        this.create_name = create_name;
        this.image_path = image_path;
    }

    @Generated(hash = 117982048)
    public Group() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup_name() {
        return this.group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreate_name() {
        return this.create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public String getImage_path() {
        return this.image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }








}
