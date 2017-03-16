package com.imfan.j.a91fan.chatroom;

import java.util.List;

/**
 * Created by J on 2017/3/15 0015.
 */
public class EaseEmojiconGroupEntity {
    /**
     * 表情数据
     */
    private List<EaseEmojicon> emojiconList;
    /**
     * 图片
     */
    private int icon;
    /**
     * 组名
     */
    private String name;
    /**
     * 表情类型
     */
    private EaseEmojicon.Type type;

    public EaseEmojiconGroupEntity(){}

    public EaseEmojiconGroupEntity(int icon, List<EaseEmojicon> emojiconList){
        this.icon = icon;
        this.emojiconList = emojiconList;
        type = EaseEmojicon.Type.NORMAL;
    }

    public EaseEmojiconGroupEntity(int icon, List<EaseEmojicon> emojiconList, EaseEmojicon.Type type){
        this.icon = icon;
        this.emojiconList = emojiconList;
        this.type = type;
    }

    public List<EaseEmojicon> getEmojiconList() {
        return emojiconList;
    }
    public void setEmojiconList(List<EaseEmojicon> emojiconList) {
        this.emojiconList = emojiconList;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public EaseEmojicon.Type getType() {
        return type;
    }

    public void setType(EaseEmojicon.Type type) {
        this.type = type;
    }


}

