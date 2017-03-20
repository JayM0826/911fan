/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午8:14
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

package com.imfan.j.a91fan.myserver;

import java.util.List;

/**
 * Created by J on 2017/3/19 0019.
 */

public class BlogOfServer {

    /**
     * status : 200
     * message : 获取成功，但不一定有动态
     * object : [{"id":1,"content":"我爱北京天安门","fanId":1,"updateTime":0},{"id":2,"content":"我爱北京天安门","fanId":1,"updateTime":0}]
     */

    private int status;
    private String message;
    private List<ObjectBean> object;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ObjectBean> getObject() {
        return object;
    }

    public void setObject(List<ObjectBean> object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * id : 1
         * content : 我爱北京天安门
         * fanId : 1
         * updateTime : 0
         */

        private int id;
        private String content;
        private int fanId;
        private long updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getFanId() {
            return fanId;
        }

        public void setFanId(int fanId) {
            this.fanId = fanId;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
