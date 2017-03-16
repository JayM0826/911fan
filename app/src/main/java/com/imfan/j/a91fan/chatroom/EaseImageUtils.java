package com.imfan.j.a91fan.chatroom;

import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

/**
 * Created by J on 2017/3/15 0015.
 */
public class EaseImageUtils extends com.hyphenate.util.ImageUtils{

    public static String getImagePath(String remoteUrl)
    {
        String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
        String path = PathUtil.getInstance().getImagePath()+"/"+ imageName;
        EMLog.d("msg", "image path:" + path);
        return path;

    }

    public static String getThumbnailImagePath(String thumbRemoteUrl) {
        String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
        String path =PathUtil.getInstance().getImagePath()+"/"+ "th"+thumbImageName;
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }

}

