/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午9:13
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

public class CommentOfServer {

    /**
     * status : 200
     * message : 获取评论列表成功
     * object : [{"id":2,"blogId":448979,"content":"我也爱","fanId":74864,"updateTime":17545},{"id":5,"blogId":448979,"content":"我也爱","fanId":741247,"updateTime":178475},{"id":4,"blogId":448979,"content":"我也爱","fanId":741247,"updateTime":1787247},{"id":3,"blogId":448979,"content":"我也爱","fanId":74864,"updateTime":1787687},{"id":1,"blogId":448979,"content":"我也爱","fanId":74864,"updateTime":17777995},{"id":6,"blogId":448979,"content":"我也爱","fanId":741247,"updateTime":17847543387}]
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
         * id : 2
         * blogId : 448979
         * content : 我也爱
         * fanId : 74864
         * updateTime : 17545
         */

        private int id;
        private int blogId;
        private String content;
        private int fanId;
        private long updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getBlogId() {
            return blogId;
        }

        public void setBlogId(int blogId) {
            this.blogId = blogId;
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
