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
    @NonNull public Long id;

    public TextItem(Long id, String name, String date){
        // this.cover = imageView;
        this.id = id;
        this.name = name;
        this.updateTime = date;
    }

}