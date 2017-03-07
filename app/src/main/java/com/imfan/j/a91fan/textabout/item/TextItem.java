package com.imfan.j.a91fan.textabout.item;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.util.Date;

/**
 * Created by jay on 17-3-5.
 */
public class TextItem {
    @NonNull public ImageView cover;
    @NonNull public String name;
    @NonNull public String updateTime; // 创建时间

    public TextItem(String name, String date){
        // this.cover = imageView;
        this.name = name;
        this.updateTime = date;
    }

}