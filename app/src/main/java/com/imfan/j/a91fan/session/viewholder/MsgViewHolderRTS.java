package com.imfan.j.a91fan.session.viewholder;

import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.session.extension.RTSAttachment;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;

/**
 * Created by jay on 17-1-15.
 */

public class MsgViewHolderRTS extends MsgViewHolderBase {

    private TextView textView;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_rts;
    }

    @Override
    protected void inflateContentView() {
        textView = (TextView) view.findViewById(R.id.rts_text);
    }

    @Override
    protected void bindContentView() {
        RTSAttachment attachment = (RTSAttachment) message.getAttachment();
        textView.setText(attachment.getContent());
    }

    @Override
    protected int leftBackground() {
        return R.drawable.nim_message_left_white_bg;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_blue_bg;
    }
}


