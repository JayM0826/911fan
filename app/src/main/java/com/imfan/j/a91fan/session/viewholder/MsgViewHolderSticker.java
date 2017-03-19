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

package com.imfan.j.a91fan.session.viewholder;

import android.widget.ImageView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.session.extension.StickerAttachment;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.session.emoji.StickerManager;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by jay on 17-1-15.
 */
public class MsgViewHolderSticker extends MsgViewHolderBase {

    private ImageView baseView;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_sticker;
    }

    @Override
    protected void inflateContentView() {
        baseView = findViewById(R.id.message_item_sticker_image);
        baseView.setMaxWidth(MsgViewHolderThumbBase.getImageMaxEdge());
    }

    @Override
    protected void bindContentView() {
        StickerAttachment attachment = (StickerAttachment) message.getAttachment();
        if (attachment == null) {
            return;
        }

        ImageLoader.getInstance().displayImage(StickerManager.getInstance().getStickerBitmapUri(attachment.getCatalog
                (), attachment.getChartlet()), baseView, StickerManager.getInstance().getStickerImageOptions
                (ScreenUtil.dip2px(R.dimen.mask_sticker_bubble_width)));
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
